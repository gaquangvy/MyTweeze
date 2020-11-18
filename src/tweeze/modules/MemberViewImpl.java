package tweeze.modules;

import pages.GroupView;
import pages.UserView;

import javax.swing.*;

public class MemberViewImpl implements MemberView {
    @Override
    public JPanel show(User user) {
        return new UserView(user);
    }

    @Override
    public JPanel show(UserGroup group) {
        return new GroupView(group);
    }
}
