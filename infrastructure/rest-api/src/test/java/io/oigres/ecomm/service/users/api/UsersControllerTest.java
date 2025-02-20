/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.oigres.ecomm.service.users.config.mapper.ResponsesMapperImpl;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.usecases.uploads.GetStoreLocationUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.create.CreateNewAdminUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.delete.DeleteAdminUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.enable.DisableAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.enable.EnableAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.list.GetAllAdminsUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.search.GetAdminByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.admins.update.UpdateAdminUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.create.CreateNewConsumerUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.delete.DeleteConsumerUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.list.GetAllConsumersUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.search.GetConsumerByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.consumers.update.UpdateConsumerUseCase;
import io.oigres.ecomm.service.users.usecases.users.delete.DeleteUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.create.CreateNewDispensaryUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.delete.DeleteDispensaryUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.list.GetAllDispensariesUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.search.GetDispensaryByDispensaryIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.search.GetDispensaryByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.dispensaries.update.UpdateDispensaryUseCase;
import io.oigres.ecomm.service.users.usecases.users.gender.GetAllGendersUseCase;
import io.oigres.ecomm.service.users.usecases.users.gender.GetGenderByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.images.ChangeCardImageStatusUseCase;
import io.oigres.ecomm.service.users.usecases.users.images.ChangeProfileImageStatusUseCase;
import io.oigres.ecomm.service.users.usecases.users.list.GetAllUsersUseCase;
import io.oigres.ecomm.service.users.usecases.users.profiles.ToggleEnabledProfileUseCaseImpl;
import io.oigres.ecomm.service.users.usecases.users.search.GetUserByIdUseCase;
import io.oigres.ecomm.service.users.usecases.users.sendcode.SendCodeUseCase;
import io.oigres.ecomm.service.users.usecases.users.validate.ValidateUserUseCase;
import io.oigres.ecomm.service.users.usecases.users.verifycode.VerifyCodeUseCase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsersController.class)
@Import(ResponsesMapperImpl.class)
class UsersControllerTest {

  @MockBean CreateNewAdminUserUseCase createNewAdminUserUseCase;
  @MockBean CreateNewConsumerUserUseCase createNewConsumerUserUseCase;
  @MockBean CreateNewDispensaryUserUseCase createNewDispensaryUserUseCase;
  @MockBean GetAllUsersUseCase getAllUsersUseCase;
  @MockBean GetUserByIdUseCase getUserByIdUseCase;
  @MockBean GetAdminByIdUseCase getAdminByIdUseCase;
  @MockBean GetConsumerByIdUseCase getConsumerByIdUseCase;
  @MockBean GetDispensaryByIdUseCase getDispensaryByIdUseCase;
  @MockBean GetDispensaryByDispensaryIdUseCase getDispensaryByDispensaryIdUseCase;
  @MockBean GetAllAdminsUseCase getAllAdminsUseCase;
  @MockBean GetAllConsumersUseCase getAllConsumersUseCase;
  @MockBean GetAllDispensariesUseCase getAllDispensariesUseCase;
  @MockBean UpdateAdminUseCase updateAdminUseCase;
  @MockBean UpdateConsumerUseCase updateConsumerUseCase;
  @MockBean UpdateDispensaryUseCase updateDispensaryUseCase;
  @MockBean DeleteUserUseCase deleteUserUseCase;
  @MockBean DeleteAdminUserUseCase deleteAdminUserUseCase;
  @MockBean DeleteConsumerUserUseCase deleteConsumerUserUseCase;
  @MockBean DeleteDispensaryUserUseCase deleteDispensaryUserUseCase;
  @MockBean ValidateUserUseCase validateUserUseCase;
  @MockBean SendCodeUseCase sendCodeUseCase;
  @MockBean VerifyCodeUseCase verifyCodeUseCase;
  @MockBean GetAllGendersUseCase getAllGendersUseCase;
  @MockBean GetGenderByIdUseCase getGenderByIdUseCase;
  @MockBean EnableAdminUseCase enableAdminUseCase;
  @MockBean DisableAdminUseCase disableAdminUseCase;
  @MockBean ToggleEnabledProfileUseCaseImpl toggleEnabledProfileUseCaseImpl;
  @MockBean GetStoreLocationUseCase getStoreLocationUseCase;
  @MockBean ChangeProfileImageStatusUseCase changeProfileImageStatusUseCase;
  @MockBean ChangeCardImageStatusUseCase changeCardImageStatusUseCase;

  @Autowired private MockMvc mvc;

  @Test
  void test() throws Exception {
    User user1 = User.builder().id(1L).email("user1@domain.com").password("password1").build();
    User user2 = User.builder().id(2L).email("user2@domain.com").password("password2").build();
    given(getAllUsersUseCase.handle(any(Pageable.class)))
        .willReturn(
            PageableExecutionUtils.getPage(List.of(user1, user2), Pageable.unpaged(), () -> 2));

    mvc.perform(
            get("/api/v1/users")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("total", is(2)))
        .andExpect(jsonPath("totalElements", is(2)))
        .andExpect(jsonPath("totalPages", is(1)))
        .andExpect(jsonPath("numberOfElements", is(2)))
        .andExpect(jsonPath("pageable.page", is(0)))
        .andExpect(jsonPath("pageable.size", is(20)))
        .andExpect(jsonPath("pageable.offset", is(00)))
        .andExpect(jsonPath("pageable.pageNumber", is(0)))
        .andExpect(jsonPath("content", is(not(empty()))))
        .andExpect(jsonPath("content.[0].userId", is(1)))
        .andExpect(jsonPath("content.[1].userId", is(2)));
  }
}
