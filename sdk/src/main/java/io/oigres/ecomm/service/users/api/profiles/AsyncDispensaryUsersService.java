package io.oigres.ecomm.service.users.api.profiles;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.users.api.model.dispensary.GetDispensaryUserResponse;

public interface AsyncDispensaryUsersService {
    Future<GetDispensaryUserResponse> getDispensaryUserByIdAsync(Long userId);
    Future<GetDispensaryUserResponse> getDispensaryByDispensaryIdAsync(Long dispensaryId);
}
