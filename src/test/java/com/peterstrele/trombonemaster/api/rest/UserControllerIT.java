package com.peterstrele.trombonemaster.api.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Sql(
        scripts = "/testdata/users.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class UserControllerIT {



    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("trombone_master_test")
                    .withUsername("test")
                    .withPassword("test");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );

        registry.add(
                "spring.jpa.hibernate.ddl-auto",
                () -> "create"
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOneUserById()  throws Exception {
        mockMvc.perform(get("/api/users/77777777-7777-7777-7777-777777777777"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldFilterUsernamePartially() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "hansi")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void shouldFilterUsernameCaseInsensitively() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "HANSI")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void shouldFilterEmailExactly() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("email", "peter@example.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].username").value("hansi4"));
    }

    @Test
    void shouldRejectInvalidEmailFilter() throws Exception {
        mockMvc.perform(get("/api/users").param("email", "peter"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldFilterDisplayNamePartially() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("displayName", "Hans")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void shouldFilterCountry() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "PL")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void shouldFilterMultipleCountries() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "PL", "DE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void shouldCombineFiltersWithAnd() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "hansi")
                                .param("displayName", "Hans")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void shouldPaginateUsers() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("page", "0")
                                .param("size", "3")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    @Test
    void shouldReturnSecondPage() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("page", "1")
                                .param("size", "3")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(4));
    }
}