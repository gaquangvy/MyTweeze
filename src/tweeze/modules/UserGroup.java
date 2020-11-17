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
    public void remove(Member mem) {
        Member found = null;
        for (Member m: members)
            if (m.equals(mem.getId()) && m.getType() == mem.getType())
                found = m;
        if (found != null) members.remove(found);
    }

    public boolean equals(String newId) {
        return id.equals(newId);
    }
}
