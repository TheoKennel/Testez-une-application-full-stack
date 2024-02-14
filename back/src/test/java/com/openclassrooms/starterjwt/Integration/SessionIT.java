package com.openclassrooms.starterjwt.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @WithMockUser
    public void findById_WithGoodId_ShouldReturnResponseOk() throws Exception {
        Long idToFind = 104L;
        mockMvc.perform(get("/api/session/{id}", idToFind)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idToFind))
                .andExpect(jsonPath("$.name").value("Yogasession"));
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
                .andExpect(jsonPath("[0].id").value("104"))
                .andExpect(jsonPath("[0].name").value("Yogasession"));
    }

    @Test
    @WithMockUser
    public void create_WithSessionDto_ShouldReturnOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("YogaSessionBackTest");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(0L);
        sessionDto.setDescription("Description");
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoToJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("YogaSessionBackTest"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    @WithMockUser
    public void update_WithIdAndSessionDto_ShouldReturnOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("OpenClassroomsUpdate");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(0L);
        sessionDto.setDescription("DescriptionUpdate");
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoToJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/111")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionDtoToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));
    }

    @Test
    @WithMockUser
    public void update_WithBadId_ShouldReturnBadRequest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("OpenClassroomsUpdate");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(0L);
        sessionDto.setDescription("Description");
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoToJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoToJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    public void participate_WithGoodUserId_ShouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/session/115/participate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    public void participate_WithNonNumericUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void noLongerParticipate_WithGoodId_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/session/115/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void noLongerParticipate_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/a/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void noLongerParticipate_WithNonNumericUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser
    public void save_WithGoodId_ShouldReturnOk() throws Exception{
        mockMvc.perform(delete("/api/session/112")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void save_WithSessionNull_ShouldReturnNotfound() throws Exception{
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void save_WithNonNumericId_ShouldReturnBadRequest() throws Exception{
        mockMvc.perform(delete("/api/session/a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void requestWithInvalidJwtToken_ShouldBeUnauthorized() throws Exception {
        String invalidJwtToken = "invalidToken";
        mockMvc.perform(get("/api/session")
                        .header("Authorization", invalidJwtToken))
                .andExpect(status().isUnauthorized());
    }
}

