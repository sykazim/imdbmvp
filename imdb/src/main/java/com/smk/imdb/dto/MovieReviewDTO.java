package com.smk.imdb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieReviewDTO {

    private Long movieId;
    private Double rating;
    private String reviewDescription;
}
