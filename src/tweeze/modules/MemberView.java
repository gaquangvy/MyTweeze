package tweeze.modules;

import javax.swing.*;

public interface MemberView {
    JPanel show(User user);
    JPanel show(UserGroup group);
}

