package com.proyecto.turisteando.dtos.responseDto;

import com.proyecto.turisteando.dtos.IDto;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@AllArgsConstructor
public class ReviewResponseDto implements Serializable, IDto {

    Long idReview;
    int rating;
    Date date;
    String comment;
}
