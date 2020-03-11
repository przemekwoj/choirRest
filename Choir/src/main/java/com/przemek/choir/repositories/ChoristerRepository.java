package com.przemek.choir.repositories;

import com.przemek.choir.models.Chorister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoristerRepository extends JpaRepository<Chorister, Integer> {
}
