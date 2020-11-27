package tweeze.modules;

import java.util.Random;

//Clients
public class UIMembers {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(generateExample1());
    }

    public static UserGroup generateExample1() throws InterruptedException {
        UserGroup root = new UserGroup();
        root.setName("Root");

        Random rand = new Random();
        User[] users = new User[12];
        for (int i = 0; i < 12; i++) {
            users[i] = new User();
            users[i].setName("User " + (i+1));
            Thread.sleep(10);
        }
        UserGroup[] groups = new UserGroup[3];
        for (int i = 0; i < 3; i++) {
            groups[i] = new UserGroup();
            Thread.sleep(10);
            groups[i].setName("Group " + (i+1));
        }
        for (int i = 0; i < 3; i++)
            root.add(users[i]);
        for (int i = 3; i < 12; i++)
            groups[i % 3].add(users[i]);
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 12; j++)
                if (i != j && rand.nextBoolean()) users[i].follow(users[j]);
        String[] words = new String[] {"new", "good", "bad", "sad", "normal"};
        for (User user: users) {
            for (int i = 0; i < 5; i++)
                user.post("I have a " + words[rand.nextInt(5)] + " day!");
            Thread.sleep(10);
        }

        groups[0].add(groups[1]);
        root.add(groups[0]);
        root.add(groups[2]);

        return root;
    }
}
