package com.smk.imdb.service.impl;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.entity.Movie;
import com.smk.imdb.entity.MovieReview;
import com.smk.imdb.enums.MovieGenre;
import com.smk.imdb.enums.MovieRecommendationChoice;
import com.smk.imdb.repository.MovieRepository;
import com.smk.imdb.repository.MovieReviewRepository;
import com.smk.imdb.service.IRecommendationService;
import com.smk.imdb.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImplementation implements IRecommendationService {

    @Autowired
    private MovieReviewRepository movieReviewRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<MovieDTO> recommendMovies(MovieRecommendationChoice choice) {
        String emailOfLoggedInUser = SecurityUtil.getEmailOfLoggedInUser();
        if (choice.equals(MovieRecommendationChoice.DIRECTOR)) {
            return recommendMoviesByDirector(emailOfLoggedInUser);
        } else {
            return recommendMoviesByGenre(emailOfLoggedInUser);
        }
    }

    private List<MovieDTO> recommendMoviesByGenre(String emailOfLoggedInUser) {
        List<MovieReview> movieReviews =
                movieReviewRepository.listMostLikedByUser(emailOfLoggedInUser);

        int topK = movieReviews.size() > 5 ? 5 : movieReviews.size();
        List<MovieReview> topKMovieReviews = movieReviews.subList(0, topK);

        List<Long> movieIDS =
                topKMovieReviews.stream().map(movieReview -> movieReview.getMovieId()).collect(Collectors.toList());

        List<Movie> movieListByID = movieRepository.findAllById(movieIDS);


        List<MovieGenre> topKMovieGenres = movieListByID.stream().map(movie -> movie.getGenre()).collect(Collectors.toList());

        List<Movie> recommendedMovieByGenre =
                movieRepository.recommendMovieByGenre(movieIDS, topKMovieGenres);

        return recommendedMovieByGenre.stream().
                map(movie -> convertMovieToDTO(movie)).collect(Collectors.toList());
    }

    private List<MovieDTO> recommendMoviesByDirector(String emailOfLoggedInUser) {
        List<MovieReview> movieReviews =
                movieReviewRepository.listMostLikedByUser(emailOfLoggedInUser);

        int topK = movieReviews.size() > 5 ? 5 : movieReviews.size();
        List<MovieReview> topKMovieReviews = movieReviews.subList(0, topK);

        List<Long> movieIDS =
                topKMovieReviews.stream().map(movieReview -> movieReview.getMovieId()).collect(Collectors.toList());

        List<Movie> movieListByID = movieRepository.findAllById(movieIDS);

        List<String> topKDirectorList =
                movieListByID.stream().map(movie -> movie.getDirector()).collect(Collectors.toList());

        List<Movie> recommendedMovieByDirector =
                movieRepository.recommendMovieByDirector(movieIDS, topKDirectorList);

        return recommendedMovieByDirector.stream().
                map(movie -> convertMovieToDTO(movie)).collect(Collectors.toList());


    }

    private MovieDTO convertMovieToDTO(Movie movie) {
        return MovieDTO.builder()
                .id(movie.getId())
                .genre(movie.getGenre())
                .name(movie.getName())
                .director(movie.getDirector())
                .rating(movie.getRating())
                .build();
    }
}
