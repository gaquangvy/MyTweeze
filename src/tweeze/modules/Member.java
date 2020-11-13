package tweeze.modules;

//Component
public interface Member {
    public enum Type {USER, GROUP};

    //necessary getters + setters
    String getId();
    String getName();
    Type getType();
    void setName(String name);
    void setId(String id);
}
