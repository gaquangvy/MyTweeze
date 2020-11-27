package tweeze.modules;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//Composite
public class UserGroup implements Member {
    @Override
    public Type getType() { return Type.GROUP; }

    private String id;
    private String name;
    private final List<Member> members;
    private final List<String[]> newsfeed;
    private final long created;

    @Override
    public String firstCreated() {
        SimpleDateFormat formatter= new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        Date date = new Date(created);
        return formatter.format(date);
    }
    @Override
    public String getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public List<String[]> getNewsfeed() { return newsfeed; }
    public List<Member> getMembers() { return members; }

    @Override
    public void setName(String name) { this.name = name; }
    @Override
    public void setId(String id) {this.id = id;}

    public UserGroup() {
        id = "group" + ((int)(new Random().nextDouble() * 1000000));
        name = "New Group";
        members = new ArrayList<>();
        newsfeed = new ArrayList<>();
        created = System.currentTimeMillis();
    }

    public User foundUser(String userid) {
        for (Member mem : members)
            if (mem.equals(userid) && mem.getType() == Type.USER)
                return (User) mem;
        return null;
    }

    public void add(Member m) { members.add(m); }

    @Override
    public boolean equals(String newId) {
        return id.equals(newId);
    }

    @Override
    public JPanel showOnPage(MemberView memberView) {
        return memberView.show(this);
    }
}
