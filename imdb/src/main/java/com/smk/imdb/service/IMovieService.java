package com.smk.imdb.service;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.dto.MovieReviewDTO;

import java.util.List;

public interface IMovieService {

    List<MovieDTO> listAllMovies(Integer pageNumber, Integer pageSize);

    List<MovieDTO> findMovieByName(String movieName);

    void submitReview(MovieReviewDTO movieReviewDTO);
}
