package pages;

import tweeze.modules.Member;
import tweeze.modules.User;
import tweeze.modules.UserGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GroupView extends JPanel implements ViewMember {
    @Override
    public String getName() {
        return currentGroup.getName() + " (@" + currentGroup.getId() + ") View ";
    }

    private JPanel panel1;
    private JTextField changeID;
    private JButton changeIDButton;
    private JTextField changeName;
    private JButton changeNameButton;
    private JList<String> list1;
    private JList<String> list2;
    private JLabel created;

    private final UserGroup currentGroup;

    public GroupView(UserGroup group) {
        currentGroup = group;
        update();
        add(panel1);
        changeID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                changeIDButton.setEnabled(changeID.getText().length() > 2);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10) {
                    boolean found = false;
                    List<UserGroup> groups = null;
                    try {
                        groups = HomeControl.getInstance().collectGroups();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    for (UserGroup group : groups) found |= group.equals(changeID.getText());
                    if (found) errorMessage("Your desired ID must be unique!");
                    else if (changeID.getText().contains(" "))
                        errorMessage("Your desired ID cannot have a space among it!");
                    else currentGroup.setId(changeID.getText());
                    try {
                        HomeControl.getInstance().update();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        changeIDButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean found = false;
                List<UserGroup> groups = null;
                try {
                    groups = HomeControl.getInstance().collectGroups();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                for (UserGroup group : groups) found |= group.equals(changeID.getText());
                if (found) errorMessage("Your desired ID must be unique!");
                else if (changeID.getText().contains(" "))
                    errorMessage("Your desired ID cannot have a space among it!");
                else currentGroup.setId(changeID.getText());
                try {
                    HomeControl.getInstance().update();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        changeName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                changeNameButton.setEnabled(changeName.getText().length() > 2);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10) {
                    currentGroup.setName(changeName.getText());
                    try {
                        HomeControl.getInstance().update();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        changeNameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentGroup.setName(changeName.getText());
                try {
                    HomeControl.getInstance().update();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                User found = null;
                JList list = (JList) e.getSource();
                int index = list.locationToIndex(e.getPoint());
                String target = list.getModel().getElementAt(index).toString();
                if (!target.equals("Users:") && !target.equals("Groups:"))
                    found = currentGroup.foundUser(target.split("@")[1]);
                if (found != null) list2.setListData(found.getPosts().toArray(new String[0]));
                else list2.setListData(new String[]{"No selected user to view his/her posts."});
            }
        });
    }

    @Override
    public void update() {
        changeID.setText(currentGroup.getId());
        changeName.setText(currentGroup.getName());
        changeIDButton.setEnabled(false);
        changeNameButton.setEnabled(false);
        list1.setListData(seperatedMembers());
        list2.setListData(new String[]{"No selected user to view his/her posts."});
        created.setText("Created on " + currentGroup.firstCreated());
    }

    private String[] seperatedMembers() {
        List<String> result = new ArrayList<>();
        result.add("Users:");
        for (Member mem : currentGroup.getMembers())
            if (mem.getType() == Member.Type.USER)
                result.add("- " + mem.getName() + " @" + mem.getId());
        result.add("Groups:");
        for (Member mem : currentGroup.getMembers())
            if (mem.getType() == Member.Type.GROUP)
                result.add("- " + mem.getName() + " @" + mem.getId());
        return result.toArray(new String[0]);
    }

    private void errorMessage(String error) {
        JOptionPane.showMessageDialog(this, error, "Error Found!", JOptionPane.ERROR_MESSAGE);
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
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBackground(new Color(-12236470));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setBackground(new Color(-1126417));
        panel2.setMaximumSize(new Dimension(600, 40));
        panel2.setMinimumSize(new Dimension(600, 40));
        panel2.setPreferredSize(new Dimension(600, 40));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        changeID = new JTextField();
        changeID.setHorizontalAlignment(0);
        changeID.setMaximumSize(new Dimension(450, 30));
        changeID.setMinimumSize(new Dimension(450, 30));
        changeID.setPreferredSize(new Dimension(450, 30));
        panel2.add(changeID);
        changeIDButton = new JButton();
        changeIDButton.setMaximumSize(new Dimension(110, 30));
        changeIDButton.setMinimumSize(new Dimension(110, 30));
        changeIDButton.setPreferredSize(new Dimension(110, 30));
        changeIDButton.setText("Change ID");
        panel2.add(changeIDButton);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel3.setBackground(new Color(-12517319));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Forte", Font.BOLD, 20, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1114370));
        label1.setHorizontalAlignment(0);
        label1.setText("Member");
        panel3.add(label1, BorderLayout.NORTH);
        list1 = new JList();
        list1.setBackground(new Color(-1126417));
        Font list1Font = this.$$$getFont$$$(null, -1, 12, list1.getFont());
        if (list1Font != null) list1.setFont(list1Font);
        list1.setForeground(new Color(-16777216));
        list1.setMaximumSize(new Dimension(150, 150));
        list1.setMinimumSize(new Dimension(150, 150));
        list1.setPreferredSize(new Dimension(150, 150));
        panel3.add(list1, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel4.setBackground(new Color(-12517319));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel4, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Forte", Font.BOLD, 20, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1114370));
        label2.setHorizontalAlignment(0);
        label2.setText("Newsfeed");
        panel4.add(label2, BorderLayout.NORTH);
        list2 = new JList();
        list2.setBackground(new Color(-1126417));
        Font list2Font = this.$$$getFont$$$(null, -1, 12, list2.getFont());
        if (list2Font != null) list2.setFont(list2Font);
        list2.setForeground(new Color(-16777216));
        list2.setMaximumSize(new Dimension(430, 200));
        list2.setMinimumSize(new Dimension(430, 200));
        list2.setPreferredSize(new Dimension(430, 200));
        panel4.add(list2, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel5.setBackground(new Color(-1126417));
        panel5.setForeground(new Color(-1126417));
        panel5.setMaximumSize(new Dimension(600, 40));
        panel5.setMinimumSize(new Dimension(600, 40));
        panel5.setPreferredSize(new Dimension(600, 40));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel5, gbc);
        changeName = new JTextField();
        changeName.setHorizontalAlignment(0);
        changeName.setMaximumSize(new Dimension(450, 30));
        changeName.setMinimumSize(new Dimension(450, 30));
        changeName.setPreferredSize(new Dimension(450, 30));
        panel5.add(changeName);
        changeNameButton = new JButton();
        changeNameButton.setText("Change Name");
        panel5.add(changeNameButton);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(spacer1, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel6.setBackground(new Color(-4168530));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel6, gbc);
        created = new JLabel();
        created.setForeground(new Color(-16777216));
        created.setHorizontalAlignment(0);
        created.setHorizontalTextPosition(0);
        created.setMaximumSize(new Dimension(600, 30));
        created.setMinimumSize(new Dimension(600, 30));
        created.setPreferredSize(new Dimension(600, 30));
        created.setText("Label");
        panel6.add(created);
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
        return panel1;
    }

}
