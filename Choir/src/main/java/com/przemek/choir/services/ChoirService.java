package com.przemek.choir.services;

import com.przemek.choir.exceptions.particularExceptions.ChoristerNotFound;
import com.przemek.choir.models.Chorister;

import java.util.List;

public interface ChoirService {

    List<Chorister> getAllChoristerSortedByName();

    Integer addChorister(Chorister chorister);

    Chorister updateChorister(Chorister chorister) throws ChoristerNotFound;

    void deleteChorister(Integer id);
}
