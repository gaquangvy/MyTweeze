package pages;

import tweeze.modules.Member;
import tweeze.modules.UIMembers;
import tweeze.modules.User;
import tweeze.modules.UserGroup;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class HomeControl extends JPanel {
    private JPanel homeControl;
    private JPanel left;
    private JPanel right;
    private JTree treeGroup;
    private JLabel treeViewTitle;
    private JLabel treeSelected;
    private JScrollPane treePane;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton numberOfTotalUsersButton;
    private JButton numberOfTotalGroupsButton;
    private JButton numberOfTweezesButton;
    private JButton showPositiveTweezeButton;
    private JButton userViewButton;
    private JButton groupViewButton;
    private JTextField userTextField;
    private JTextField groupTextField;

    //value input to show
    private final UserGroup root;
    private Member chosenMember;
    private List<UserView> panels = new ArrayList<>();

    public String getName() {
        return "My Tweeze (ADMIN)";
    }

    private static HomeControl instance = null;

    private HomeControl() {
        add(homeControl);
        root = UIMembers.generateExample1();
        root.setId("Root");
        update();
        treeGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                TreePath currentPath = treeGroup.getPathForLocation(evt.getX(), evt.getY());
                treeSelected.setText((currentPath != null) ? currentPath.getLastPathComponent().toString() : "");
                if (currentPath == null) {
                    userViewButton.setEnabled(false);
                    groupViewButton.setEnabled(false);
                    chosenMember = null;
                } else {
                    int numOfElement = currentPath.getPathCount();
                    chosenMember = root;
                    for (int i = 0; i < numOfElement; ++i) {
                        List<Member> members = ((UserGroup) chosenMember).getMembers();
                        for (Member m : members)
                            if (m.getId().equals(currentPath.getPathComponent(i).toString())) {
                                chosenMember = m;
                                break;
                            }
                    }
                    userViewButton.setEnabled(chosenMember.getType() == Member.Type.USER);
                    groupViewButton.setEnabled(chosenMember.getType() == Member.Type.GROUP);
                }
            }
        });

        addUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String newID = userTextField.getText();
                if (!newID.isEmpty()) {
                    boolean duplicate = false;
                    for (User user : collectUsers())
                        duplicate |= user.equals(newID);
                    if (duplicate) treeSelected.setText("NO DUPLICATION");
                    else {
                        User user = new User();
                        user.setId(newID);
                        root.add(user);
                        update();
                    }
                }
            }
        });
        userTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                addUserButton.setEnabled(!userTextField.getText().isEmpty());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e); // Enter keycode = 10
                if (e.getKeyCode() == 10) {
                    String newID = userTextField.getText();
                    if (!newID.isEmpty()) {
                        boolean duplicate = false;
                        for (User user : collectUsers())
                            duplicate |= user.equals(newID);
                        if (duplicate) treeSelected.setText("NO DUPLICATION");
                        else {
                            User user = new User();
                            user.setId(newID);
                            root.add(user);
                            update();
                        }
                    }
                }
            }
        });
        treeGroup.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                super.keyPressed(evt); //Delete key code = 127
                update();
            }
        });
        numberOfTotalUsersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (numberOfTotalUsersButton.getText().equals("Show Total Users"))
                    numberOfTotalUsersButton.setText(collectUsers().size() + " Users in Total");
                else numberOfTotalUsersButton.setText("Show Total Users");
            }
        });
        numberOfTotalGroupsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (numberOfTotalGroupsButton.getText().equals("Show Total Groups"))
                    numberOfTotalGroupsButton.setText(collectGroups().size() + " Groups in Total");
                else numberOfTotalGroupsButton.setText("Show Total Groups");
            }
        });
        numberOfTweezesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (numberOfTweezesButton.getText().equals("Show Total Tweezes")) {
                    int result = 0;
                    for (User user : collectUsers())
                        result += user.getPosts().size();
                    numberOfTweezesButton.setText(result + " Tweezes in Total");
                } else numberOfTweezesButton.setText("Show Total Tweezes");
            }
        });
        showPositiveTweezeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (showPositiveTweezeButton.getText().equals("Show Positive Tweeze")) {
                    int result = 0, totalPosts = 0;
                    for (User user : collectUsers()) {
                        totalPosts += user.getPosts().size();
                        for (String tweeze : user.getPosts()) {
                            List<String> positiveDictionary;
                            try {
                                positiveDictionary = PositiveWordsCollection.outList();
                            } catch (FileNotFoundException fileNotFoundException) {
                                positiveDictionary = new ArrayList<>();
                            }
                            for (String word : positiveDictionary)
                                if (tweeze.toLowerCase().contains(word)) result++;
                        }
                    }
                    showPositiveTweezeButton.setText((totalPosts == 0 ? 0 : (result * 100 / totalPosts)) + "% Tweezes in Total");
                } else showPositiveTweezeButton.setText("Show Positive Tweeze");
            }
        });
        userViewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                UserView userView = new UserView((User) chosenMember);
                panels.add(userView);
                new FramePage(userView);
            }
        });
        groupTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                addGroupButton.setEnabled(!groupTextField.getText().isEmpty());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10) {
                    String newID = groupTextField.getText();
                    if (!newID.isEmpty()) {
                        boolean duplicate = false;
                        for (UserGroup group : collectGroups())
                            duplicate |= group.equals(newID);
                        if (duplicate) treeSelected.setText("NO DUPLICATION");
                        else {
                            UserGroup group = new UserGroup();
                            group.setId(newID);
                            root.add(group);
                            update();
                        }
                    }
                }
            }
        });
        addGroupButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String newID = groupTextField.getText();
                if (!newID.isEmpty()) {
                    boolean duplicate = false;
                    for (UserGroup group : collectGroups())
                        duplicate |= group.equals(newID);
                    if (duplicate) treeSelected.setText("NO DUPLICATION");
                    else {
                        UserGroup group = new UserGroup();
                        group.setId(newID);
                        root.add(group);
                        update();
                    }
                }
            }
        });
    }

    private DefaultMutableTreeNode generateTree(UserGroup root) {
        DefaultMutableTreeNode example = new DefaultMutableTreeNode(root.getId());
        example.setAllowsChildren(true);
        if (root.getMembers().isEmpty()) example.add(new DefaultMutableTreeNode());
        for (Member m : root.getMembers())
            if (m.getType() == Member.Type.USER)
                example.add(new DefaultMutableTreeNode(m.getId()));
            else example.add(generateTree((UserGroup) m));
        return example;
    }

    public List<User> collectUsers() {
        List<User> users = new ArrayList<>();
        List<UserGroup> groups = new ArrayList<>();
        groups.add(root);
        while (!groups.isEmpty()) {
            for (Member m : groups.get(0).getMembers())
                if (m.getType() == Member.Type.GROUP) groups.add((UserGroup) m);
                else users.add((User) m);
            groups.remove(0);
        }
        return users;
    }

    public List<UserGroup> collectGroups() {
        List<UserGroup> groups = new ArrayList<>();
        groups.add(root);
        int i = 0;
        while (i < groups.size()) {
            for (Member m : groups.get(i).getMembers())
                if (m.getType() == Member.Type.GROUP) groups.add((UserGroup) m);
            ++i;
        }
        groups.remove(0); //no root groups (admin)
        return groups;
    }

    public static HomeControl getInstance() {
        if (instance == null) instance = new HomeControl();
        return instance;
    }

    public void update() {
        treeGroup.setModel(new DefaultTreeModel(generateTree(root)));
        userViewButton.setEnabled(false);
        groupViewButton.setEnabled(false);
        addUserButton.setEnabled(false);
        addGroupButton.setEnabled(false);
        groupTextField.setText("");
        userTextField.setText("");
        treeSelected.setText("");
        numberOfTotalUsersButton.setText("Show Total Users");
        numberOfTotalGroupsButton.setText("Show Total Groups");
        numberOfTweezesButton.setText("Show Total Tweezes");
        showPositiveTweezeButton.setText("Show Positive Tweeze");
        for (UserView view : panels)
            view.update();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        homeControl = new JPanel();
        homeControl.setLayout(new GridBagLayout());
        homeControl.setBackground(new Color(-12513218));
        homeControl.setMaximumSize(new Dimension(600, 400));
        homeControl.setMinimumSize(new Dimension(600, 400));
        homeControl.setPreferredSize(new Dimension(600, 400));
        right = new JPanel();
        right.setLayout(new GridBagLayout());
        right.setBackground(new Color(-4168530));
        right.setMaximumSize(new Dimension(400, 400));
        right.setMinimumSize(new Dimension(400, 400));
        right.setPreferredSize(new Dimension(400, 400));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        homeControl.add(right, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBackground(new Color(-1126417));
        panel1.setForeground(new Color(-15464429));
        panel1.setMaximumSize(new Dimension(400, 200));
        panel1.setMinimumSize(new Dimension(400, 200));
        panel1.setPreferredSize(new Dimension(400, 200));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        right.add(panel1, gbc);
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-16777216));
        label1.setMaximumSize(new Dimension(60, 100));
        label1.setMinimumSize(new Dimension(60, 100));
        label1.setPreferredSize(new Dimension(60, 100));
        label1.setText("Group ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label1, gbc);
        addUserButton = new JButton();
        addUserButton.setText("Add User");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(addUserButton, gbc);
        addGroupButton = new JButton();
        addGroupButton.setText("Add Group");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(addGroupButton, gbc);
        final JLabel label2 = new JLabel();
        label2.setBackground(new Color(-12517319));
        label2.setForeground(new Color(-16777216));
        label2.setMaximumSize(new Dimension(60, 100));
        label2.setMinimumSize(new Dimension(60, 100));
        label2.setPreferredSize(new Dimension(60, 100));
        label2.setText("User ID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel1.add(label2, gbc);
        userTextField = new JTextField();
        userTextField.setMaximumSize(new Dimension(150, 60));
        userTextField.setMinimumSize(new Dimension(150, 60));
        userTextField.setPreferredSize(new Dimension(150, 60));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(userTextField, gbc);
        groupTextField = new JTextField();
        groupTextField.setMaximumSize(new Dimension(150, 60));
        groupTextField.setMinimumSize(new Dimension(150, 60));
        groupTextField.setPreferredSize(new Dimension(150, 60));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(groupTextField, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setBackground(new Color(-5008204));
        panel2.setMaximumSize(new Dimension(400, 100));
        panel2.setMinimumSize(new Dimension(400, 100));
        panel2.setPreferredSize(new Dimension(400, 100));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        right.add(panel2, gbc);
        groupViewButton = new JButton();
        groupViewButton.setMaximumSize(new Dimension(180, 30));
        groupViewButton.setMinimumSize(new Dimension(180, 30));
        groupViewButton.setOpaque(true);
        groupViewButton.setPreferredSize(new Dimension(180, 30));
        groupViewButton.setText("Group View");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(groupViewButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        userViewButton = new JButton();
        userViewButton.setMaximumSize(new Dimension(180, 30));
        userViewButton.setMinimumSize(new Dimension(180, 30));
        userViewButton.setOpaque(true);
        userViewButton.setPreferredSize(new Dimension(180, 30));
        userViewButton.setText("User View");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(userViewButton, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setBackground(new Color(-1126417));
        panel3.setMaximumSize(new Dimension(400, 200));
        panel3.setMinimumSize(new Dimension(400, 200));
        panel3.setPreferredSize(new Dimension(400, 200));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        right.add(panel3, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel4.setBackground(new Color(-1126417));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        panel3.add(panel4, gbc);
        numberOfTotalUsersButton = new JButton();
        numberOfTotalUsersButton.setMaximumSize(new Dimension(180, 60));
        numberOfTotalUsersButton.setMinimumSize(new Dimension(180, 60));
        numberOfTotalUsersButton.setPreferredSize(new Dimension(180, 60));
        numberOfTotalUsersButton.setText("Show Total Users");
        panel4.add(numberOfTotalUsersButton);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel5.setBackground(new Color(-1126417));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        panel3.add(panel5, gbc);
        numberOfTotalGroupsButton = new JButton();
        numberOfTotalGroupsButton.setMaximumSize(new Dimension(180, 60));
        numberOfTotalGroupsButton.setMinimumSize(new Dimension(180, 60));
        numberOfTotalGroupsButton.setPreferredSize(new Dimension(180, 60));
        numberOfTotalGroupsButton.setText("Show Total Groups");
        panel5.add(numberOfTotalGroupsButton);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel6.setBackground(new Color(-1126417));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        panel3.add(panel6, gbc);
        numberOfTweezesButton = new JButton();
        numberOfTweezesButton.setHorizontalTextPosition(0);
        numberOfTweezesButton.setMaximumSize(new Dimension(180, 60));
        numberOfTweezesButton.setMinimumSize(new Dimension(180, 60));
        numberOfTweezesButton.setPreferredSize(new Dimension(180, 60));
        numberOfTweezesButton.setText("Show Total Tweezes");
        panel6.add(numberOfTweezesButton);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel7.setBackground(new Color(-1126417));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel3.add(panel7, gbc);
        showPositiveTweezeButton = new JButton();
        showPositiveTweezeButton.setMaximumSize(new Dimension(180, 60));
        showPositiveTweezeButton.setMinimumSize(new Dimension(180, 60));
        showPositiveTweezeButton.setPreferredSize(new Dimension(180, 60));
        showPositiveTweezeButton.setText("Show Positive Tweeze");
        panel7.add(showPositiveTweezeButton);
        left = new JPanel();
        left.setLayout(new GridBagLayout());
        left.setBackground(new Color(-4168530));
        left.setMaximumSize(new Dimension(200, 400));
        left.setMinimumSize(new Dimension(200, 400));
        left.setPreferredSize(new Dimension(200, 400));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        homeControl.add(left, gbc);
        treeViewTitle = new JLabel();
        Font treeViewTitleFont = this.$$$getFont$$$("Baskerville Old Face", Font.BOLD, 22, treeViewTitle.getFont());
        if (treeViewTitleFont != null) treeViewTitle.setFont(treeViewTitleFont);
        treeViewTitle.setForeground(new Color(-1114370));
        treeViewTitle.setHorizontalAlignment(0);
        treeViewTitle.setHorizontalTextPosition(0);
        treeViewTitle.setText("Tree View");
        treeViewTitle.setVerticalAlignment(3);
        treeViewTitle.setVerticalTextPosition(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        left.add(treeViewTitle, gbc);
        treeSelected = new JLabel();
        Font treeSelectedFont = this.$$$getFont$$$(null, -1, 14, treeSelected.getFont());
        if (treeSelectedFont != null) treeSelected.setFont(treeSelectedFont);
        treeSelected.setForeground(new Color(-15464429));
        treeSelected.setHorizontalAlignment(0);
        treeSelected.setHorizontalTextPosition(0);
        treeSelected.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        left.add(treeSelected, gbc);
        treePane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        left.add(treePane, gbc);
        treeGroup = new JTree();
        treeGroup.setBackground(new Color(-1132305));
        treeGroup.setDoubleBuffered(true);
        treePane.setViewportView(treeGroup);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return homeControl;
    }

}
