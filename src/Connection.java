import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class Connection implements Runnable {
    private final Socket socket;
    private final Server server;
    private final BufferedReader in;
    private final PrintStream out;

    private String pendingText;

    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error Occurred.");
            throw new RuntimeException(e);
        }
        this.pendingText = null;
    }

    public void send(String text) {
        this.pendingText = text;
    }

    private Member auth(String authentication) throws AuthenticationFailed {
        HashMap<String, String> authMap = new HashMap<>();

        for (String key : authentication.split("/")) {
            try {
                authMap.put(key.split("=")[0], key.split("=")[1]);
            } catch (ArrayIndexOutOfBoundsException ignored) {
                throw new AuthenticationFailed("Invalid authentication body.");
            }
        }

        if (!(authMap.containsKey("username") && authMap.containsKey("uID"))) {
            throw new AuthenticationFailed("Invalid Authentication: Should be \"username=USERNAME/uID=UID\"");
        }
        return new Member(
            this.socket.getInetAddress().getHostAddress(),
            this.socket.getPort(),
            authMap.get("username"),
            authMap.get("uID"),
            this
        );
    }

    private Chat establishChat(Member requester, String key) throws EndpointNotFound {
        Member member = this.server.getMember(key);
        if (member != null) {
            Chat chat = server.createChat();
            chat.addMember(member);
            chat.addMember(requester);
            return chat;
        }

        Chat chat = this.server.getChat(key);
        if (chat != null) {
            chat.addMember(requester);
            return chat;
        }
        throw new EndpointNotFound("No member or chat found with key " + key + ".");
    }


    @Override
    public void run() {
        Member connectionMember = null;

        try {
            String authentication = this.in.readLine();
            connectionMember = this.auth(authentication);
            server.addMember(connectionMember);
        } catch (AuthenticationFailed e) {
            this.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error Occurred.");
            throw new RuntimeException(e);
        }

        Chat chat = null;
        this.out.println(
                "Who do you want to chat with?\n" +
                        "This can be a username or the ID of a group chat."
        );

        while (chat == null) {
            String key;
            try {
                key = this.in.readLine();
            } catch (IOException ignored) {
                return;
            }
            try {
                chat = this.establishChat(connectionMember, key);
            } catch (EndpointNotFound e) {
                this.out.println(e.getMessage());
            }
        }
        this.out.println("You are now chatting with " + chat.getKey());

        while (true) {
            try {
                if (this.pendingText != null) {
                    this.out.println(this);
                    pendingText = null;
                }
                if (this.in.ready()) {
                    String input = this.in.readLine();
                    chat.send(input);
                }
            } catch (IOException ignored) {
                break;
            }
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
