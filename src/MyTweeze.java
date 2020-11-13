import pages.FramePage;
import pages.HomeControl;

public class MyTweeze {
    public static void main(String[] args) {
        FramePage homepage = new FramePage(HomeControl.getInstance());
    }
}
