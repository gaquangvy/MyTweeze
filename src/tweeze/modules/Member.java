package tweeze.modules;

import javax.swing.*;
import java.util.List;

//Component
public interface Member {
    enum Type {USER, GROUP}

    //Composite Design Pattern's methods
    String getId();
    String getName();
    Type getType();
    List<String[]> getNewsfeed();
    void setName(String name);
    void setId(String id);
    boolean equals(String newId);

    //Visitor Design Pattern's methods
    JPanel showOnPage(MemberView memberView);
}