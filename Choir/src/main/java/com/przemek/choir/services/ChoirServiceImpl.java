package com.przemek.choir.services;

import com.przemek.choir.exceptions.particularExceptions.ChoristerNotFound;
import com.przemek.choir.models.Chorister;
import com.przemek.choir.repositories.ChoristerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoirServiceImpl implements ChoirService {

    private final ChoristerRepository choristerRepository;

    @Autowired
    public ChoirServiceImpl(ChoristerRepository choristerRepository) {
        this.choristerRepository = choristerRepository;
    }

    @Override
    public List<Chorister> getAllChoristerSortedByName() {
        return choristerRepository.findAll().stream()
                .sorted(Comparator.comparing(Chorister::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Integer addChorister(Chorister chorister) {
        chorister.setId(null);
        return choristerRepository.save(chorister).getId();
    }

    @Override
    public Chorister updateChorister(Chorister chorister) throws ChoristerNotFound {
        checkIfChoristerExist(chorister);
        return choristerRepository.save(chorister);
    }

    private void checkIfChoristerExist(Chorister chorister) throws ChoristerNotFound {
        choristerRepository.findById(chorister.getId()).orElseThrow(ChoristerNotFound::new);
    }

    @Override
    public void deleteChorister(Integer id) {
        choristerRepository.deleteById(id);
    }
}
