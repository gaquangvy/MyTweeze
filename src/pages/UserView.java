package pages;

import tweeze.modules.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UserView extends JPanel implements ViewMember {
    //outputs
    private User viewed;
    private final User viewing;

    @Override
    public String getName() {
        return "My Tweeze (" + viewing.getName() + " @" + viewing.getId() + ") View ";
    }

    private JPanel userView;
    private JButton followButton;
    private JButton tweezeButton;
    private JTextArea tweezeContent;
    private JList<String> followingList;
    private JList<String> newfeedList;
    private JTextField username;
    private JLabel newsfeedTitle;
    private JLabel followingTitle;
    private JTextField changeId;
    private JButton changeIDButton;
    private JTextField changeName;
    private JButton changeNameButton;

    public UserView(User user) {
        viewed = user;
        viewing = user;
        add(userView);

        update();
        tweezeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String content = tweezeContent.getText();
                if (!content.isEmpty() && content.length() > 2)
                    viewed.post(content);
                HomeControl.getInstance().update();
            }
        });
        tweezeContent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                tweezeButton.setEnabled(!tweezeContent.getText().isEmpty() && tweezeContent.getText().length() > 2);
            }
        });
        username.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e); //Enter keycode = 10
                if (e.getKeyCode() == 10) {
                    User foundUser = null;
                    for (User user : HomeControl.getInstance().collectUsers())
                        if (user.getId().equals(username.getText()))
                            foundUser = user;
                    if (foundUser != null) view(foundUser);
                }
            }
        });
        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                username.setText("");
            }
        });
        followButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                viewing.follow(viewed);
                HomeControl.getInstance().update();
            }
        });
        changeId.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String text = changeId.getText();
                changeIDButton.setEnabled(text.length() > 2 && userCheck(text));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                String text = changeId.getText();
                if (e.getKeyCode() == 10 && text.length() > 2 && userCheck(text)) viewing.setId(text);
                HomeControl.getInstance().update();
            }
        });
        changeId.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                changeId.setText("");
                changeIDButton.setEnabled(false);
            }
        });
        changeIDButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String text = changeId.getText();
                if (text.length() > 2 && userCheck(text)) viewing.setId(text);
                HomeControl.getInstance().update();
            }
        });
        changeName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                String text = changeName.getText();
                changeNameButton.setEnabled(text.length() > 2 && !viewing.getName().equals(text));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                String text = changeName.getText();
                if (e.getKeyCode() == 10 && text.length() > 2 && !viewing.getName().equals(text)) viewing.setName(text);
                HomeControl.getInstance().update();
            }
        });
        changeName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                changeName.setText("");
                changeNameButton.setEnabled(false);
            }
        });
        changeNameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String text = changeName.getText();
                if (text.length() > 2 && !viewing.getName().equals(text)) viewing.setName(text);
                HomeControl.getInstance().update();
            }
        });
    }

    public void view(User other) {
        viewed = other;
        update();
        newsfeedTitle.setText(viewing.equals(viewed.getId()) ? "Newsfeed" : "User's Posts");
        followingTitle.setText(viewing.equals(viewed.getId()) ? "Followings" : "Mutual Follow");
    }

    private boolean userCheck(String name) {
        boolean found = false;
        for (User user : HomeControl.getInstance().collectUsers())
            found |= user.equals(name);
        return !found;
    }

    private String[] generateFollowings() {
        List<String> followingsList = new ArrayList<>();
        List<User> viewedFollowings = viewed.getFollowings();
        List<User> viewingFollowings = viewing.getFollowings();
        if (viewedFollowings.isEmpty()) followingsList.add("No Following");
        for (User viewedLocal : viewedFollowings)
            for (User viewingLocal : viewingFollowings)
                if (viewedLocal.equals(viewingLocal.getId())) {
                    followingsList.add("- " + viewedLocal.getName() + " @" + viewedLocal.getId());
                    break;
                }
        return followingsList.toArray(new String[0]);
    }

    private String[] generateNewsfeed() {
        List<String> newsfeed = new ArrayList<>();
        List<String[]> news = viewed.getNewsfeed();
        if (news.isEmpty()) newsfeed.add("Newsfeed is Empty");
        else for (String[] post : news)
            newsfeed.add("+ " + post[0] + ": " + post[1]);
        return newsfeed.toArray(new String[0]);
    }

    private String[] generatePosts() {
        if (viewed.getPosts().isEmpty()) return new String[]{"No Posts"};
        return viewed.getPosts().toArray(new String[0]);
    }

    @Override
    public void update() {
        username.setText(viewed.getId());
        followingList.setListData(generateFollowings());
        newfeedList.setListData(viewed.equals(viewing.getId()) ? generateNewsfeed() : generatePosts());
        tweezeContent.setText("");
        tweezeContent.setEnabled(viewed.equals(viewing.getId()));
        tweezeButton.setEnabled(viewed.equals(viewing.getId()));
        boolean found = false;
        for (User user : viewing.getFollowings()) found |= user.equals(viewed);
        followButton.setEnabled(!viewed.equals(viewing) && !found);
        changeId.setEnabled(viewed.equals(viewing.getId()));
        changeIDButton.setEnabled(viewed.equals(viewing.getId()));
        changeName.setEnabled(viewed.equals(viewing.getId()));
        changeNameButton.setEnabled(viewed.equals(viewing.getId()));
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
        userView = new JPanel();
        userView.setLayout(new GridBagLayout());
        userView.setBackground(new Color(-1126417));
        userView.setForeground(new Color(-16777216));
        userView.setMaximumSize(new Dimension(600, 600));
        userView.setMinimumSize(new Dimension(600, 600));
        userView.setPreferredSize(new Dimension(600, 600));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBackground(new Color(-1126417));
        panel1.setMaximumSize(new Dimension(600, 200));
        panel1.setMinimumSize(new Dimension(600, 200));
        panel1.setPreferredSize(new Dimension(600, 200));
        panel1.setRequestFocusEnabled(false);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(panel1, gbc);
        followButton = new JButton();
        followButton.setMaximumSize(new Dimension(100, 30));
        followButton.setMinimumSize(new Dimension(100, 30));
        followButton.setPreferredSize(new Dimension(100, 30));
        followButton.setText("Follow");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(followButton, gbc);
        tweezeButton = new JButton();
        tweezeButton.setMaximumSize(new Dimension(100, 50));
        tweezeButton.setMinimumSize(new Dimension(100, 50));
        tweezeButton.setPreferredSize(new Dimension(100, 50));
        tweezeButton.setText("Tweeze");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(tweezeButton, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setMaximumSize(new Dimension(340, 150));
        scrollPane1.setMinimumSize(new Dimension(340, 150));
        scrollPane1.setPreferredSize(new Dimension(340, 150));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        tweezeContent = new JTextArea();
        tweezeContent.setMaximumSize(new Dimension(340, 100));
        tweezeContent.setMinimumSize(new Dimension(340, 100));
        tweezeContent.setOpaque(true);
        tweezeContent.setPreferredSize(new Dimension(340, 100));
        scrollPane1.setViewportView(tweezeContent);
        username = new JTextField();
        username.setHorizontalAlignment(0);
        username.setMaximumSize(new Dimension(550, 50));
        username.setMinimumSize(new Dimension(550, 50));
        username.setOpaque(false);
        username.setPreferredSize(new Dimension(550, 50));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(username, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setBackground(new Color(-1126417));
        panel2.setForeground(new Color(-1126417));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        changeId = new JTextField();
        changeId.setMaximumSize(new Dimension(150, 30));
        changeId.setMinimumSize(new Dimension(150, 30));
        changeId.setPreferredSize(new Dimension(150, 30));
        panel2.add(changeId);
        changeIDButton = new JButton();
        changeIDButton.setText("Change ID");
        panel2.add(changeIDButton);
        changeName = new JTextField();
        changeName.setMaximumSize(new Dimension(150, 30));
        changeName.setMinimumSize(new Dimension(150, 30));
        changeName.setPreferredSize(new Dimension(150, 30));
        panel2.add(changeName);
        changeNameButton = new JButton();
        changeNameButton.setText("Change Name");
        panel2.add(changeNameButton);
        followingTitle = new JLabel();
        Font followingTitleFont = this.$$$getFont$$$("Comic Sans MS", Font.BOLD, 26, followingTitle.getFont());
        if (followingTitleFont != null) followingTitle.setFont(followingTitleFont);
        followingTitle.setForeground(new Color(-16777216));
        followingTitle.setHorizontalAlignment(0);
        followingTitle.setMaximumSize(new Dimension(200, 50));
        followingTitle.setMinimumSize(new Dimension(200, 50));
        followingTitle.setPreferredSize(new Dimension(200, 50));
        followingTitle.setText("Followings");
        followingTitle.setVerticalAlignment(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        userView.add(followingTitle, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(spacer1, gbc);
        newsfeedTitle = new JLabel();
        newsfeedTitle.setFocusable(true);
        Font newsfeedTitleFont = this.$$$getFont$$$("Comic Sans MS", Font.BOLD, 26, newsfeedTitle.getFont());
        if (newsfeedTitleFont != null) newsfeedTitle.setFont(newsfeedTitleFont);
        newsfeedTitle.setForeground(new Color(-16777216));
        newsfeedTitle.setHorizontalAlignment(0);
        newsfeedTitle.setMaximumSize(new Dimension(400, 50));
        newsfeedTitle.setMinimumSize(new Dimension(400, 50));
        newsfeedTitle.setPreferredSize(new Dimension(400, 50));
        newsfeedTitle.setText("Newfeeds");
        newsfeedTitle.setVerticalAlignment(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        userView.add(newsfeedTitle, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setMaximumSize(new Dimension(200, 350));
        scrollPane2.setMinimumSize(new Dimension(200, 350));
        scrollPane2.setPreferredSize(new Dimension(200, 350));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(scrollPane2, gbc);
        followingList = new JList<String>();
        followingList.setMaximumSize(new Dimension(200, 200));
        followingList.setMinimumSize(new Dimension(200, 200));
        followingList.setPreferredSize(new Dimension(200, 200));
        scrollPane2.setViewportView(followingList);
        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setMaximumSize(new Dimension(400, 350));
        scrollPane3.setMinimumSize(new Dimension(400, 350));
        scrollPane3.setPreferredSize(new Dimension(400, 350));
        scrollPane3.setVerifyInputWhenFocusTarget(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        userView.add(scrollPane3, gbc);
        newfeedList = new JList<String>();
        newfeedList.setMaximumSize(new Dimension(200, 200));
        newfeedList.setMinimumSize(new Dimension(200, 200));
        newfeedList.setPreferredSize(new Dimension(200, 200));
        scrollPane3.setViewportView(newfeedList);
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
        return userView;
    }

}
