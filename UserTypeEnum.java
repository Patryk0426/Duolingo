public enum UserTypeEnum {
    Admin(1),
    User(2);

    private final int value;

    private UserTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
