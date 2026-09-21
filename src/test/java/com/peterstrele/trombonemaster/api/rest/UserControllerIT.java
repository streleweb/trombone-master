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

    //GET-REQUESTS

    @Test
    void shouldReturnOneUserById()  throws Exception {
        mockMvc.perform(get("/api/users/77777777-7777-7777-7777-777777777777"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("77777777-7777-7777-7777-777777777777"))
                .andExpect(jsonPath("$.username").value("marco_rossi"))
                .andExpect(jsonPath("$.email").value("marco.rossi@example.com"))
                .andExpect(jsonPath("$.displayName").value("Marco Rossi"))
                .andExpect(jsonPath("$.country").value("IT"));
    }

    @Test
    void shouldFilterDisplayNameCaseInsensitively() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("displayName", "hANS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void shouldFilterCountryCaseInsensitively() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "pl")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void shouldReturnEmptyResultWhenNoUsersMatch() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "does-not-exist")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void shouldReturnEmptyResultForNonMatchingCountry() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "JP")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void shouldReturnEmptyPageWhenPageIsBeyondLastPage() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("page", "10")
                                .param("size", "3")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.page").value(10))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    @Test
    void shouldRejectNegativePage() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("page", "-1")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldRejectZeroPageSize() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("size", "0")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldRejectPageSizeAboveMaximum() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnUserNotFound() throws Exception {
        mockMvc.perform(
                        get("/api/users/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
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

    /**
     * This Test also verifies if ORDER BY username is ASCENDING
     * @throws Exception
     */
    @Test
    void shouldFilterUsernamePartially() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "hansi")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].username").value("hansi3"))
                .andExpect(jsonPath("$.content[1].username").value("hansi4"))
                .andExpect(jsonPath("$.content[2].username").value("hansi5"))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1));
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
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("hansi4"))
                .andExpect(jsonPath("$.content[0].email").value("peter@example.com"))
                .andExpect(jsonPath("$.content[0].displayName").value("Hans Strele"))
                .andExpect(jsonPath("$.content[0].country").value("PL"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
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
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].displayName").value("Hans Strele"))
                .andExpect(jsonPath("$.content[1].displayName").value("Hanswurst Strele"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldFilterCountry() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "PL")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].country").value("PL"))
                .andExpect(jsonPath("$.content[1].country").value("PL"))
                .andExpect(jsonPath("$.content[2].country").value("PL"))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldFilterMultipleCountries() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("country", "PL", "DE")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldCombineFiltersWithAnd() throws Exception {
        mockMvc.perform(
                        get("/api/users")
                                .param("username", "hansi")
                                .param("displayName", "Hans")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].username").value("hansi4"))
                .andExpect(jsonPath("$.content[1].username").value("hansi5"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
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
                .andExpect(jsonPath("$.content[0].username").value("anna_keller"))
                .andExpect(jsonPath("$.content[1].username").value("hansi3"))
                .andExpect(jsonPath("$.content[2].username").value("hansi4"))
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
                .andExpect(jsonPath("$.content[0].username").value("hansi5"))
                .andExpect(jsonPath("$.content[1].username").value("jane_smith"))
                .andExpect(jsonPath("$.content[2].username").value("john_smith"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(3))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    //POST-REQUESTS TODO
}