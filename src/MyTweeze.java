import pages.FramePage;
import pages.HomeControl;

import javax.swing.*;

public class MyTweeze {
    public static void main(String[] args) throws InterruptedException {
        FramePage homepage = new FramePage(HomeControl.getInstance());
        homepage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
