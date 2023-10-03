package com.smk.imdb.dto;

import com.smk.imdb.enums.MovieGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    private Long id;

    private String name;

    private MovieGenre genre;

    private String director;

    private double rating;
}
