package com.smk.imdb.service.impl;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.dto.MovieReviewDTO;
import com.smk.imdb.entity.Customer;
import com.smk.imdb.entity.Movie;
import com.smk.imdb.entity.MovieReview;
import com.smk.imdb.exception.ImdbException;
import com.smk.imdb.repository.CustomerRepository;
import com.smk.imdb.repository.MovieRepository;
import com.smk.imdb.repository.MovieReviewRepository;
import com.smk.imdb.service.IMovieService;
import com.smk.imdb.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImplementation implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Override
    public List<MovieDTO> listAllMovies(Integer pageNumber, Integer pageSize) {
        PageRequest page = PageRequest.of(pageNumber, pageSize);
        List<Movie> movieList = movieRepository.findAll(page).getContent();
        return movieList.stream().map(this::convertMovieToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findMovieByName(String movieName){
        List<Movie> movieList = movieRepository.findByNameLike(movieName);
        return movieList.stream().map(this::convertMovieToDTO).collect(Collectors.toList());
    }

    @Override
    public void submitReview(MovieReviewDTO movieReviewDTO) {
        Long movieId = movieReviewDTO.getMovieId();
        String email = SecurityUtil.getEmailOfLoggedInUser();
        Movie movie = movieRepository.findById(movieId).
                orElseThrow(() -> new ImdbException(HttpStatus.NOT_FOUND, "Movie does not exist"));

        List<Customer> byEmail = customerRepository.findByEmail(email);
        if (byEmail == null || byEmail.isEmpty()) {
            throw new ImdbException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        MovieReview movieReview = MovieReview.builder()
                .movieId(movieId)
                .userEmail(email)
                .rating(movieReviewDTO.getRating())
                .reviewDescription(movieReviewDTO.getReviewDescription())
                .build();

        movieReviewRepository.save(movieReview);
        calculateAverageRatingOfMovie(movie, movieReviewDTO.getRating());
    }

    private void calculateAverageRatingOfMovie(Movie movie, Double rating) {
        Integer noOfReviews = movieReviewRepository.countReviewByMovieId(movie.getId());
        Double previousTotal = 0.0;
        if(noOfReviews > 1){
            previousTotal = movie.getRating() * (noOfReviews - 1);
        }
        Double newAverageRating = (rating + previousTotal) / noOfReviews;
        movie.setRating(newAverageRating);
        movieRepository.save(movie);
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
