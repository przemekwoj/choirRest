package com.przemek.choir.services;

import com.przemek.choir.exceptions.particularExceptions.ChoristerNotFound;
import com.przemek.choir.models.Chorister;
import com.przemek.choir.repositories.ChoristerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChoirService Unit Tests")
public class ChoirServiceTest {

    @InjectMocks
    private ChoirServiceImpl choirService;
    @Mock
    private ChoristerRepository choristerRepository;


    @Test
    @DisplayName("should return all choristers sorted by name")
    void shouldReturnSortedChoristersTest() {
        //given
        List<Chorister> choristers = new ArrayList<>(Arrays.asList(
                new Chorister(1, "Franek", "456765876"),
                new Chorister(2, "Ania", "765948374"),
                new Chorister(3, "Zosia", "980675253")
        ));
        when(choristerRepository.findAll()).thenReturn(choristers);
        //when
        List<Chorister> sortedChoristers = choirService.getAllChoristerSortedByName();
        //then
        assertAll(
                () -> assertEquals(3, sortedChoristers.size()),
                () -> assertEquals("Ania", sortedChoristers.get(0).getName())
        );
    }

    @Test
    @DisplayName("should throw ChoristerNotFound Exception")
    void shouldThrowNotFoundExceptionTest() {
        //given
        Integer id = 999;
        Chorister chorister = new Chorister(id,"Grzegorz","989787656");
        when(choristerRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        //when-then
        assertThrows(ChoristerNotFound.class, () -> choirService.updateChorister(chorister));
    }
}
