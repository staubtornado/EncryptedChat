import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    private final ArrayList<Chat> chats;
    private final ArrayList<Member> members;

    public Server(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port, 1000, InetAddress.getByName("127.0.0.1"));

        this.chats = new ArrayList<>();
        this.members = new ArrayList<>();

        while (true) {
            Socket incoming;
            try {
                incoming = socket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Connection connection = new Connection(incoming, this);
            Thread thread = new Thread(connection);
            thread.start();
            System.out.println("Accepted: " + incoming.getInetAddress());
        }
    }

    private static String getRandomHexString(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 8) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.substring(0, 8);
    }

    public synchronized Chat getChat(String key) {
        for (Chat chat : this.chats) {
            if (chat.getKey().equals(key)) {
                return chat;
            }
        }
        return null;
    }

    public synchronized Chat createChat() {
        Chat chat = new Chat(getRandomHexString());
        this.chats.add(chat);
        return chat;
    }

    public synchronized Member getMember(String uID) {
        for (Member member : this.members) {
            if (member.getUID().equals(uID)) {
                return member;
            }
        }
        return null;
    }

    public synchronized void addMember(Member member) {
        this.members.add(member);
    }
}
