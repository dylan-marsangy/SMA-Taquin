package mailbox.type;

public enum RequestType {

    NONE("none"),
    PUSH("PUSH");

    private String type;

    RequestType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

}
