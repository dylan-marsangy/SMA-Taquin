package puzzle.agent.state.name;

public enum StateName {

    NONE("none"),
    STARTING("STARTING"),
    MOVING("MOVING"),
    READING("READING"),
    SLEEPING("SLEEPING"),
    PUSHED("PUSHED"),
    WAITING("WAITING");

    private String name;

    StateName(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
