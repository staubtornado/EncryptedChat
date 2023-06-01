public class Member {
    private final String ADDRESS;
    private final int PORT;
    private final String USERNAME;
    private final String UID;

    private final Connection connection;

    public Member(String address, int port, String username, String uID, Connection connection) {
        this.ADDRESS = address;
        this.PORT = port;
        this.USERNAME = username;
        this.UID = uID;
        this.connection = connection;
    }

    public String getAddress() {
        return this.ADDRESS;
    }

    public int getPort() {
        return PORT;
    }

    public String getUsername() {
        return this.USERNAME;
    }

    public String getUID() {
        return this.UID;
    }

    public void send(String text) {
        this.connection.send(text);
    }
}
