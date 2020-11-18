package tweeze.modules;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Leaf
public class User implements Member {
    @Override
    public Type getType() { return Type.USER; }

    private String id;
    private String name;
    private final List<String[]> newsfeed;
    private final List<String> posts;
    private final List<User> followings;
    private final List<User> followers;

    @Override
    public String getId() { return id; }
    @Override
    public String getName() { return name; }

    public List<String> getPosts() { return posts; }
    public List<String[]> getNewsfeed() { return newsfeed; }
    public List<User> getFollowings() { return followings; }

    @Override
    public void setName(String name) { this.name = name; }
    @Override
    public void setId(String id) { this.id = id; }

    public User() {
        id = "user" + ((int)(new Random().nextDouble() * 1000000));
        name = "New User";
        posts = new ArrayList<>();
        newsfeed = new ArrayList<>();
        followings = new ArrayList<>();
        followers = new ArrayList<>();
    }

    public void follow(User someone) {
        followings.add(someone);
        someone.followers.add(this);
    }

    public void post(String tweeze) {
        posts.add(0, tweeze);
        for (User u: followers)
            u.newsfeed.add(0, new String[] {name, tweeze});
    }

    @Override
    public boolean equals(String newId) {
        return id.equals(newId);
    }

    @Override
    public JPanel showOnPage(MemberView memberView) {
        return memberView.show(this);
    }
}
