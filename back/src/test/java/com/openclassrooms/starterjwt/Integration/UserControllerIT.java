package com.openclassrooms.starterjwt.Integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void findById_WithGoodId_ShouldReturnOk() throws Exception{
        String id = "1";

        mockMvc.perform(get("/api/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void findById_WithUserNull_ShouldReturnNotFound() throws Exception{
        String id = "100";

        mockMvc.perform(get("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findById_WithNonNumericalId_ShouldReturnBadRequest() throws Exception{
        String id = "a";

        mockMvc.perform(get("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "newuser6@example.com")
    public void save_WithGoodId_ShouldReturnOk() throws Exception{
        String id = "12";

        mockMvc.perform(delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void save_WithUserNull_ShouldReturnNotFound() throws Exception{
        String id = "100";

        mockMvc.perform(delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void save_WithNonNumericalId_ShouldReturnBadRequest() throws Exception{
        String id = "a";

        mockMvc.perform(delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
