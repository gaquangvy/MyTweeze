package tweeze.modules;

//Clients
public class UIMembers {
    public static void main(String[] args) {
        System.out.println(generateExample1());
    }

    public static UserGroup generateExample1() {
        UserGroup root = new UserGroup();
        root.setName("Root");

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();
        User user6 = new User();
        User user7 = new User();
        User user8 = new User();

        user1.setName("User 1");
        user2.setName("User 2");
        user3.setName("User 3");
        user4.setName("User 4");
        user5.setName("User 5");
        user6.setName("User 6");
        user7.setName("User 7");
        user8.setName("User 8");

        UserGroup group1 = new UserGroup();
        UserGroup group2 = new UserGroup();
        UserGroup group3 = new UserGroup();

        group1.setName("Group 1");
        group2.setName("Group 2");
        group3.setName("Group 3");

        root.add(user1);
        root.add(user2);
        group1.add(user3);
        group1.add(user4);
        group2.add(user5);
        group2.add(user6);
        group3.add(user7);
        group3.add(user8);
        root.add(group1);
        root.add(group2);
        group1.add(group3);

        return root;
    }
}
