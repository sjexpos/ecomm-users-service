package io.oigres.ecomm.service.users.enums;

import java.util.Optional;

public enum ConsumerTypeEnum {

    MEDICAL(1L,"Medical"),
    RECREATIONAL(2L,"Recreational");
    private final Long id;
    private final String prettyName;

    ConsumerTypeEnum(Long id, String prettyName) {
            this.id = id;
            this.prettyName = prettyName;
    }

    public static Optional<ConsumerTypeEnum> getById(Long id) {
        for (ConsumerTypeEnum e : values()) {
            if (e.getId().equals(id)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Long getId() { return id; }

    public String getPrettyName() { return prettyName; }
}
