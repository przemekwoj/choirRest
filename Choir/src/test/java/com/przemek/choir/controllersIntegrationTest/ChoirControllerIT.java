package com.przemek.choir.controllersIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.przemek.choir.models.Chorister;
import com.przemek.choir.repositories.ChoristerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sqlScripts/restartSequence.sql")
public class ChoirControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChoristerRepository choristerRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void initializeDatabase() {
        dropAll();
        addChoristerToDatabase();
    }

    private void addChoristerToDatabase() {
        List<Chorister> choristers = new ArrayList<>(Arrays.asList(
                new Chorister(1, "Franek", "456765876"),
                new Chorister(2, "Ania", "765948374"),
                new Chorister(3, "Zosia", "980675253")
        ));
        choristerRepository.saveAll(choristers);
    }

    private void dropAll() {
        choristerRepository.deleteAll();
    }

    @Test
    @DisplayName("should return choristers sorted by name")
    public void shouldReturnSortedByNameChoristersIT() throws Exception {
        int amountOdChoristers = choristerRepository.findAll().size();
        //given-when
        MvcResult result = mockMvc.perform(get("/choir/choristers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        Chorister[] sortedChoristers = mapper.readValue(result.getResponse().getContentAsString(), Chorister[].class);
        assertAll(
                () -> assertEquals(amountOdChoristers, sortedChoristers.length),
                () -> assertEquals("Ania", sortedChoristers[0].getName())
        );
    }

    @Test
    @DisplayName("should add chorister")
    public void shouldAddChoristerIT() throws Exception {
        //given
        Chorister newChorister = new Chorister();
        newChorister.setName("Przemek");
        newChorister.setPhoneNumber("878676565");
        int sizeBeforeAddChorister = choristerRepository.findAll().size();
        //when
        MvcResult result = mockMvc.perform(post("/choir/chorister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newChorister))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        Integer newId = mapper.readValue(result.getResponse().getContentAsString(), Integer.class);
        assertAll(
                () -> assertEquals(4, newId),
                () -> assertEquals(sizeBeforeAddChorister + 1, choristerRepository.findAll().size())
        );
    }

    @Test
    @DisplayName("should throw exception while try to add chorister with invalid phone number")
    public void shouldThrowExceptionWhileTryToAddChoristerWithInvalidPhoneNumberIT() throws Exception {
        //given
        Chorister newChorister = new Chorister();
        newChorister.setName("Przemek");
        newChorister.setPhoneNumber("87867656588");
        //when
        MvcResult result = mockMvc.perform(post("/choir/chorister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newChorister))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        //then
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
    }

    @Test
    @DisplayName("should delete user by id")
    public void shouldDeleteUserByIdIT() throws Exception {
        //given
        Integer id = 1;
        int sizeBeforeDelete = choristerRepository.findAll().size();
        Chorister deleteChorister = choristerRepository.findById(id).get();
        //when
        MvcResult result = mockMvc.perform(delete("/choir/chorister/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertAll(
                () -> assertEquals(sizeBeforeDelete - 1, choristerRepository.findAll().size()),
                () -> assertFalse(choristerRepository.findAll().contains(deleteChorister))
        );
    }

    @Test
    @DisplayName("should update chorister by id")
    public void shouldUpdateChoristerByIdIT() throws Exception {
        //given
        Chorister updateChorister = new Chorister(1, "Przemek", "657657465");
        int amountOfChoristersBeforeUpdate = choristerRepository.findAll().size();
        //when
        MvcResult result = mockMvc.perform(put("/choir/chorister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateChorister))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertAll(
                () -> assertEquals(amountOfChoristersBeforeUpdate, choristerRepository.findAll().size()),
                () -> assertEquals(updateChorister, choristerRepository.findById(1).get())
        );
    }

    @Test
    @DisplayName("should throw exception while try to update chorister with invalid phone number")
    public void shouldThrowExceptionWhileTryToUpdateChoristerWithInvalidPhoneNumberIT() throws Exception {
        //given
        Chorister updateChorister = new Chorister(1, "Przemek", "123");

        //when
        MvcResult result = mockMvc.perform(put("/choir/chorister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateChorister))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        //then
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
    }

    @Test
    @DisplayName("should throw exception while we try to update choister witout id")
    public void shouldThrowExceptionWhileTryToUpdateIllegalChoristerIT() throws Exception{
        //given
        Chorister updateChorister = new Chorister("Przemek","878787765");
        //when
        MvcResult result = mockMvc.perform(put("/choir/chorister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateChorister))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        //then
        assertTrue(result.getResolvedException() instanceof InvalidDataAccessApiUsageException);
    }
}
