package com.proyecto.turisteando.utils;

import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.repositories.CityRepository;
import com.proyecto.turisteando.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for loading initial data into the database.
 * It implements the CommandLineRunner interface, which allows it to run code
 * after the application context has been loaded.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void run(String... args) throws Exception {

        List<CountryEntity> countries = countryRepository.findAll();
        CountryEntity country;

        if (countries.isEmpty()) {
            // Cargar datos de paises
            CountryEntity newCountry = CountryEntity.builder()
                    .name("Perú")
                    .build();

            country = countryRepository.save(newCountry);
        } else {
            country = null;
        }

        List<CityEntity> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            // Cargar datos de ciudades
            List<String> departamentos = Arrays.asList(
                    "Amazonas",
                    "Áncash",
                    "Apurímac",
                    "Arequipa",
                    "Ayacucho",
                    "Cajamarca",
                    "Callao",
                    "Cusco",
                    "Huancavelica",
                    "Huánuco",
                    "Ica",
                    "Junín",
                    "La Libertad",
                    "Lambayeque",
                    "Lima",
                    "Loreto",
                    "Madre de Dios",
                    "Moquegua",
                    "Pasco",
                    "Piura",
                    "Puno",
                    "San Martín",
                    "Tacna",
                    "Tumbes",
                    "Ucayali"
            );
            departamentos.forEach(departamento -> {
                CityEntity city = CityEntity.builder()
                        .name(departamento)
                        .country(country)
                        .build();
                cityRepository.save(city);
            });
        }
    }
}
