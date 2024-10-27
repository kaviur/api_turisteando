package com.proyecto.turisteando.utils;

import com.proyecto.turisteando.entities.CityEntity;
import com.proyecto.turisteando.entities.CountryEntity;
import com.proyecto.turisteando.services.ICrudService;
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
    private ICrudService<CountryEntity, Long> countryService;

    @Autowired
    private ICrudService<CityEntity, Long> cityService;

    @Override
    public void run(String... args) throws Exception {

        List<CountryEntity> countries = (List<CountryEntity>) countryService.getAll();
        CountryEntity country;

        if (countries.isEmpty()) {
            // Cargar datos de paises
            CountryEntity country1 = CountryEntity.builder()
                    .name("Perú")
                    .build();
            country = countryService.create(country1);
        } else {
            country = null;
        }

        List<CityEntity> cities = (List<CityEntity>) cityService.getAll();
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
                cityService.create(city);
            });
        }
    }
}
