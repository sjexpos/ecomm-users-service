package io.oigres.ecomm.service.users.usecases.users.admins.create;

import io.oigres.ecomm.service.users.domain.profile.AdminProfile;
import io.oigres.ecomm.service.users.exception.profile.DeletedProfileException;
import io.oigres.ecomm.service.users.exception.profile.ExistingProfileException;
import io.oigres.ecomm.service.users.exception.profile.TypeNotFoundProfileException;

public interface CreateNewAdminUserUseCase {
    AdminProfile handle(AdminProfile adminProfile) throws DeletedProfileException, ExistingProfileException, TypeNotFoundProfileException;
}
