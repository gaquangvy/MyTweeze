package tweeze.modules;

import pages.UserView;

import javax.swing.*;

//Component
public interface Member {
    enum Type {USER, GROUP}

    //Composite Design Pattern's methods
    String getId();
    String getName();
    Type getType();
    void setName(String name);
    void setId(String id);
    boolean equals(String newId);

    //Visitor Design Pattern's methods
    void showOnPage(MemberView memberView);
}

interface MemberView {
    JPanel show(User user);
    JPanel show(UserGroup group);
}

class MemberViewImpl implements MemberView {
    @Override
    public JPanel show(User user) {
        return new UserView(user);
    }

    @Override
    public JPanel show(UserGroup group) {
        return null;
    }
}