package io.oigres.ecomm.service.users.api;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.oigres.ecomm.service.users.Bootstrap;
import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.repository.UserJpaRepository;

@SpringBootTest(
    classes = Bootstrap.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties"
)
@SetEnvironmentVariable(key = "AWS_ENDPOINT", value = "http://localhost:4566")
@SetEnvironmentVariable(key = "AWS_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_DEFAULT_REGION", value = "us-east-1")
@SetEnvironmentVariable(key = "AWS_ACCESS_KEY_ID", value = "test")
@SetEnvironmentVariable(key = "AWS_SECRET_ACCESS_KEY", value = "test")
class UsersControllerTest {

    @Autowired UserJpaRepository userJpaRepository;
    @Autowired MockMvc mvc;

    @Test
    void test() throws Exception {
        User user1 = User.builder()
            .email("user1@domain.com")
            .password("password1")
            .build();
        User user2 = User.builder()
            .email("user2@domain.com")
            .password("password2")
            .build();
        userJpaRepository.saveAllAndFlush(List.of(user1, user2));

        mvc.perform(get("/v1/users")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON)
            )
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
            .andExpect(jsonPath("content.[1].userId", is(2)))
        ;
    }

   @AfterEach
    void tearDown() {
        List<User> users = userJpaRepository.findAll();
        users.stream()
            .forEach(u -> userJpaRepository.delete(u));
    }

}
