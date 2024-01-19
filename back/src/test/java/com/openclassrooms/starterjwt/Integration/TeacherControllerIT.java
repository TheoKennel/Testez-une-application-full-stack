package com.openclassrooms.starterjwt.Integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
    @WithMockUser
    public void findById_WithGoodId_ShouldReturnOk() throws Exception {
        String id = "1";
        mockMvc.perform(get("/api/teacher/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser
    public void findById_WithBadId_ShouldReturnNotFound() throws Exception {
        String id = "525";
        mockMvc.perform(get("/api/teacher/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findById_WithNonNumericalId_ShouldReturnBadRequest() throws Exception {
        String id = "a";
        mockMvc.perform(get("/api/teacher/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void findAll_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
