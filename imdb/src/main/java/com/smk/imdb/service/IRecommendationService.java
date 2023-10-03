package com.smk.imdb.service;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.enums.MovieRecommendationChoice;

import java.util.List;

public interface IRecommendationService {

    List<MovieDTO> recommendMovies(MovieRecommendationChoice choice);
}
