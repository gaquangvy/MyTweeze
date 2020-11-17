package tweeze.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Leaf
public class User implements Member {
    public Type getType() { return Type.USER; }

    private String id;
    private String name;
    private List<String[]> newsfeed;
    private List<String> posts;
    private List<User> followings;
    private List<User> followers;

    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getPosts() { return posts; }
    public List<String[]> getNewsfeed() { return newsfeed; }
    public List<User> getFollowings() { return followings; }

    public void setName(String name) { this.name = name; }
    public void setId(String id) {this.id = id;}

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

    public boolean equals(String newId) {
        return id.equals(newId);
    }
}
