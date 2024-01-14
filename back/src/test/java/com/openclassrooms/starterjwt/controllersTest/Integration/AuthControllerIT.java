package com.openclassrooms.starterjwt.controllersTest.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAuthenticateUser() throws Exception {
        LoginRequest validLogin = new LoginRequest();
        validLogin.setEmail("toto32@toto.com");
        validLogin.setPassword("test!1234");
        String loginRequestJson = new ObjectMapper().writeValueAsString(validLogin);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()); //
    }

    @Test
    public void shouldRejectInvalidLogin() throws Exception {
        LoginRequest invalidLogin = new LoginRequest();
        invalidLogin.setEmail("wronguser@example.com");
        invalidLogin.setPassword("wrongpassword");
        String loginRequestJson = new ObjectMapper().writeValueAsString(invalidLogin);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("newuser6@example.com");
        signUpRequest.setPassword("newPassword");
        signUpRequest.setFirstName("FirstName");
        signUpRequest.setLastName("LastName");

        String signUpRequestJson = new ObjectMapper().writeValueAsString(signUpRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    public void shouldRejectDuplicateEmailRegistration() throws Exception {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("duplicate6@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("FirstName");
        signUpRequest.setLastName("LastName");

        String signUpRequestJson = new ObjectMapper().writeValueAsString(signUpRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }
}
