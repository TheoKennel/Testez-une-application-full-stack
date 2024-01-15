package com.openclassrooms.starterjwt.controllersTest.Integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void findById_WithGoodId_ShouldReturnResponseOk() throws Exception {
        Long idToFind = 1L;
        mockMvc.perform(get("/api/session/{id}", idToFind)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idToFind))
                .andExpect(jsonPath("$.name").value("session 1"));
    }

    @Test
    @WithMockUser
    public void findById_WithBadId_ShouldReturnBadRequest() throws Exception {
        String id = "a";
        mockMvc.perform(get("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void findById_WithSessionNull_ShouldReturnNotFound() throws Exception {
        Long id = 1115L;
        mockMvc.perform(get("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findAll_ShouldReturnResponseOk() throws Exception {
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value("idAttenduAset"))
                .andExpect(jsonPath("[0].name").value("nomAttenduASet"));
    }

    @Test
    @WithMockUser
    public void create_WithIdAndSessionDto_ShouldReturnOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Theo");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description");

        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoToJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionDtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDto.getId()))
                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                .andExpect(jsonPath("$.teacher_id").value(sessionDto.getTeacher_id()))
                .andExpect(jsonPath("$.date").value(sessionDto.getDate()))
                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));

    }
}
