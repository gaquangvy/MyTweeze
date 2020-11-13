package tweeze.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Composite
public class UserGroup implements Member {
    public Type getType() { return Type.GROUP; }

    private String id;
    private String name;
    private List<Member> members;

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Member> getMembers() { return members; }

    public void setName(String name) { this.name = name; }
    public void setId(String id) {this.id = id;}
    public UserGroup() {
        id = "group" + ((int)(new Random().nextDouble() * 1000000));
        name = "New Group";
        members = new ArrayList<>();
    }

    public void add(Member m) { members.add(m); }
    public void remove(String id, Type type) {
        for (Member m: members)
            if (m.getId().equals(id) && m.getType() == type)
                members.remove(m);
    }

    public boolean search(String id, Type type) {
        for (Member m: members)
            if (m.getId().equals(id) && m.getType() == type) return true;
            else if (m.getType() == Type.GROUP) return ((UserGroup) m).search(id, type);
        return false;
    }

    @Override
    public String toString() {
        String result = name + ": [";
        for (Member m: members)
            if (m.getId().contains("user")) result += m.getName() + ", ";
            else result += m;
        result += "]";
        return result;
    }
}
