package com.proyecto.turisteando.utils;

import com.proyecto.turisteando.entities.*;
import com.proyecto.turisteando.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TouristPlanRepository touristPlanRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CharacteristicRepository characteristicRepository;

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

        List<CityEntity> citiesList = cityRepository.findAll();
        if (citiesList.isEmpty()) {
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
            List<CityEntity> cities = departamentos.stream().map(
                            departamento -> CityEntity.builder()
                                    .name(departamento)
                                    .country(country)
                                    .build())
                    .toList();
            cityRepository.saveAll(cities);
        }

        // Cargar categorías (si no existen)
        List<CategoryEntity> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            // Guardar imágenes como entidades
            List<ImageEntity> images = Arrays.asList(
                    ImageEntity.builder().imageUrl("https://example.com/images/tours.jpg").build(),
                    ImageEntity.builder().imageUrl("https://example.com/images/activities.jpg").build()
            );
//            List<ImageEntity> savedImages = imageRepository.saveAll(images);

            // Crear categorías con las imágenes asociadas
            categoryRepository.saveAll(Arrays.asList(
                    CategoryEntity.builder()
                            .name("Tours")
                            .status((byte) 1)
                            .description("Recorridos turísticos...")
                            .image(images.get(0)) // Asociar la primera imagen
                            .build(),
                    CategoryEntity.builder()
                            .name("Actividades")
                            .status((byte) 1)
                            .description("Actividades al aire libre...")
                            .image(images.get(1)) // Asociar la segunda imagen
                            .build()
            ));
        }

        List<CharacteristicEntity> characteristicsListEntities = characteristicRepository.findAll();
        if (characteristicsListEntities.isEmpty()) {
            List<String> characteristics = Arrays.asList(
                    "Caminata",
                    "Tren",
                    "Hotel",
                    "Comida incluida",
                    "Wifi incluido",
                    "Piscina",
                    "Parque",
                    "Accesibilidad",
                    "Pets Friendly",
                    "Niños"
            );

            // Crear y guardar las entidades de imagen primero
            List<ImageEntity> images = characteristics.stream().map(characteristic -> {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImageUrl("https://example.com/images/" + characteristic.toLowerCase().replace(" ", "-") + ".jpg");
                return imageEntity;
            }).toList();

            // Asociar cada característica con su imagen correspondiente
            List<CharacteristicEntity> newCharacteristicsList = characteristics.stream()
                    .map(characteristic -> {
                        // Obtener la imagen correspondiente para esta característica
                        ImageEntity associatedImage = images.get(characteristics.indexOf(characteristic));

                        // Crear la característica y asociarla con la imagen
                        return CharacteristicEntity.builder()
                                .name(characteristic)
                                .status((byte) 1)
                                .icon(associatedImage)
                                .build();
                    })
                    .toList();

            // Guardar todas las entidades de características con las imágenes asociadas
            characteristicRepository.saveAll(newCharacteristicsList);
        }

        List<CharacteristicEntity> characteristicsList = characteristicRepository.findAll();
        List<CityEntity> cities = cityRepository.findAll();

        List<TouristPlanEntity> plans = touristPlanRepository.findAll();
        if (plans.isEmpty()) {
            CategoryEntity toursCategory = categoryRepository.findByName("Tours");
            CategoryEntity activitiesCategory = categoryRepository.findByName("Actividades");

            // Crear planes turísticos
            TouristPlanEntity tour1 = TouristPlanEntity.builder()
                    .title("Tour guiado a Machu Picchu")
                    .description("Explora la antigua ciudadela inca de Machu Picchu, una de las nuevas Siete Maravillas del Mundo. Puedes optar por hacer la caminata del Camino Inca o tomar el tren desde Cusco hasta Aguas Calientes, seguido de un tour guiado por el sitio arqueológico.")
                    .price(329.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(100)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("3 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour1);
            ImageEntity image1 = ImageEntity.builder()
                    .imageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Machu_Picchu%2C_Peru_%282018%29.jpg/1280px-Machu_Picchu%2C_Peru_%282018%29.jpg")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image1);
            tour1.setImages(Arrays.asList(image1));
            touristPlanRepository.save(tour1);

            TouristPlanEntity tour2 = TouristPlanEntity.builder()
                    .title("Tour por el Valle Sagrado")
                    .description("Un tour de un día completo para explorar el Valle Sagrado de los Incas, que incluye visitas a Pisac, Ollantaytambo y los vibrantes mercados de Chinchero. Conocerás la cultura inca, verás pueblos tradicionales y disfrutarás de paisajes impresionantes.")
                    .price(19.00)
                    .seller("Valle Sagrado Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(50)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour2);
            ImageEntity image2 = ImageEntity.builder()
                    .imageUrl("https://www.ollantaytambo.org/img/cusco-sacred-valley-machu-picchu-007-01.jpg")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image2);

            tour2.setImages(Arrays.asList(image2));
            touristPlanRepository.save(tour2);

            TouristPlanEntity tour3 = TouristPlanEntity.builder()
                    .title("Tour por la Reserva Nacional de Paracas")
                    .description("Explora la hermosa Reserva Nacional de Paracas, ubicada en la costa del Pacífico. Este tour te permite disfrutar de impresionantes paisajes desérticos, playas aisladas y una rica fauna marina. Puedes avistar flamencos, lobos marinos y aves guaneras. Además, visitarás la famosa Catedral de Paracas, una formación rocosa icónica, y disfrutarás de las vistas del Océano Pacífico desde diversos miradores.")
                    .price(45.00)
                    .seller("Paracas Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(20)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(7),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour3);
            ImageEntity image3 = ImageEntity.builder()
                    .imageUrl("https://www.civitatis.com/f/peru/paracas/big/tour-reserva-nacional-paracas.jpg")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image3);

            tour3.setImages(Arrays.asList(image3));
            touristPlanRepository.save(tour3);

            TouristPlanEntity tour4 = TouristPlanEntity.builder()
                    .title("Tour aéreo sobre las Líneas de Nazca")
                    .description("Sobrevuela las misteriosas Líneas de Nazca, antiguos geoglifos gigantes trazados en el desierto. El tour suele salir desde Nazca o Ica, y podrás observar las famosas figuras como el mono, la araña y el colibrí.")
                    .price(99.00)
                    .seller("Nazca Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour4);
            ImageEntity image4 = ImageEntity.builder()
                    .imageUrl("https://sp-ao.shortpixel.ai/client/to_webp,q_lossless,ret_img,w_720,h_606/https://www.condorxtreme.com/wp-content/uploads/2023/08/optimized_LINEAS_DE_NASCA_6-720x606.jpg")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image4);
            tour4.setImages(Arrays.asList(image4));
            touristPlanRepository.save(tour4);

            TouristPlanEntity tour5 = TouristPlanEntity.builder()
                    .title("Tour a las Islas Flotantes del Lago Titicaca")
                    .description("Visita las islas flotantes de los Uros en el Lago Titicaca, donde podrás conocer a las comunidades locales que viven en islas hechas de totora, una planta acuática. Además, puedes hacer una visita a la Isla Taquile.")
                    .price(30.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(20))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(15)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour5);
            ImageEntity image5 = ImageEntity.builder()
                    .imageUrl("https://blog.viajesmachupicchu.travel/wp-content/uploads/2023/05/islas-de-los-uros-en-el-lago-titicaca-0.jpg")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image5);
            tour5.setImages(Arrays.asList(image5));
            touristPlanRepository.save(tour5);

            TouristPlanEntity tour6 = TouristPlanEntity.builder()
                    .title("Tour en el Cañón del Colca")
                    .description("Un tour de dos días al Cañón del Colca, uno de los cañones más profundos del mundo. Además de disfrutar de paisajes espectaculares, puedes avistar el majestuoso cóndor andino. El tour suele partir desde Arequipa.")
                    .price(80.00)
                    .seller("Colca Tours")
                    .city(cities.get(3))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(15)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour6);
            ImageEntity image6 = ImageEntity.builder()
                    .imageUrl("https://www.peru.travel/contenido/experiencia/imagen/es/67/1.1/visita%20al%20canon%20del%20colca.jpg")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image6);
            tour6.setImages(Arrays.asList(image6));
            touristPlanRepository.save(tour6);

            TouristPlanEntity activity1 = TouristPlanEntity.builder()
                    .title("Trekking en la Montaña de 7 Colores")
                    .description("Realiza una caminata hacia la famosa Montaña de los Siete Colores, cerca de Cusco. Este destino es conocido por sus impresionantes colores naturales debido a los minerales presentes en la tierra.")
                    .price(19.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Actividades"))
                    .capacity(30)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity1);
            ImageEntity image7 = ImageEntity.builder()
                    .imageUrl("https://www.peru.travel/contenido/general/imagen/es/302/1.1/vinicunca.jpg")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image7);
            activity1.setImages(Arrays.asList(image7));
            touristPlanRepository.save(activity1);

            TouristPlanEntity activity2 = TouristPlanEntity.builder()
                    .title("Exploración de la Selva Amazónica")
                    .description("Vive la experiencia de explorar la selva amazónica peruana desde Iquitos o Puerto Maldonado. Puedes realizar caminatas por la selva, avistamiento de fauna y paseos en bote por ríos llenos de vida.")
                    .price(250.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(15))
                    .category(categoryRepository.findByName("Actividades"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("2 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity2);
            ImageEntity image8 = ImageEntity.builder()
                    .imageUrl("https://tribusviajeras.com/wp-content/uploads/2023/04/Tambopata-amazonas.jpg")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image8);
            activity2.setImages(Arrays.asList(image8));
            touristPlanRepository.save(activity2);

            TouristPlanEntity activity3 = TouristPlanEntity.builder()
                    .title("Sandboarding en Huacachina")
                    .description("Deslízate por las dunas de arena de Huacachina, cerca de Ica. El sandboarding es una actividad emocionante, y también puedes hacer recorridos en buggies por el desierto.")
                    .price(83.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Actividades"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity3);
            ImageEntity image9 = ImageEntity.builder()
                    .imageUrl("https://andinoperu.b-cdn.net/wp-content/uploads/2024/02/huacachina-3.webp")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image9);
            activity3.setImages(Arrays.asList(image9));
            touristPlanRepository.save(activity3);

            TouristPlanEntity activity4 = TouristPlanEntity.builder()
                    .title("Visita al Mercado de San Pedro en Cusco")
                    .description("Sumérgete en la cultura local visitando el Mercado de San Pedro, donde podrás comprar artesanías, probar comida tradicional peruana y conocer más sobre los ingredientes locales.")
                    .price(10.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Actividades"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity4);
            ImageEntity image10 = ImageEntity.builder()
                    .imageUrl("https://www.wayraqperu.com/wp-content/uploads/2020/04/MSPCG.jpg")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image10);
            activity4.setImages(Arrays.asList(image10));
            touristPlanRepository.save(activity4);

            TouristPlanEntity activity5 = TouristPlanEntity.builder()
                    .title("Visita a las Salineras de Maras")
                    .description("Visita las impresionantes terrazas de sal en Maras, un sitio único donde se extrae sal de manera tradicional desde la época inca. Es una actividad cercana al Valle Sagrado, ideal para combinar con otros tours.")
                    .price(35.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Actividades"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity5);
            ImageEntity image11 = ImageEntity.builder()
                    .imageUrl("https://www.lorenzoexpeditions.com/wp-content/uploads/2023/12/Salinera-de-Maras-Moray.jpg")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image11);
            activity5.setImages(Arrays.asList(image11));
            touristPlanRepository.save(activity5);
        }
    }
}
