package io.oigres.ecomm.service.users.exception.profile;

public class EnableStatusProfileException extends ProfileUserException {
    public EnableStatusProfileException(Boolean enabledStatus) {
        super("Profile's enabled status was already on " + enabledStatus);
    }
}
