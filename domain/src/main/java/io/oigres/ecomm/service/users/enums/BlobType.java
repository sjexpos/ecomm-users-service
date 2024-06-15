package io.oigres.ecomm.service.users.enums;

public enum BlobType {
    PROFILE_IMAGE("profiles/images"),
    MMJ_CARD_IMAGE("mmjcard/images");

    private String folder;

    BlobType(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
