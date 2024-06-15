package io.oigres.ecomm.service.users.enums;

public enum ProfileTypeEnum {
    ADMIN(1, "Admin"),
    CONSUMER(2, "Consumer"),
    DISPENSARY(3, "Dispensary"),
    GROWER(4, "Grower");

    private final Integer id;
    private final String prettyName;

    ProfileTypeEnum(Integer id, String prettyName) {
        this.id = id;
        this.prettyName = prettyName;
    }

    public Integer getId() { return id; }

    public String getPrettyName() { return prettyName; }
}
