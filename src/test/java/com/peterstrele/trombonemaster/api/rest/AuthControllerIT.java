package com.peterstrele.trombonemaster.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peterstrele.trombonemaster.infrastructure.postgresql.entities.UserEntity;
import com.peterstrele.trombonemaster.infrastructure.postgresql.repositories.SpringDataUserRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Sql(
        scripts = "/testdata/users.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class AuthControllerIT {

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

    @Autowired
    private SpringDataUserRepository userRepository;

    @Test
    void shouldRegisterUser() throws Exception {

        String request = """
                {
                    "username": "newplayer",
                    "email": "newplayer@example.com",
                    "password": "SecurePassword123!",
                    "displayName": "New Player",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("newplayer"))
                .andExpect(jsonPath("$.email").value("newplayer@example.com"))
                .andExpect(jsonPath("$.displayName").value("New Player"))
                .andExpect(jsonPath("$.country").value("AT"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void shouldNormalizeUserDataWhenRegistering() throws Exception {

        String request = """
            {
                "username": "NewPlayer2",
                "email": "NEWPLAYER2@EXAMPLE.COM",
                "password": "SecurePassword123!",
                "displayName": "New    Player",
                "country": "at"
            }
            """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newplayer2"))
                .andExpect(jsonPath("$.email").value("newplayer2@example.com"))
                .andExpect(jsonPath("$.displayName").value("New Player"))
                .andExpect(jsonPath("$.country").value("AT"));
    }

    @Test
    void shouldStorePasswordAsHash() throws Exception {

        String rawPassword = "SecurePassword123!";

        String request = """
                {
                    "username": "hashedplayer",
                    "email": "hashedplayer@example.com",
                    "password": "%s",
                    "displayName": "Hashed Player",
                    "country": "DE"
                }
                """.formatted(rawPassword);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isCreated());

        Optional<UserEntity> user =
                userRepository.findAll()
                        .stream()
                        .filter(entity ->
                                entity.getUsername().equals("hashedplayer")
                        )
                        .findFirst();

        assertThat(user).isPresent();

        String storedPasswordHash =
                user.orElseThrow().getPasswordHash();

        assertThat(storedPasswordHash)
                .isNotBlank()
                .isNotEqualTo(rawPassword)
                .startsWith("$2");
    }

    @Test
    void shouldRejectDuplicateUsername() throws Exception {

        String request = """
                {
                    "username": "hansi4",
                    "email": "unique-username-test@example.com",
                    "password": "SecurePassword123!",
                    "displayName": "Unique Username Test",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        String request = """
                {
                    "username": "uniqueemailuser",
                    "email": "peter@example.com",
                    "password": "SecurePassword123!",
                    "displayName": "Unique Email Test",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectDuplicateDisplayName() throws Exception {

        String request = """
                {
                    "username": "uniquedisplayuser",
                    "email": "unique-display@example.com",
                    "password": "SecurePassword123!",
                    "displayName": "Marco Rossi",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {

        String request = """
                {
                    "username": "invalidemail",
                    "email": "not-an-email",
                    "password": "SecurePassword123!",
                    "displayName": "Invalid Email",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));
    }

    @Test
    void shouldRejectTooShortPassword() throws Exception {

        String request = """
                {
                    "username": "shortpassword",
                    "email": "shortpassword@example.com",
                    "password": "short",
                    "displayName": "Short Password",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));
    }

    @Test
    void shouldRejectMissingPassword() throws Exception {

        String request = """
                {
                    "username": "nopassword",
                    "email": "nopassword@example.com",
                    "displayName": "No Password",
                    "country": "AT"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));
    }
}