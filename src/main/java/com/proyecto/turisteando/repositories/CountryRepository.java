package com.proyecto.turisteando.repositories;

import com.proyecto.turisteando.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
