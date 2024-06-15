package io.oigres.ecomm.service.users.api.profiles;

import java.util.concurrent.Future;

import io.oigres.ecomm.service.users.api.model.admin.GetAdminUserResponse;

public interface AsyncAdminUsersService {
    Future<GetAdminUserResponse> getAdminUserByIdAsync(long userId);
}
