import java.util.ArrayList;

public class Chat {
    private final ArrayList<Member> members;
    private final String key;
    private final ArrayList<String> messages;

    public Chat(String key) {
        this.members = new ArrayList<>();
        this.key = key;
        this.messages = new ArrayList<>();
    }

    public String getKey() {
        return this.key;
    }

    public void addMember(Member member) {
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i) == null) {
                this.members.set(i, member);
                return;
            }
        }
    }

    public void removeMember(Member member) {
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i) == member) {
                this.members.set(i, null);
                return;
            }
        }
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public String[] getMessages() {
        return this.messages.toArray(new String[0]);
    }

    public ArrayList<Member> getMembers() {
        return this.members;
    }

    public void send(String text) {
        for (Member member : this.members) {
            if (member != null) {
                member.send(text);
            }
        }
    }
}
