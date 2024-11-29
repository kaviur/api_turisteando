package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReviewRequestDto;
import com.proyecto.turisteando.dtos.responseDto.ReviewResponseDto;
import com.proyecto.turisteando.entities.ReviewEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import com.proyecto.turisteando.exceptions.customExceptions.*;
import com.proyecto.turisteando.mappers.ReviewMapper;
import com.proyecto.turisteando.repositories.IUserRepository;
import com.proyecto.turisteando.repositories.ReservationRepository;
import com.proyecto.turisteando.repositories.ReviewRepository;
import com.proyecto.turisteando.repositories.TouristPlanRepository;
import com.proyecto.turisteando.services.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final TouristPlanRepository touristPlanRepository;
    private final IUserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final ReservationRepository reservationRepository;


    @Override
    public Iterable<ReviewResponseDto> getAll() {
        try {
            Iterable<ReviewEntity> countries = reviewRepository.findAll();
            return StreamSupport.stream(countries.spliterator(), false)
                    .map(reviewMapper::toResponseDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ReviewResponseDto read(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        return reviewMapper.toResponseDto(reviewEntity);
    }

    @Override
    public ReviewResponseDto create(ReviewRequestDto reviewRequestDto) {
        try {
            boolean hasReservation = reservationRepository.existsByUserIdAndTouristPlanId(
                    reviewRequestDto.getIdUser(),
                    reviewRequestDto.getPlanId());
            if (!hasReservation) {
                throw new ReviewWithoutReservationException("No se puede crear una reseña sin haber reservado el plan turístico");
            }

            boolean alreadyReviewed = reviewRepository.existsByUserIdAndTouristPlanId(
                    reviewRequestDto.getIdUser(),
                    reviewRequestDto.getPlanId());
            if (alreadyReviewed) {
                throw new ReviewAlreadyExistsException("Ya has realizado una reseña para este plan turístico");
            }

            TouristPlanEntity touristPlan = touristPlanRepository.findById(reviewRequestDto.getPlanId())
                    .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + reviewRequestDto.getPlanId() + " not found"));
            UserEntity user = userRepository.findById(reviewRequestDto.getIdUser())
                    .orElseThrow(() -> new ReviewNotFoundException("User with id " + reviewRequestDto.getIdUser() + " not found"));
            ReviewEntity reviewEntity = reviewMapper.toEntity(reviewRequestDto);
            reviewEntity.setTouristPlan(touristPlan);
            reviewEntity.setUser(user);

            // Actualizar el total de reseñas y estrellas en el plan
            touristPlan.setTotalReviews(touristPlan.getTotalReviews() + 1);
            touristPlan.setTotalStars(touristPlan.getTotalStars() + reviewRequestDto.getRating());

            touristPlanRepository.save(touristPlan); // Guardar el plan turístico con el nuevo cálculo

            return reviewMapper.toResponseDto(reviewRepository.save(reviewEntity));
        } catch (Exception e) {
            throw new ServiceException("Error creating review entity " + e.getMessage());
        }
    }

    @Override
    public ReviewResponseDto update(ReviewRequestDto reviewRequestDto, Long id) {

        ReviewEntity existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));

        // Verificar que el usuario que está intentando actualizar sea el mismo que creó la review
        if (!existingReview.getUser().getId().equals(reviewRequestDto.getIdUser())) {
            throw new UnauthorizedActionException("Sólo el creador de la review puede modificarla");
        }

        // Obtener el plan turístico asociado
        TouristPlanEntity touristPlan = existingReview.getTouristPlan();

        // Restar el rating actual del total de estrellas del plan
        touristPlan.setTotalStars(touristPlan.getTotalStars() - existingReview.getRating());

        // Actualizar la review con el nuevo rating
        ReviewEntity updatedReview = reviewMapper.partialUpdate(reviewRequestDto, existingReview);

        // Agregar el nuevo rating al total de estrellas del plan
        touristPlan.setTotalStars(touristPlan.getTotalStars() + updatedReview.getRating());

        // Guardar los cambios en el plan turístico
        touristPlanRepository.save(touristPlan);

        // Guardar y retornar la review actualizada
        return reviewMapper.toResponseDto(reviewRepository.save(updatedReview));

    }

    @Override
    public ReviewResponseDto delete(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));

        // Obtener la autenticación del contexto de seguridad
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName(); // Nombre del usuario autenticado

        // Verificar si el usuario tiene el rol de ADMIN
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Validar si el usuario logueado es el creador de la review o un administrador
        if (!reviewEntity.getUser().getUsername().equals(currentUsername) && !isAdmin) {
            throw new UnauthorizedActionException("No tienes permisos para eliminar esta review");
        }

        reviewEntity.setStatus((byte) 0);
        ReviewEntity deletedReview = reviewRepository.save(reviewEntity);

        // Actualizar el total de reseñas y estrellas en el plan tras eliminar reseña
        TouristPlanEntity plan = reviewEntity.getTouristPlan();
        plan.setTotalReviews(plan.getTotalReviews() - 1);
        plan.setTotalStars(plan.getTotalStars() - reviewEntity.getRating());
        touristPlanRepository.save(plan);

        return reviewMapper.toResponseDto(deletedReview);
    }

    @Override
    public ReviewResponseDto toggleStatus(Long id) {
        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        reviewEntity.setStatus(reviewEntity.getStatus() == 1 ? (byte) 0 : (byte) 1);
        ReviewEntity updatedReview = reviewRepository.save(reviewEntity);
        return reviewMapper.toResponseDto(updatedReview);
    }

    @Override
    public Iterable<ReviewResponseDto> getAllByPlan(Long idPlan) {
        TouristPlanEntity plan = touristPlanRepository.findById(idPlan)
                .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + idPlan + " not found"));
        return reviewRepository.findByTouristPlanIdAndStatus(idPlan, 1)
                .stream().map(reviewMapper::toResponseDto)
                .toList();
    }

    @Override
    public Iterable<ReviewResponseDto> getAllByRating(Long idPlan, int rating) {
        TouristPlanEntity plan = touristPlanRepository.findById(idPlan)
                .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + idPlan + " not found"));
        return reviewRepository.findByRatingAndTouristPlanIdAndStatus(rating, idPlan, 1)
                .stream().map(reviewMapper::toResponseDto)
                .toList();

    }

    @Override
    public Page<ReviewResponseDto> getAllByPlanP(Long idPlan, Pageable pageable) {
        TouristPlanEntity plan = touristPlanRepository.findById(idPlan)
                .orElseThrow(() -> new TouristPlanNotFoundException("Tourist plan with id " + idPlan + " not found"));
        Page<ReviewEntity> reviews = reviewRepository.findByTouristPlanIdAndStatus(idPlan, 1, pageable);

        return reviews.map(reviewMapper::toResponseDto);
    }

    @Override
    public Page<ReviewResponseDto> getAllByUser(Long idUser, Pageable pageable) {
        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new ReviewNotFoundException("User with id " + idUser + " not found"));
        Page<ReviewEntity> reviews = reviewRepository.findByUserIdAndStatus(idUser, 1, pageable);

        return reviews.map(reviewMapper::toResponseDto);
    }
}
