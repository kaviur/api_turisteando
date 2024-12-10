package com.proyecto.turisteando.utils;

import com.proyecto.turisteando.entities.*;
import com.proyecto.turisteando.entities.enums.Role;
import com.proyecto.turisteando.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
                    ImageEntity.builder().imageUrl("http://res.cloudinary.com/dworm9bnx/image/upload/v1733683632/turisteando/file_eiimnx.svg").build(),
                    ImageEntity.builder().imageUrl("http://res.cloudinary.com/dworm9bnx/image/upload/v1733683630/turisteando/file_mfenjy.svg").build()
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
                            .name("Activity")
                            .status((byte) 1)
                            .description("Actividades al aire libre ..")
                            .image(images.get(1)) // Asociar la segunda imagen
                            .build()
            ));
        }

        // Primero, verifica si ya existen características en la base de datos
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

            List<String> imageUrls = Arrays.asList(
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733680824/turisteando/file_dkl1ju.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733682899/turisteando/file_t2ycxa.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733680827/turisteando/file_af1g3c.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733680825/turisteando/file_s6jq8u.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733682901/turisteando/file_aizb67.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733682898/turisteando/file_ubgupr.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733682895/turisteando/file_rkltxp.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733682893/turisteando/file_quk9nf.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733680831/turisteando/file_kyovsa.svg",
                    "http://res.cloudinary.com/dworm9bnx/image/upload/v1733680829/turisteando/file_kys4k0.svg"
            );

            // Crear y guardar las entidades de imagen primero
            List<ImageEntity> images = characteristics.stream().map(characteristic -> {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImageUrl(imageUrls.get(characteristics.indexOf(characteristic)));
                return imageEntity;
            }).toList();

            // Asociar cada característica con su imagen correspondiente
            List<CharacteristicEntity> newCharacteristicsList = characteristics.stream()
                    .map(characteristic -> {
                        // Crear la característica y asociarla con la imagen
                        return CharacteristicEntity.builder()
                                .name(characteristic)
                                .status((byte) 1)
                                .image(images.get(characteristics.indexOf(characteristic))) // Asociar la imagen correspondiente
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


// Reserva Nacional de Paracas
            TouristPlanEntity tour3 = TouristPlanEntity.builder()
                    .title("Tour por la Reserva Nacional de Paracas")
                    .description("Explora la hermosa Reserva Nacional de Paracas, ubicada en la costa del Pacífico. Este tour te permite disfrutar de impresionantes paisajes desérticos, playas aisladas y una rica fauna marina. Puedes avistar flamencos, lobos marinos y aves guaneras. Además, visitarás la famosa Catedral de Paracas, una formación rocosa icónica, y disfrutarás de las vistas del Océano Pacífico desde diversos miradores.")
                    .price(1500.00)
                    .seller("Paracas Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(80)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("4 días")
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


            ImageEntity image1T3 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733630402/1._Tour_por_la_Reserva_Nacional_de_Paracas_hcwnd4.png")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image1T3);


            ImageEntity image2T3 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733630389/2._Tour_por_la_Reserva_Nacional_de_Paracas_jxuacy.png")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image2T3);
            ImageEntity image3T3 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733630377/3._Tour_por_la_Reserva_Nacional_de_Paracas_auq3k1.png")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image3T3);
            ImageEntity image4T3 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733630363/4._Tour_por_la_Reserva_Nacional_de_Paracas_xhmygb.png")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image4T3);
            ImageEntity image5T3 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733630348/5._Tour_por_la_Reserva_Nacional_de_Paracas_fdztgy.png")
                    .touristPlan(tour3)
                    .build();
            imageRepository.save(image5T3);

// Islas Flotantes Listo
            TouristPlanEntity tour5 = TouristPlanEntity.builder()
                    .title("Tour a las Islas Flotantes del Lago Titicaca")
                    .description("Visita las islas flotantes de los Uros en el Lago Titicaca, donde podrás conocer a las comunidades locales que viven en islas hechas de totora, una planta acuática. Además, puedes hacer una visita a la Isla Taquile.")
                    .price(380.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(20))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(150)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 2, 25))
                    .duration("1 semana")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour5);


            ImageEntity image1T5 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733691840/1._Tour_a_las_Islas_Flotantes_del_Lago_Titicaca_ptt0d4.png")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image1T5);


            ImageEntity image2T5 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733692030/4.titicaca_e5digo.png")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image2T5);


            ImageEntity image3T5 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733691853/3_titicaca_xkldlr.png")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image3T5);


            ImageEntity image4T5 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733692015/2._Titicaca_bddtpa.png")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image4T5);
            ImageEntity image5T5 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733692113/5_titicaca_btuokl.png")
                    .touristPlan(tour5)
                    .build();
            imageRepository.save(image5T5);


            //Tour 7
            TouristPlanEntity tour7 = TouristPlanEntity.builder()
                    .title("Tour a montaña de colores Palccoyo")
                    .description("Enrúmbate con nosotros a la montaña de colores Palccoyo, una ruta 100% alternativa en comparación con la montaña Vinicunca, esta experiencia contiene una belleza natural imperdible, no solamente veras una montaña de colores sino varias montañas coloridas, aparte de ello, también podrás ver el bosque de piedras coloridas y tal vez el río rojo. Así que por más que el día esté lluvioso, en este tour siempre saldrás ganando, la buena experiencia está garantizada.")
                    .price(760.00)
                    .seller("Cusco Tours")
                    .city(cities.get(3))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(400)
                    .availabilityStartDate(LocalDate.of(2024, 11, 28))
                    .availabilityEndDate(LocalDate.of(2025, 3, 28))
                    .duration("1 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour7);

            ImageEntity image1Tour7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733691531/pallay-poncho-tour-sharp-rainbow-mountain-cusco-5_kk7qjn.png")
                    .touristPlan(tour7)
                    .build();
            imageRepository.save(image1Tour7);

            ImageEntity image2Tour7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733691551/pallay-poncho-1-dia-tour-desde-cusco_6_ujmnlv.png")
                    .touristPlan(tour7)
                    .build();
            imageRepository.save(image2Tour7);

            ImageEntity image3Tour7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733691515/pallay-poncho-tour-sharp-rainbow-mountain-cusco-3_fbaaqu.png")
                    .touristPlan(tour7)
                    .build();
            imageRepository.save(image3Tour7);


            ImageEntity image4Tour7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733691500/pallay-poncho-tour-sharp-rainbow-mountain-cusco-2_zztxof.png")
                    .touristPlan(tour7)
                    .build();
            imageRepository.save(image4Tour7);


            ImageEntity image5Tour7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733691484/pallay-poncho-1-dia-tour-desde-cusco_1_jhnmya.png")
                    .touristPlan(tour7)
                    .build();
            imageRepository.save(image5Tour7);


            //TOUR 8 Listo
            TouristPlanEntity tour8 = TouristPlanEntity.builder()
                    .title("Recorre el valle Sur del Cusco")
                    .description("Reserva con FWTP nuestro tour al valle sur del Cusco, durante este tour visitarás Tipón, Piquillaqta y Andahuaylillas — en un tour de medio día. Tipón es un sitio arqueológico donde aun se pueden canales de irrigación que denotan lo mejor de la ingeniería hidráulica del imperio de los incas; por el otro lado, Andahuaylillas es un sitio arqueológico que perteneció a la cultura Wari")
                    .price(40.00)
                    .seller("freewalkingtours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(20)
                    .availabilityStartDate(LocalDate.of(2024, 11, 28))
                    .availabilityEndDate(LocalDate.of(2025, 3, 15))
                    .duration("3 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour8);


            ImageEntity image1Tour8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602668/ValleSurCuscoPpal_oz42dy.png")
                    .touristPlan(tour8)
                    .build();
            imageRepository.save(image1Tour8);


            ImageEntity image2Tour8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602689/valle-sur-cusco-tour-3_sufsgv.png")
                    .touristPlan(tour8)
                    .build();
            imageRepository.save(image2Tour8);


            ImageEntity image3Tour8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602700/valle-sur-cusco-tour-8_sufczt.png")
                    .touristPlan(tour8)
                    .build();
            imageRepository.save(image3Tour8);


            ImageEntity image4Tour8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602721/valle-sur-cusco-tour-6_d9aeby.png")
                    .touristPlan(tour8)
                    .build();
            imageRepository.save(image4Tour8);


            ImageEntity image5Tour8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602768/valle-sur-cusco-tour-5_xzfxqn.png")
                    .touristPlan(tour8)
                    .build();
            imageRepository.save(image5Tour8);

            //TOUR 9 Colca Puno LISTO
            TouristPlanEntity tour9 = TouristPlanEntity.builder()
                    .title("Tour Colca Puno")
                    .description("Disfruta esta experiencia preferida por los turistas, donde aprovecharás al máximo tu tiempo conociendo lo más resaltante del Valle del Colca, sus pueblos tradicionales y los baños termales de Chacapi. Conocerás este pueblo cerca del Valle y finalizarás el recorrido en la ciudad de Puno después de 2 días, haciendo breves paradas para apreciar los paisajes.")
                    .price(236.00)
                    .seller("Arequipa Explorer")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(200)
                    .availabilityStartDate(LocalDate.of(2024, 11, 25))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("3 días")
                    .characteristic(List.of(
                            characteristicsList.get(9),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour9);
            ImageEntity image1Tour9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733603049/canon-del-colca5_aytizv.png")
                    .touristPlan(tour9)
                    .build();
            imageRepository.save(image1Tour9);

            ImageEntity image2Tour9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733603059/canon-del-colca_eur68v.png")
                    .touristPlan(tour9)
                    .build();
            imageRepository.save(image2Tour9);

            ImageEntity image3Tour9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733603074/canon-del-colca6_nhozim.png")
                    .touristPlan(tour9)
                    .build();
            imageRepository.save(image3Tour9);

            ImageEntity image4Tour9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733603087/canon-del-colca4_lyvvwn.png")
                    .touristPlan(tour9)
                    .build();
            imageRepository.save(image4Tour9);


            ImageEntity image5Tour9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733603096/canon-del-colca3_wx7pza.png")
                    .touristPlan(tour9)
                    .build();
            imageRepository.save(image5Tour9);

            //TOUR 10 Sur del Cusco Listo
            TouristPlanEntity tour10 = TouristPlanEntity.builder()
                    .title("Tour al valle Sur del Cusco")
                    .description("Visita a Las Huacas del Sol y la Luna, grandes construcciones Moche con impresionantes murales de divinidades. Parada para almorzar en Trujillo. Continuamos hacia Chanchan (Palacio Nikan) y la playa Huanchaco. Luego, visitaremos El Brujo, compuesto por Huaca Cortada, Huaca Prieta y la Huaca Cao Viejo. En el Museo Cao se exhibe la Señora de Cao, la única mujer gobernante del Perú antiguo.")
                    .price(450.00)
                    .seller("Viajando tours")
                    .city(cities.get(12))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(250)
                    .availabilityStartDate(LocalDate.of(2024, 11, 25))
                    .availabilityEndDate(LocalDate.of(2025, 4, 10))
                    .duration("5 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour10);
            ImageEntity image1Tour10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602668/ValleSurCuscoPpal_oz42dy.png")
                    .touristPlan(tour10)
                    .build();
            imageRepository.save(image1Tour10);

            ImageEntity image2Tour10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602689/valle-sur-cusco-tour-3_sufsgv.png")
                    .touristPlan(tour10)
                    .build();
            imageRepository.save(image2Tour10);

            ImageEntity image3Tour10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602700/valle-sur-cusco-tour-8_sufczt.png")
                    .touristPlan(tour10)
                    .build();
            imageRepository.save(image3Tour10);

            ImageEntity image4Tour10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602721/valle-sur-cusco-tour-6_d9aeby.png")
                    .touristPlan(tour10)
                    .build();
            imageRepository.save(image4Tour10);

            ImageEntity image5Tour10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733602768/valle-sur-cusco-tour-5_xzfxqn.png")
                    .touristPlan(tour10)
                    .build();
            imageRepository.save(image5Tour10);


            //TOUR 11 Huascarán LISTO
            TouristPlanEntity tour11 = TouristPlanEntity.builder()
                    .title("Tour- Expedición al Huascarán")
                    .description("¡Conquista el Techo del Perú! Adéntrate en el majestuoso Nevado Huascarán, también llamado “Mataraju”, con sus impresionantes picos gemelos. Vive la emoción de ascender a la cima más alta del país (6,877 m) en el Parque Nacional Huascarán. Supera desafíos únicos como grietas y seracs, siempre guiado por expertos. ¡Haz realidad esta inolvidable experiencia llena de adrenalina y naturaleza!.")
                    .price(2000.00)
                    .seller(" Huascaran tours")
                    .city(cities.get(1))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(20)
                    .availabilityStartDate(LocalDate.of(2024, 11, 20))
                    .availabilityEndDate(LocalDate.of(2025, 1, 27))
                    .duration("7 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(6),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour11);
            ImageEntity image1Tour11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601645/portada-expedicion-al-nevado-huascaran-imagen2-del-tour_y91apa.png")
                    .touristPlan(tour11)
                    .build();
            imageRepository.save(image1Tour11);

            ImageEntity image2Tour11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601655/expedicion-al-nevado-huascaran-galeria-del-tour3_jj699d.png")
                    .touristPlan(tour11)
                    .build();
            imageRepository.save(image2Tour11);

            ImageEntity image3Tour11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601876/expedicion-al-nevado-huascaran-galeria-del-tour4_hbdq0m.png")
                    .touristPlan(tour11)
                    .build();
            imageRepository.save(image3Tour11);

            ImageEntity image4Tour11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601679/expedicion-al-nevado-huascaran-galeria-del-tour7_uonadt.png")
                    .touristPlan(tour11)
                    .build();
            imageRepository.save(image4Tour11);

            ImageEntity image5Tour11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601679/expedicion-al-nevado-huascaran-galeria-del-tour7_uonadt.png")
                    .touristPlan(tour11)
                    .build();
            imageRepository.save(image5Tour11);


//Actividad 6 Montaña Siete Colores Listo
            TouristPlanEntity activity1 = TouristPlanEntity.builder()
                    .title("Trekking en la Montaña de 7 Colores")
                    .description("Realiza una caminata hacia la famosa Montaña de los Siete Colores, cerca de Cusco. Este destino es conocido por sus impresionantes colores naturales debido a los minerales presentes en la tierra.")
                    .price(19.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(120)
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

            ImageEntity image1activity1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555871/montana-7-colores1-1000x650_bjlgjs.pngg")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image1activity1);

            ImageEntity image2activity1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555706/montana-llamas-vinicunca1-1000x650_bjlrty.png")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image2activity1);

            ImageEntity image3activity1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555716/montana-colores-caballos1-1000x650_f4nbpi.png")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image3activity1);

            ImageEntity image4activity1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555763/chicas-cartel-montana-7-colores2-1000x650_r68cq2.png")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image4activity1);

            ImageEntity image5activity1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733616889/MOUNTAIN5_knshod.png")
                    .touristPlan(activity1)
                    .build();
            imageRepository.save(image5activity1);


            //Actividad 2 Listo
            TouristPlanEntity activity2 = TouristPlanEntity.builder()
                    .title("Exploración de la Selva Amazónica")
                    .description("Vive la experiencia de explorar la selva amazónica peruana desde Iquitos o Puerto Maldonado. Puedes realizar caminatas por la selva, avistamiento de fauna y paseos en bote por ríos llenos de vida.")
                    .price(275.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(15))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(300)
                    .availabilityStartDate(LocalDate.of(2025, 1, 1))
                    .availabilityEndDate(LocalDate.of(2025, 3, 30))
                    .duration("6 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity2);
            ImageEntity image1activity2 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554509/SELVA-AMAZONICA2_acsrvk.png")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image1activity2);

            ImageEntity image2activity2 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554520/SELVA-AMAZONICA_mebrcv.png")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image2activity2);

            ImageEntity image3activity2 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554496/SELVA-AMAZONICA3_ll3tnw.png")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image3activity2);

            ImageEntity image4activity2 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554466/selva-amazonicaPpal_eru4un.png")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image4activity2);

            ImageEntity image5activity2 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733618977/SELVA-AMAZONICA5_yyp1y8.png")
                    .touristPlan(activity2)
                    .build();
            imageRepository.save(image5activity2);


            //Actividad 3
            TouristPlanEntity activity3 = TouristPlanEntity.builder()
                    .title("Sandboarding en Huacachina")
                    .description("Deslízate por las dunas de arena de Huacachina, cerca de Ica. El sandboarding es una actividad emocionante, y también puedes hacer recorridos en buggies por el desierto.")
                    .price(1000.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 12, 20))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("4 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity3);
            ImageEntity image1activity3 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554572/Huacachina4_cnucrh.png")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image1activity3);

            ImageEntity image2activity3 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554624/Huacachina2_m1qnlw.png")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image2activity3);

            ImageEntity image3activity3 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554610/Huacachina3_amcryz.png")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image3activity3);

            ImageEntity image4activity3 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554598/Huacachina5_xsdoz3.png")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image4activity3);

            ImageEntity image5activity3 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554585/HuacachinaPpal_xbsqeq.png")
                    .touristPlan(activity3)
                    .build();
            imageRepository.save(image5activity3);


            //Actividad 7 Listo
            TouristPlanEntity activity7 = TouristPlanEntity.builder()
                    .title("Excursión a laguna Humantay por Cusco")
                    .description("Descubre la Laguna Humantay: un paraíso en los Andes Vive una experiencia única en los alrededores de Cusco. Partiremos temprano en bus hacia Mollepata para disfrutar de un delicioso desayuno. Luego, continuaremos hasta Soraypampa, una pintoresca comunidad indígena, desde donde iniciaremos nuestra caminata hacia la impresionante Laguna Humantay, rodeada de la majestuosa cordillera de Salkantay. ¡Una aventura que no te puedes perder!")
                    .price(1110.00)
                    .seller("Freewalking Tours Perú")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(90)
                    .availabilityStartDate(LocalDate.of(2024, 11, 26))
                    .availabilityEndDate(LocalDate.of(2025, 3, 26))
                    .duration("5 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(8),
                            characteristicsList.get(6)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity7);


            ImageEntity image1activity7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555452/humantay-lake-day-trip-from-cusco-6_ciosv2.png")
                    .touristPlan(activity7)
                    .build();
            imageRepository.save(image1activity7);


            ImageEntity image2activity7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555339/humantay-lake-day-trip-from-cusco-2_svok2o.png")
                    .touristPlan(activity7)
                    .build();
            imageRepository.save(image2activity7);


            ImageEntity image3activity7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555330/tour-laguna-humantay-desde-cusco_3_f28du4.png")
                    .touristPlan(activity7)
                    .build();
            imageRepository.save(image3activity7);


            ImageEntity image4activity7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555318/tour-laguna-humantay-desde-cusco_4_gkkue0.png")
                    .touristPlan(activity7)
                    .build();
            imageRepository.save(image4activity7);


            ImageEntity image5activity7 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733555306/tour-laguna-humantay-desde-cusco1_wfp7mz.png")
                    .touristPlan(activity7)
                    .build();
            imageRepository.save(image5activity7);


            //Activity 8 listo
            TouristPlanEntity activity8 = TouristPlanEntity.builder()
                    .title("City tour Cusco + 4 ruinas de Sacsayhuamán")
                    .description("Descubre Cusco con el City Tour Original. Sumérgete en la historia y belleza de Cusco en nuestro city tour de 5 horas. A pie, exploraremos la imponente Catedral del Cusco y el Templo de Coricancha. Luego, tomaremos un bus hacia las ruinas de Sacsayhuamán, donde descubrirás los secretos de la arquitectura Inca. Una experiencia única para conocer los puntos más emblemáticos de la antigua capital del Imperio Inca. ¡No te lo puedes perder!")
                    .price(530.00)
                    .seller("Cusco Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(500)
                    .availabilityStartDate(LocalDate.of(2024, 11, 29))
                    .availabilityEndDate(LocalDate.of(2025, 5, 29))
                    .duration("8 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(4),
                            characteristicsList.get(3),
                            characteristicsList.get(6),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity8);


            ImageEntity image1activity8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733590591/city-tour-cusco-with-sacsayhuaman-4-ruins-6_okqjjg.png")
                    .touristPlan(activity8)
                    .build();
            imageRepository.save(image1activity8);


            ImageEntity image2activity8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733590603/city-tour-cusco-with-sacsayhuaman-4-ruins-4-5_zwpcw0.png")
                    .touristPlan(activity8)
                    .build();
            imageRepository.save(image2activity8);


            ImageEntity image3activity8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733590615/city-tour-cusco-with-sacsayhuaman-4-ruins-4_vwcaqw.png")
                    .touristPlan(activity8)
                    .build();
            imageRepository.save(image3activity8);


            ImageEntity image4activity8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733590567/city-tour-cusco-with-sacsayhuaman-4-ruins-fwtp_1_gxp93e.png")
                    .touristPlan(activity8)
                    .build();
            imageRepository.save(image4activity8);


            ImageEntity image5activity8 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733590579/city-tour-cusco-with-sacsayhuaman-4-ruins-fwtp_atpwtk.pngg")
                    .touristPlan(activity8)
                    .build();
            imageRepository.save(image5activity8);

            //Actividad 9 Listo
            TouristPlanEntity activity9 = TouristPlanEntity.builder()
                    .title("Excursión a Waqrapukara por Santa Lucia")
                    .description("Explora Waqrapukara: La Fortaleza Andina. Ubicada en el distrito de Acos, provincia de Acomayo, Waqrapukara es una fortaleza en forma de cuerno que ofrece vistas impresionantes de las montañas y formaciones rocosas. Durante este tour, recorrerás un camino rodeado de la rica flora y fauna andina. Este sitio, habitado por la cultura Canchis y luego conquistado por los Incas, es un destino único lleno de historia y belleza natural. ¡Una aventura que no puedes perderte!")
                    .price(600.00)
                    .seller("Macchu Pichu Viajes Peru")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(30)
                    .availabilityStartDate(LocalDate.of(2024, 11, 28))
                    .availabilityEndDate(LocalDate.of(2025, 1, 10))
                    .duration("5 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(6),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity9);

            ImageEntity image1activity9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733600576/excursion-a-waqrapukara-ruta-por-santa-lucia_1_mgp0ky.png")
                    .touristPlan(activity9)
                    .build();
            imageRepository.save(image1activity9);

            ImageEntity image2activity9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733600561/excursion-a-waqrapukara-ruta-por-santa-lucia_3_dcfnvt.png")
                    .touristPlan(activity9)
                    .build();
            imageRepository.save(image2activity9);

            ImageEntity image3activity9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733600544/excursion-a-waqrapukara-ruta-por-santa-lucia_4_paahkp.png")
                    .touristPlan(activity9)
                    .build();
            imageRepository.save(image3activity9);

            ImageEntity image4activity9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733600534/excursion-a-waqrapukara-ruta-por-santa-lucia_2_lfs4he.png")
                    .touristPlan(activity9)
                    .build();
            imageRepository.save(image4activity9);

            ImageEntity image5activity9 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733600518/excursion-a-waqrapukara-ruta-por-santa-lucia_5_chw2ul.png")
                    .touristPlan(activity9)
                    .build();
            imageRepository.save(image5activity9);


//Actividad 10

            TouristPlanEntity activity10 = TouristPlanEntity.builder()
                    .title("Aventura en el Planetario del Cusco")
                    .description("Vive la magia del Planetario de Cusco. Ubicado en el Parque Arqueológico de Sacsayhuamán y la reserva ecológica de Llaullipata, el Planetario de Cusco ofrece una experiencia única de astronomía. A solo 15 minutos del centro histórico, disfrutarás de un entorno natural impresionante. Explora la astronomía inca, leyendas de los cielos del Sur, y observa las estrellas, nebulosas y planetas con telescopios avanzados. ¡Una experiencia única que conecta historia y ciencia!")
                    .price(135.00)
                    .seller("Macchu Pichu Viajes Peru")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(350)
                    .availabilityStartDate(LocalDate.of(2024, 11, 27))
                    .availabilityEndDate(LocalDate.of(2025, 2, 5))
                    .duration("4 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(6),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity10);

            ImageEntity image1activity10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733553672/planetario_uhm4f2.png")
                    .touristPlan(activity10)
                    .build();
            imageRepository.save(image1activity10);

            ImageEntity image2activity10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733553683/aventura-en-el-planetario-del-cusco_7_umqru2.png")
                    .touristPlan(activity10)
                    .build();
            imageRepository.save(image2activity10);

            ImageEntity image3activity10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733553700/aventura-en-el-planetario-del-cusco_6_b4pedu.png")
                    .touristPlan(activity10)
                    .build();
            imageRepository.save(image3activity10);

            ImageEntity image4activity10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733553714/aventura-en-el-planetario-del-cusco_5_cas9fv.png")
                    .touristPlan(activity10)
                    .build();
            imageRepository.save(image4activity10);

            ImageEntity image5activity10 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733553727/aventura-en-el-planetario-del-cusco_3_tmgdzo.png")
                    .touristPlan(activity10)
                    .build();
            imageRepository.save(image5activity10);


//Ana—------- Tours Lima Antigua y Moderna Listo

            TouristPlanEntity tour12 = TouristPlanEntity.builder()
                    .title("Tour Lima Antigua y Moderna")
                    .description("El tour Lima Antigua y Moderna ofrece un recorrido completo por la capital peruana, combinando su fascinante historia colonial con su vibrante vida moderna. Explora la majestuosidad de su arquitectura histórica en el centro antiguo, Patrimonio de la Humanidad, y descubre los barrios residenciales y modernos de Miraflores y San Isidro, con vistas espectaculares del Océano Pacífico")
                    .price(920.00)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(50)
                    .availabilityStartDate(LocalDate.of(2024, 12, 5))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour12);
            ImageEntity image1T12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733633192/5._Tour_Lima_Antigua_y_Moderna_ifla7i.png")
                    .touristPlan(tour12)
                    .build();
            imageRepository.save(image1T12);
            touristPlanRepository.save(tour12);
            ImageEntity image2T12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733633258/1.Tour_Lima_Antigua_y_Moderna_ekbooh.png")
                    .touristPlan(tour12)
                    .build();
            imageRepository.save(image2T12);
            touristPlanRepository.save(tour12);
            ImageEntity image3T12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733633247/2._Tour_Lima_Antigua_y_Moderna_d7bdib.png")
                    .touristPlan(tour12)
                    .build();
            imageRepository.save(image3T12);
            touristPlanRepository.save(tour12);
            ImageEntity image4T12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733633234/3._Tour_Lima_Antigua_y_Moderna_pnsm0x.png")
                    .touristPlan(tour12)
                    .build();
            imageRepository.save(image4T12);
            touristPlanRepository.save(tour12);
            ImageEntity image5T12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733633222/4._Tour_Lima_Antigua_y_Moderna_en435x.png")
                    .touristPlan(tour12)
                    .build();
            imageRepository.save(image5T12);

// Tour Pachamac y Barranco Listo
            TouristPlanEntity tour13 = TouristPlanEntity.builder()
                    .title("Tour al complejo arqueológico Pachacamac y Barranco")
                    .description("Este tour te lleva al fascinante complejo arqueológico de Pachacamac, un antiguo centro ceremonial preinca ubicado al sur de Lima, donde podrás explorar templos y pirámides sagradas. Luego, continúa hacia el bohemio barrio de Barranco, famoso por su arte, coloridas calles, y vistas al mar, donde experimentarás el espíritu creativo y romántico de Lima.")
                    .price(430.00)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(160)
                    .availabilityStartDate(LocalDate.of(2024, 12, 20))
                    .availabilityEndDate(LocalDate.of(2025, 2, 20))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour13);


            ImageEntity image1T13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733677494/2._Tour_al_complejo_arqueológico_Pachacamac_y_Barranco_ifqssx.png")
                    .touristPlan(tour13)
                    .build();
            imageRepository.save(image1T13);
            ImageEntity image2T13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733677470/3._Tour_al_complejo_arqueológico_Pachacamac_y_Barranco_p7rx8r.png")
                    .touristPlan(tour13)
                    .build();
            imageRepository.save(image2T13);
            ImageEntity image3T13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733677451/4._Tour_al_complejo_arqueológico_Pachacamac_y_Barranco_umkbaf.png")
                    .touristPlan(tour13)
                    .build();
            imageRepository.save(image3T13);
            ImageEntity image4T13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733677436/1._Tour_al_complejo_arqueológico_Pachacamac_y_Barranco_mmfvaa.png")
                    .touristPlan(tour13)
                    .build();
            imageRepository.save(image4T13);
            ImageEntity image5T13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733677405/5._Tour_al_complejo_arqueológico_Pachacamac_y_Barranco_jf2neo.png")
                    .touristPlan(tour13)
                    .build();
            imageRepository.save(image5T13);

            //Tour gastronómico Miraflores
            TouristPlanEntity tour14 = TouristPlanEntity.builder()
                    .title("Tour gastronómico por Miraflores")
                    .description("Descubre los sabores auténticos de Perú en un tour gastronómico por Miraflores. Degusta platos emblemáticos, como el ceviche y la causa limeña, mientras exploras mercados locales, restaurantes y cafeterías que muestran la rica diversidad culinaria peruana. Este recorrido es una inmersión en la cultura y tradición gastronómica de Lima, ubicada en uno de sus barrios más vibrantes y modernos.")
                    .price(300.20)
                    .seller("XTravel Perú")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(20)
                    .availabilityStartDate(LocalDate.of(2025, 2, 2))
                    .availabilityEndDate(LocalDate.of(2025, 4, 28))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour14);
            ImageEntity image1T14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733632619/4._Tour_gastronómico_por_Miraflores_hqptrf.png")
                    .touristPlan(tour14)
                    .build();
            imageRepository.save(image1T14);
            ImageEntity image2T14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733632656/2._Tour_gastronómico_por_Miraflores_iq34oc.png")
                    .touristPlan(tour14)
                    .build();
            imageRepository.save(image2T14);
            ImageEntity image3T14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733632646/3._Tour_gastronómico_por_Miraflores_u5ugnn.png")
                    .touristPlan(tour14)
                    .build();
            imageRepository.save(image3T14);
            ImageEntity image4T14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733632636/5._Tour_gastronómico_por_Miraflores_ucl48t.png")
                    .touristPlan(tour14)
                    .build();
            imageRepository.save(image4T14);
            ImageEntity image5T14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733632605/1._Tour_gastronómico_por_Miraflores_o5n4f8.png")
                    .touristPlan(tour14)
                    .build();
            imageRepository.save(image5T14);

// Waqrapukara Tour Listo
            TouristPlanEntity tour15 = TouristPlanEntity.builder()
                    .title("Waqrapukara Tour")
                    .description("El tour a Waqrapukara te lleva a una impresionante fortaleza inca enclavada en las montañas de Cusco, conocida por sus formaciones rocosas en forma de cuernos. Este recorrido combina naturaleza y arqueología, ofreciendo vistas panorámicas, caminatas por paisajes andinos y la oportunidad de explorar un sitio sagrado lleno de historia y misticismo, ideal para aventureros y amantes de la cultura inca.")
                    .price(830.20)
                    .seller("XTravel Perú")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(200)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("9 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(6),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour15);
            ImageEntity image1T15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733765581/1._Waqrapukara_Tour_hsozd0.png")
                    .touristPlan(tour15)
                    .build();
            imageRepository.save(image1T15);
            ImageEntity image2T15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733765567/2._Waqrapukara_Tour_uvbd28.png")
                    .touristPlan(tour15)
                    .build();
            imageRepository.save(image2T15);
            ImageEntity image3T15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733765553/3._Waqrapukara_Tour_f8ulin.png")
                    .touristPlan(tour15)
                    .build();
            imageRepository.save(image3T15);
            ImageEntity image4T15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733765541/4._Waqrapukara_Tour_n3iaqv.png")
                    .touristPlan(tour15)
                    .build();
            imageRepository.save(image4T15);
            ImageEntity image5T15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733765530/5._Waqrapukara_Tour_qnzyuy.png")
                    .touristPlan(tour15)
                    .build();
            imageRepository.save(image5T15);

//Cañon de los Perdidos Listo
            TouristPlanEntity tour16 = TouristPlanEntity.builder()
                    .title("Tour Cañón de los Perdidos")
                    .description("El tour al Cañón de los Perdidos te lleva a un enigmático paisaje desértico en Ica, donde podrás explorar formaciones rocosas impresionantes y profundos cañones esculpidos por el tiempo y la naturaleza. Este recorrido te permite descubrir fósiles marinos y vistas panorámicas únicas, brindando una experiencia inolvidable en uno de los secretos mejor guardados del desierto peruano.")
                    .price(250.50)
                    .seller("XTravel Perú")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(30)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 1, 31))
                    .duration("5 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(6),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour16);
            ImageEntity image1T16 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733693812/1._Tour_Cañón_de_los_Perdidos_tk5jz6.png")
                    .touristPlan(tour16)
                    .build();
            imageRepository.save(image1T16);
            ImageEntity image2T16 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695367/2._Tour_Cañón_de_los_Perdidos_eqng97.png")
                    .touristPlan(tour16)
                    .build();
            imageRepository.save(image2T16);
            ImageEntity image3T16 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695353/3._Tour_Cañón_de_los_Perdidos_noy7iz.png")
                    .touristPlan(tour16)
                    .build();
            imageRepository.save(image3T16);
            ImageEntity image4T16 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695339/4._Tour_Cañón_de_los_Perdidos_ar17aq.png")
                    .touristPlan(tour16)
                    .build();
            imageRepository.save(image4T16);
            ImageEntity image5T16 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733693769/5._Tour_Cañón_de_los_Perdidos_rj9jyx.png")
                    .touristPlan(tour16)
                    .build();
            imageRepository.save(image5T16);

//Ana—-------5 Actividades
//Street Limeño Listo

            TouristPlanEntity activity11 = TouristPlanEntity.builder()
                    .title("Descubriendo el Street food limeño, tour privado.")
                    .description("Te sumergirás en los sabores callejeros más auténticos de Lima. Recorre mercados y puestos tradicionales, donde podrás degustar delicias como los anticuchos, picarones y butifarras, mientras conoces las historias y tradiciones detrás de cada plato. Ideal para amantes de la gastronomía que desean explorar Lima como un verdadero local.")
                    .price(490.50)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(4)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("6 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(3),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity11);
            ImageEntity image1A11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629102/1._Descubriendo_el__Street_food__limeño_tour_privado_d6fj6x.png")
                    .touristPlan(activity11)
                    .build();
            imageRepository.save(image1A11);
            ImageEntity image2A11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629069/2._Descubriendo_el__Street_food__limeño_tour_privado_vp4ydg.png")
                    .touristPlan(activity11)
                    .build();
            imageRepository.save(image2A11);
            ImageEntity image3A11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629050/3._Descubriendo_el__Street_food__limeño_tour_privado_ipjp9x.png")
                    .touristPlan(activity11)
                    .build();
            imageRepository.save(image3A11);
            ImageEntity image4A11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629037/4._Descubriendo_el__Street_food__limeño_tour_privado_u9ovjq.png")
                    .touristPlan(activity11)
                    .build();
            imageRepository.save(image4A11);


            ImageEntity image5A11 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629008/5._Descubriendo_el__Street_food__limeño_tour_privado_a4wied.png")
                    .touristPlan(activity11)
                    .build();
            imageRepository.save(image5A11);

//Surf listo
            TouristPlanEntity activity12 = TouristPlanEntity.builder()
                    .title("Clase de surf: Domina la ola perfecta para principiantes y surfistas avanzados")
                    .description("En esta clase de surf, aprenderás a dominar la ola perfecta, ya seas principiante o surfista avanzado. Instructores experimentados te guiarán paso a paso, desde las técnicas básicas de equilibrio y remada hasta movimientos avanzados en olas de mayor desafío. Disfruta de una experiencia segura y personalizada en las mejores playas, ideal para quienes buscan mejorar sus habilidades y vivir la emoción del surf.")
                    .price(162.42)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(4)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 2, 3))
                    .duration("3 días")
                    .characteristic(List.of(
                            characteristicsList.get(6),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity12);
            ImageEntity image1A12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627724/2._Clase_de_surf_qsqwzg.png")
                    .touristPlan(activity12)
                    .build();
            imageRepository.save(image1A12);
            ImageEntity image2A12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627713/3._Clase_de_surf_z3pp5m.png")
                    .touristPlan(activity12)
                    .build();
            imageRepository.save(image2A12);


            ImageEntity image3A12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627701/4._Clase_de_surf_q92c3l.png")
                    .touristPlan(activity12)
                    .build();
            imageRepository.save(image3A12);
            ImageEntity image4A12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627688/1._Clase_de_surf_xfdqdt.png")
                    .touristPlan(activity12)
                    .build();
            imageRepository.save(image4A12);
            ImageEntity image5A12 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627661/5._Clase_de_surf_ouryry.png")
                    .touristPlan(activity12)
                    .build();
            imageRepository.save(image5A12);

// Paramotor en Lima Listo
            TouristPlanEntity activity13 = TouristPlanEntity.builder()
                    .title("Paramotor Sky Activity - Explorando la costa sur de Lima")
                    .description("Vuela en un emocionante tour en paramotor sobre la costa sur de Lima y disfruta de una perspectiva única del océano Pacífico y sus acantilados. Esta experiencia de vuelo libre te permite sentir la libertad de surcar los cielos con seguridad, acompañado por pilotos expertos. Ideal para quienes buscan una aventura inolvidable, el tour ofrece vistas panorámicas y una conexión inigualable con el paisaje costero limeño.")
                    .price(85.00)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(5)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity13);


            ImageEntity image1A13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624637/1._Paramotor_Sky_Activity_bmtdz2.png")
                    .touristPlan(activity13)
                    .build();
            imageRepository.save(image1A13);
            ImageEntity image2A13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624627/2._Paramotor_Sky_Activity_d1litb.png")
                    .touristPlan(activity13)
                    .build();
            imageRepository.save(image2A13);
            ImageEntity image3A13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624614/3._Paramotor_Sky_Activity_uvdwgq.png")
                    .touristPlan(activity13)
                    .build();
            imageRepository.save(image3A13);
            ImageEntity image4A13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624600/5._Paramotor_Sky_Activity_zwzlbt.png")
                    .touristPlan(activity13)
                    .build();
            imageRepository.save(image4A13);
            ImageEntity image5A13 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624587/4._Paramotor_Sky_Activity_h9s1xd.png")
                    .touristPlan(activity13)
                    .build();
            imageRepository.save(image5A13);

//Lobos marinos listo
            TouristPlanEntity activity14 = TouristPlanEntity.builder()
                    .title("Nado con lobos marinos en Islas Palomino")
                    .description("Vive una experiencia inolvidable nadando con lobos marinos en las Islas Palomino, frente a la costa de Lima. Este tour te llevará en un viaje en barco hasta la reserva natural, donde podrás sumergirte en aguas cristalinas y disfrutar de un encuentro cercano y seguro con estos juguetones animales marinos en su hábitat natural. Una aventura única para los amantes de la naturaleza y la vida marina.")
                    .price(190.00)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 3, 1))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(3),
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity14);
            ImageEntity image1A14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733628129/5._Nado_con_lobos_marinos_en_Islas_Palomino_hvhoxs.png")
                    .touristPlan(activity14)
                    .build();
            imageRepository.save(image1A14);
            ImageEntity image2A14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733628176/1._Nado_con_lobos_marinos_en_Islas_Palomino_dxejfi.png")
                    .touristPlan(activity14)
                    .build();
            imageRepository.save(image2A14);
            ImageEntity image3A14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733628166/2._Nado_con_lobos_marinos_en_Islas_Palomino_qqvpni.png")
                    .touristPlan(activity14)
                    .build();
            imageRepository.save(image3A14);
            ImageEntity image4A14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733628156/3._Nado_con_lobos_marinos_en_Islas_Palomino_yzo16s.png")
                    .touristPlan(activity14)
                    .build();
            imageRepository.save(image4A14);
            ImageEntity image5A14 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733628147/4._Nado_con_lobos_marinos_en_Islas_Palomino_lghrwo.png")
                    .touristPlan(activity14)
                    .build();
            imageRepository.save(image5A14);

// Actividad Kayak en Lima listo
            TouristPlanEntity activity15 = TouristPlanEntity.builder()
                    .title("Tour en kayak: Explorando el horizonte de Lima por mar")
                    .description("Embárcate en una emocionante aventura en kayak y explora el horizonte de Lima desde el mar. Navega a lo largo de sus costas, disfrutando de impresionantes vistas panorámicas de los acantilados, playas y el Océano Pacífico. Este tour es perfecto para quienes buscan una experiencia tranquila pero aventurera, en contacto directo con la naturaleza y el paisaje costero limeño.")
                    .price(116.00)
                    .seller("Lima Tours")
                    .city(cities.get(14))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(6)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 1, 16))
                    .duration("1 días")
                    .characteristic(List.of(
                            characteristicsList.get(6),
                            characteristicsList.get(7),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity15);
            ImageEntity image1A15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627106/2._Tour_en_kayak_t1qscc.png")
                    .touristPlan(activity15)
                    .build();
            imageRepository.save(image1A15);
            ImageEntity image2A15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627096/1._Tour_en_kayak_l25rnr.png")
                    .touristPlan(activity15)
                    .build();
            imageRepository.save(image2A15);
            ImageEntity image3A15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627086/3._Tour_en_kayak_ovadw2.png")
                    .touristPlan(activity15)
                    .build();
            imageRepository.save(image3A15);
            ImageEntity image4A15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627075/4._Tour_en_kayak_fuq6a0.png")
                    .touristPlan(activity15)
                    .build();
            imageRepository.save(image4A15);


            ImageEntity image5A15 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733627064/5._Tour_en_kayak_htzsbp.png")
                    .touristPlan(activity15)
                    .build();
            imageRepository.save(image5A15);


// Los primeros Planes Turísticos Tour Machu Picchu listo
            TouristPlanEntity tour1 = TouristPlanEntity.builder()
                    .title("Tour guiado a Machu Picchu")
                    .description("Explora la antigua ciudadela inca de Machu Picchu, una de las nuevas Siete Maravillas del Mundo. Puedes optar por hacer la caminata del Camino Inca o tomar el tren desde Cusco hasta Aguas Calientes, seguido de un tour guiado por el sitio arqueológico.")
                    .price(700.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(100)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 4, 20))
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


            ImageEntity image1T1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733631637/1._Tour_guiado_a_Machu_Picchu_vxub0f.png")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image1T1);


            ImageEntity image2T1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733631601/4._Tour_guiado_a_Machu_Picchu_sfwsy0.png")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image2T1);


            ImageEntity image3T1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733631748/3._Tour_guiado_a_Machu_Picchu_axunej.png")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image3T1);


            ImageEntity image4T1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733631587/2._Tour_guiado_a_Machu_Picchu_zf6omu.png")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image4T1);


            ImageEntity image5T1 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733631099/5._Tour_guiado_a_Machu_Picchu_judaok.png")
                    .touristPlan(tour1)
                    .build();
            imageRepository.save(image5T1);

//Nazca listo
            TouristPlanEntity tour4 = TouristPlanEntity.builder()
                    .title("Tour aéreo sobre las Líneas de Nazca")
                    .description("Sobrevuela las misteriosas Líneas de Nazca, antiguos geoglifos gigantes trazados en el desierto. El tour suele salir desde Nazca o Ica, y podrás observar las famosas figuras como el mono, la araña y el colibrí.")
                    .price(1650.00)
                    .seller("Nazca Tours")
                    .city(cities.get(10))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 29))
                    .availabilityEndDate(LocalDate.of(2025, 1, 5))
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


            ImageEntity image1T4 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629624/4._Tour_aéreo_sobre_las_Líneas_de_Nazca_uvde2e.png")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image1T4);


            ImageEntity image2T4 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629429/1._Tour_aéreo_sobre_las_Líneas_de_Nazca_1_dffufd.png")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image2T4);


            ImageEntity image3T4 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629331/3._Tour_aéreo_sobre_las_Líneas_de_Nazca_udfdbx.png")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image3T4);


            ImageEntity image4T4 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629320/2._Tour_aéreo_sobre_las_Líneas_de_Nazca_jps0io.png")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image4T4);


            ImageEntity image5T4 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733629300/nasca5_xgms6c.png")
                    .touristPlan(tour4)
                    .build();
            imageRepository.save(image5T4);

//Colca listo
            TouristPlanEntity tour6 = TouristPlanEntity.builder()
                    .title("Tour en el Cañón del Colca")
                    .description("Un tour de dos días al Cañón del Colca, uno de los cañones más profundos del mundo. Además de disfrutar de paisajes espectaculares, puedes avistar el majestuoso cóndor andino. El tour suele partir desde Arequipa.")
                    .price(1250.00)
                    .seller("Colca Tours")
                    .city(cities.get(3))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(25)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 2, 2))
                    .duration("3 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour6);


            ImageEntity image1T6 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695552/2._Tour_en_el_Cañón_del_Colca_elrugf.png")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image1T6);


            ImageEntity image2T6 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695536/3._Tour_en_el_Cañón_del_Colca_eebimm.png")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image2T6);


            ImageEntity image3T6 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695519/4._Tour_en_el_Cañón_del_Colca_p9x5wv.png")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image3T6);
            ImageEntity image4T6 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695506/5._Tour_en_el_Cañón_del_Colca_mykeyo.png")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image4T6);


            ImageEntity image5T6 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733695491/1._canon_del_colca_h0dv6v.png")
                    .touristPlan(tour6)
                    .build();
            imageRepository.save(image5T6);

            //Tour Trujillo Huascas

            TouristPlanEntity tour2 = TouristPlanEntity.builder()
                    .title("Tour Trujillo Huascas")
                    .description("Viaja y disfruta del norte de Perú Trujillo y descubre en full day las huacas de moche , chan chan y los caballos peruanos de paso solo en Trujillo Perú.")
                    .price(1800.00)
                    .seller("Trujillo Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Tours"))
                    .capacity(200)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2024, 12, 31))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(tour2);


            ImageEntity image1T2 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601073/Trujillo12_cisrjz.png")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image1T2);


            ImageEntity image2T2 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601244/Trujillo11_jy4w2c.png")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image2T2);


            ImageEntity image3T2 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601060/Trujillo5_gr4tbb.png")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image3T2);


            ImageEntity image4T2 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601050/Trujillo9_coz1b5.png")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image4T2);


            ImageEntity image5T2 = ImageEntity.builder().imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733601039/Trujillo6_ivk0ei.png")
                    .touristPlan(tour2)
                    .build();
            imageRepository.save(image5T2);


//Primeras Actividades listo

            TouristPlanEntity activity6 = TouristPlanEntity.builder()
                    .title("7 lagunas de Ausangate aguas termales")
                    .description("Explora las 7 Lagunas de Ausangate y relájate en sus aguas termales. Si amas la naturaleza y la aventura, este tour es para ti. Recorre un sendero rodeado de imponentes montañas, donde descubrirás lagunas de origen glaciar en tonos azul turquesa y verde esmeralda. Disfruta de vistas panorámicas únicas y culmina con un baño relajante en las aguas termales de Pacchanta. ¡Conecta con el encanto natural en esta experiencia inolvidable!")
                    .price(75.00)
                    .seller("Freewalking Tours Peru")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(15)
                    .availabilityStartDate(LocalDate.of(2024, 12, 2))
                    .availabilityEndDate(LocalDate.of(2025, 1, 25))
                    .duration("2 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(4),
                            characteristicsList.get(7)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity6);

            ImageEntity image1activity6 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554738/tour-ausangate-7-lagunas-aguas-termales_7_r2k0mp.png")
                    .touristPlan(activity6)
                    .build();
            imageRepository.save(image1activity6);

            ImageEntity image2activity6 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554747/tour-ausangate-7-lagunas-aguas-termales_Ppal_jtxrgg.png")
                    .touristPlan(activity6)
                    .build();
            imageRepository.save(image2activity6);

            ImageEntity image3activity6 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554728/tour-ausangate-7-lagunas-aguas-termales_6_werrt4.png")
                    .touristPlan(activity6)
                    .build();
            imageRepository.save(image3activity6);

            ImageEntity image4activity6 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1733554718/tour-ausangate-7-lagunas-aguas-termales_4_xkg9cj.png")
                    .touristPlan(activity6)
                    .build();
            imageRepository.save(image4activity6);

            ImageEntity image5activity6 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/dvjfzzck0/image/upload/v1731633826/tour-ausangate-7-lagunas-aguas-termales_1_geytff.jpg")
                    .touristPlan(activity6)
                    .build();
            imageRepository.save(image5activity6);

            //Actividad 4 listo
            TouristPlanEntity activity4 = TouristPlanEntity.builder()
                    .title("Visita al Mercado de San Pedro en Cusco")
                    .description("Sumérgete en la cultura local visitando el Mercado de San Pedro, donde podrás comprar artesanías, probar comida tradicional peruana y conocer más sobre los ingredientes locales.")
                    .price(100.00)
                    .seller("Machupicchu Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 1, 15))
                    .duration("2 día")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity4);

            ImageEntity image1activity4 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624817/1_san_pedro_r7wuli.png")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image1activity4);

            ImageEntity image2activity4 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624898/2._Visita_al_Mercado_de_San_Pedro_en_Cusco_ezgnaz.png")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image2activity4);

            ImageEntity image3activity4 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624852/3._Visita_al_Mercado_de_San_Pedro_en_Cusco_lwiah1.webp")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image3activity4);

            ImageEntity image4activity4 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624871/4._Visita_al_Mercado_de_San_Pedro_en_Cusco_hn8pcg.png")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image4activity4);

            ImageEntity image5activity4 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733624836/5._Visita_al_Mercado_de_San_Pedro_en_Cusco_fg7z2c.png")
                    .touristPlan(activity4)
                    .build();
            imageRepository.save(image5activity4);

            //Actividad 6 Salineras Listo

            TouristPlanEntity activity5 = TouristPlanEntity.builder()
                    .title("Visita a las Salineras de Maras")
                    .description("Visita las impresionantes terrazas de sal en Maras, un sitio único donde se extrae sal de manera tradicional desde la época inca. Es una actividad cercana al Valle Sagrado, ideal para combinar con otros tours.")
                    .price(50.00)
                    .seller("Titicaca Tours")
                    .city(cities.get(7))
                    .category(categoryRepository.findByName("Activity"))
                    .capacity(10)
                    .availabilityStartDate(LocalDate.of(2024, 11, 1))
                    .availabilityEndDate(LocalDate.of(2025, 3, 30))
                    .duration("2 días")
                    .characteristic(List.of(
                            characteristicsList.get(0),
                            characteristicsList.get(2),
                            characteristicsList.get(3),
                            characteristicsList.get(8),
                            characteristicsList.get(9)))
                    .isActive(true)
                    .build();
            touristPlanRepository.save(activity5);

            ImageEntity image1activity5 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733625624/5._Visita_a_las_Salineras_de_Maras_w1yhgt.png")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image1activity5);

            ImageEntity image2activity5 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733625614/4._Visita_a_las_Salineras_de_Maras_xe2pbu.png")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image2activity5);

            ImageEntity image3activity5 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733625603/3._Visita_a_las_Salineras_de_Maras_w4k4vy.png")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image3activity5);

            ImageEntity image4activity5 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733625592/2._Visita_a_las_Salineras_de_Maras_igsouz.png")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image4activity5);

            ImageEntity image5activity5 = ImageEntity.builder()
                    .imageUrl("https://res.cloudinary.com/daksixwdc/image/upload/v1733625582/1._Visita_a_las_Salineras_de_Maras_c1axao.png")
                    .touristPlan(activity5)
                    .build();
            imageRepository.save(image5activity5);


        }
        // Hasta aquí

        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            UserEntity user1 = UserEntity.builder()
                    .name("Yanaira")
                    .lastName("Aranguren")
                    .email("yaranguren@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.BUYER)
                    .build();
            userRepository.save(user1);
            UserEntity user2 = UserEntity.builder()
                    .name("Jill Lee")
                    .lastName("Arias")
                    .email("jlee@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.BUYER)
                    .build();
            userRepository.save(user2);
            UserEntity user3 = UserEntity.builder()
                    .name("Ron Spenser")
                    .lastName("Sanchez")
                    .email("ronspenser@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.BUYER)
                    .build();
            userRepository.save(user3);
            UserEntity user4 = UserEntity.builder()
                    .name("Rafael")
                    .lastName("Jimenez")
                    .email("rjimenez@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.BUYER)
                    .build();
            userRepository.save(user4);
            UserEntity user5 = UserEntity.builder()
                    .name("Hans")
                    .lastName("Urpay")
                    .email("hurpay@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user5);
            UserEntity user6 = UserEntity.builder()
                    .name("admin")
                    .lastName("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user6);
        }

        List<ReviewEntity> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            ReviewEntity review1 = ReviewEntity.builder()
                    .user(userRepository.findById(1L).get())
                    .touristPlan(touristPlanRepository.findById(1L).get())
                    .rating(5)
                    .comment("¡Increíble experiencia! Desde el inicio, el plan superó mis expectativas. La caminata por las montañas fue guiada por expertos que compartieron datos fascinantes sobre la flora y fauna de la región. Además, las vistas desde la cima eran espectaculares, un verdadero paraíso para los amantes de la fotografía.")
                    .build();
            reviewRepository.save(review1);
            ReviewEntity review2 = ReviewEntity.builder()
                    .user(userRepository.findById(2L).get())
                    .touristPlan(touristPlanRepository.findById(1L).get())
                    .rating(4)
                    .comment("El recorrido fue encantador, lleno de historias y datos fascinantes sobre la arquitectura colonial y la cultura local. La guía era muy conocedora y respondió a todas nuestras preguntas con entusiasmo.\n" +
                            "\n" +
                            "Lo que más disfruté fue la visita al mercado artesanal y la degustación de dulces típicos, que agregaron un toque especial a la experiencia")
                    .build();
            reviewRepository.save(review2);
            ReviewEntity review3 = ReviewEntity.builder()
                    .user(userRepository.findById(3L).get())
                    .touristPlan(touristPlanRepository.findById(2L).get())
                    .rating(5)
                    .comment("¡Una experiencia mágica! Desde el paisaje hasta el ambiente, todo fue perfecto. Nos recibieron con una copa de vino espumoso y nos llevaron a un recorrido privado por los viñedos. Aprendimos mucho sobre la producción del vino y disfrutamos de una cata de cinco variedades acompañadas de quesos locales.")
                    .build();
            reviewRepository.save(review3);
            ReviewEntity review4 = ReviewEntity.builder()
                    .user(userRepository.findById(4L).get())
                    .touristPlan(touristPlanRepository.findById(2L).get())
                    .rating(4)
                    .comment("La caminata nocturna fue una experiencia fascinante. Con linternas y la guía de un experto, pudimos observar animales nocturnos como ranas y búhos. Además, el guía tenía un vasto conocimiento sobre el ecosistema local, lo que hizo el recorrido aún más interesante.")
                    .build();
            reviewRepository.save(review4);
            ReviewEntity review5 = ReviewEntity.builder()
                    .user(userRepository.findById(5L).get())
                    .touristPlan(touristPlanRepository.findById(3L).get())
                    .rating(5)
                    .comment("¡Un sueño hecho realidad! Visitar Machu Picchu fue una experiencia mágica, y el tour estuvo impecablemente organizado. Desde el viaje en tren con vistas espectaculares hasta la caminata por las antiguas ruinas, cada momento fue inolvidable.\n" +
                            "\n" +
                            "El guía fue increíblemente conocedor y apasionado, compartiendo historias y datos fascinantes sobre la civilización inca..")
                    .build();
            reviewRepository.save(review5);
            ReviewEntity review6 = ReviewEntity.builder()
                    .user(userRepository.findById(1L).get())
                    .touristPlan(touristPlanRepository.findById(3L).get())
                    .rating(5)
                    .comment("¡Una experiencia única e inolvidable! Ver las Líneas de Nazca desde el aire es algo que no se puede describir con palabras; la magnitud y el detalle de estas figuras son asombrosos. El piloto y el guía a bordo fueron muy profesionales y explicaron la historia y los misterios detrás de cada figura.")
                    .build();
            reviewRepository.save(review6);
            ReviewEntity review7 = ReviewEntity.builder()
                    .user(userRepository.findById(2L).get())
                    .touristPlan(touristPlanRepository.findById(4L).get())
                    .rating(4)
                    .comment("Llevamos a los niños al tour y todos disfrutamos muchísimo. Las actividades estaban bien pensadas y adaptadas para todas las edades..")
                    .build();
            reviewRepository.save(review7);
            ReviewEntity review8 = ReviewEntity.builder()
                    .user(userRepository.findById(3L).get())
                    .touristPlan(touristPlanRepository.findById(4L).get())
                    .rating(5)
                    .comment("Desde el primer contacto, el equipo de TURISTEANDO fue muy profesional. Todo salió mejor de lo esperado, gracias a su excelente organización.\n")
                    .build();
            reviewRepository.save(review8);
            ReviewEntity review9 = ReviewEntity.builder()
                    .user(userRepository.findById(4L).get())
                    .touristPlan(touristPlanRepository.findById(5L).get())
                    .rating(4)
                    .comment("Fuimos en pareja al TOUR y todo estuvo perfecto. La cena romántica y los paisajes hicieron que fuera una escapada inolvidable.")
                    .build();
            reviewRepository.save(review9);
            ReviewEntity review10 = ReviewEntity.builder()
                    .user(userRepository.findById(5L).get())
                    .touristPlan(touristPlanRepository.findById(5L).get())
                    .rating(5)
                    .comment("Por el precio que pagamos, recibimos mucho más de lo esperado. El Tour tiene una calidad increíble y vale cada centavo.\n")
                    .build();
            reviewRepository.save(review10);
            ReviewEntity review11 = ReviewEntity.builder()
                    .user(userRepository.findById(1L).get())
                    .touristPlan(touristPlanRepository.findById(6L).get())
                    .rating(5)
                    .comment("Lo mejor en mucho tiempo " +
                            "Habíamos probado otros servicios, pero el plan TURISTEANDO realmente se destacó. La calidad, el servicio y la experiencia fueron excepcionales.")
                    .build();
            reviewRepository.save(review11);
            ReviewEntity review12 = ReviewEntity.builder()
                    .user(userRepository.findById(2L).get())
                    .touristPlan(touristPlanRepository.findById(6L).get())
                    .rating(4)
                    .comment("Una maravilla natural " +
                            "el Tour de TURISTEANDO nos dejó sin palabras. Los paisajes eran de ensueño, y el servicio excelente. ¡Lo recomendamos a todos!")
                    .build();
            reviewRepository.save(review12);
            ReviewEntity review13 = ReviewEntity.builder()
                    .user(userRepository.findById(3L).get())
                    .touristPlan(touristPlanRepository.findById(7L).get())
                    .rating(5)
                    .comment("IUna jornada increíble llena de historia y paisajes. Visitamos Pisac, Ollantaytambo y Chinchero, y en cada lugar aprendimos sobre la cultura inca. Los mercados locales también fueron un punto destacado para comprar artesanías auténticas..")
                    .build();
            reviewRepository.save(review13);
            ReviewEntity review14 = ReviewEntity.builder()
                    .user(userRepository.findById(4L).get())
                    .touristPlan(touristPlanRepository.findById(7L).get())
                    .rating(4)
                    .comment("Cusco es una ciudad llena de historia y encanto. Visitamos la Catedral, el Templo de Qoricancha y Sacsayhuamán, y cada lugar era más fascinante que el anterior. El guía fue muy amable y apasionado por su trabajo.")
                    .build();
            reviewRepository.save(review14);
            ReviewEntity review15 = ReviewEntity.builder()
                    .user(userRepository.findById(5L).get())
                    .touristPlan(touristPlanRepository.findById(8L).get())
                    .rating(5)
                    .comment("Mi esposo y yo quedamos fascinados de todo lo que pudimos visitar con este TOUR, podríamos recomendar la maravillosa experiencia que tuvimos con TURISTEANDO, un equipo de personas muy profesionales y dispuestos a brindar la mejor atención a sus clientes.")
                    .build();
            reviewRepository.save(review15);
            ReviewEntity review16 = ReviewEntity.builder()
                    .user(userRepository.findById(1L).get())
                    .touristPlan(touristPlanRepository.findById(8L).get())
                    .rating(5)
                    .comment("Podríamos decir, que es todo un paraíso para los amantes de la naturaleza. Pudimos ver monos, guacamayos y hasta caimanes durante los recorridos en bote y caminatas guiadas. La experiencia nocturna fue emocionante, llena de sonidos de la selva.")
                    .build();
            reviewRepository.save(review16);
            ReviewEntity review17 = ReviewEntity.builder()
                    .user(userRepository.findById(2L).get())
                    .touristPlan(touristPlanRepository.findById(9L).get())
                    .rating(4)
                    .comment("Aventura para recordar " +
                            "hicimos el plan completo, no nos podíamos perder de todas las actividades recomendadas por el grupo de TURISTEANDO y cada momento fue emocionante. Las actividades estuvieron muy bien organizadas. ¡Excelente!")
                    .build();
            reviewRepository.save(review17);
            ReviewEntity review18 = ReviewEntity.builder()
                    .user(userRepository.findById(3L).get())
                    .touristPlan(touristPlanRepository.findById(9L).get())
                    .rating(5)
                    .comment("Una experiencia relajante con vistas hermosas del litoral limeño. Disfrutar del atardecer desde el catamarán fue lo mejor. El personal fue muy atento y nos ofrecieron bebidas y snacks durante el recorrido.")
                    .build();
            reviewRepository.save(review18);
            ReviewEntity review19 = ReviewEntity.builder()
                    .user(userRepository.findById(4L).get())
                    .touristPlan(touristPlanRepository.findById(10L).get())
                    .rating(4)
                    .comment("Aprender sobre el proceso de elaboración del pisco y disfrutar de una cata fue espectacular. Además, nos enseñaron a preparar el famoso Pisco Sour. El ambiente del tour fue muy agradable y los guías sabían mucho del tema.")
                    .build();
            reviewRepository.save(review19);
            ReviewEntity review20 = ReviewEntity.builder()
                    .user(userRepository.findById(5L).get())
                    .touristPlan(touristPlanRepository.findById(10L).get())
                    .rating(5)
                    .comment("Un tour increíble para los amantes del mar. Navegar hacia las Islas Palomino y nadar con lobos marinos fue una experiencia única. Los guías fueron muy atentos y cuidaron nuestra seguridad en todo momento")
                    .build();
            reviewRepository.save(review20);
            ReviewEntity review21 = ReviewEntity.builder()
                    .user(userRepository.findById(1L).get())
                    .touristPlan(touristPlanRepository.findById(11L).get())
                    .rating(5)
                    .comment("Una experiencia auténtica descubriendo pequeños restaurantes y locales de comida casera. Probamos platos únicos como el ají de gallina y el lomo saltado. Los anfitriones eran muy amables y nos explicaron cada receta con detalle")
                    .build();
            reviewRepository.save(review21);
            ReviewEntity review22 = ReviewEntity.builder()
                    .user(userRepository.findById(2L).get())
                    .touristPlan(touristPlanRepository.findById(11L).get())
                    .rating(4)
                    .comment("Una forma divertida y relajante de recorrer dos de los distritos más hermosos de Lima. Las vistas del malecón en Miraflores y la energía bohemia de Barranco fueron lo mejor. El guía fue excelente al compartir datos históricos y culturales durante todo el trayecto.")
                    .build();
            reviewRepository.save(review22);
        }

        List<ReservationEntity> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            ReservationEntity reservation1 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(1L).get())
                    .user(userRepository.findById(1L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation1);
            ReservationEntity reservation2 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(2L).get())
                    .user(userRepository.findById(2L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation2);
            ReservationEntity reservation3 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(3L).get())
                    .user(userRepository.findById(3L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation3);
            ReservationEntity reservation4 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(4L).get())
                    .user(userRepository.findById(4L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation4);
            ReservationEntity reservation5 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(5L).get())
                    .user(userRepository.findById(5L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation5);
            ReservationEntity reservation6 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(6L).get())
                    .user(userRepository.findById(1L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation6);
            ReservationEntity reservation7 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(7L).get())
                    .user(userRepository.findById(2L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation7);
            ReservationEntity reservation8 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(8L).get())
                    .user(userRepository.findById(3L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation8);
            ReservationEntity reservation9 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(9L).get())
                    .user(userRepository.findById(4L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation9);
            ReservationEntity reservation10 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(10L).get())
                    .user(userRepository.findById(5L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation10);
            ReservationEntity reservation11 = ReservationEntity.builder()
                    .touristPlan(touristPlanRepository.findById(11L).get())
                    .user(userRepository.findById(1L).get())
                    .startDate(LocalDate.of(2024, 11, 1))
                    .endDate(LocalDate.of(2024, 11, 3))
                    .peopleCount(2)
                    .build();
            reservationRepository.save(reservation11);
        }
    }
}
