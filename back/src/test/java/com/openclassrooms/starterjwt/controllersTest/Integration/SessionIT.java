package com.openclassrooms.starterjwt.controllersTest.Integration;


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
public class SessionIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void shouldFindSessionById() throws Exception {
        Long idToFind = 1L;
        this.mockMvc.perform(get("/api/session/{id}", idToFind)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idToFind))
                .andExpect(jsonPath("$.name").value("session 1"));
    }

}
