package io.oigres.ecomm.service.users.usecases.users.utils;

import io.oigres.ecomm.service.users.domain.ProfileImage;
import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.enums.ResourceStatusEnum;

public class ImageUtils {
    public ImageUtils() {
        throw new AssertionError();
    }

    public static String getProfileImageURLForConsumerUser(ConsumerProfile profile) {
        ProfileImage profileImage = profile.getProfileImage();
        return getProfileImageURL(profileImage);
    }

    public static String getProfileImageURLForAdminUser(AdminProfile profile) {
        ProfileImage profileImage = profile.getProfileImage();
        return getProfileImageURL(profileImage);
    }

    private static String getProfileImageURL(ProfileImage profileImage) {
        String url = null;
        if (profileImage != null
                && profileImage.getImageURL() != null
                && ResourceStatusEnum.UPLOADED.equals(profileImage.getStatus())) {
            url = profileImage.getImageURL();
        }
        return url;
    }
}
