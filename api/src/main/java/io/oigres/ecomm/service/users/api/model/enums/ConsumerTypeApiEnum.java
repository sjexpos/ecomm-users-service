package io.oigres.ecomm.service.users.api.model.enums;

public enum ConsumerTypeApiEnum {

    MEDICAL(1L, "Medical"),
    RECREATIONAL(2L, "Recreational");
    private final Long id;
    private final String prettyName;

    ConsumerTypeApiEnum(Long id, String prettyName) {
        this.id = id;
        this.prettyName = prettyName;
    }

    public Long getId() {
        return id;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
