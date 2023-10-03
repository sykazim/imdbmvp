package com.smk.imdb.service.impl;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.entity.Movie;
import com.smk.imdb.entity.MovieReview;
import com.smk.imdb.enums.MovieGenre;
import com.smk.imdb.enums.MovieRecommendationChoice;
import com.smk.imdb.repository.MovieRepository;
import com.smk.imdb.repository.MovieReviewRepository;
import com.smk.imdb.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecommendationServiceImplementationTest {

    @Mock
    private MovieReviewRepository movieReviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RecommendationServiceImplementation recommendationServiceImplementation;

    @Test
    public void recommendMoviesByGenreTest() {
        MockedStatic<SecurityUtil> secUtil = Mockito.mockStatic(SecurityUtil.class);
        secUtil.when(() -> SecurityUtil.getEmailOfLoggedInUser()).thenReturn("samkaz@gmail.com");

        MovieReview movieReview = MovieReview.builder()
                .movieId(1L)
                .rating(4.2)
                .userEmail("samkaz@gmail.com")
                .reviewDescription("fantastic")
                .build();

        Movie movie = Movie.builder()
                .id(1L)
                .genre(MovieGenre.ACTION)
                .name("RACE3")
                .rating(4.4)
                .build();

        Movie movieRecommended = Movie.builder()
                .id(2L)
                .genre(MovieGenre.ACTION)
                .name("Dhoom3")
                .rating(4.4)
                .build();

        when(movieReviewRepository.listMostLikedByUser(anyString())).thenReturn(List.of(movieReview));
        when(movieRepository.findAllById(any())).thenReturn(List.of(movie));
        when(movieRepository.recommendMovieByGenre(any(), any())).thenReturn(List.of(movieRecommended));

        List<MovieDTO> movieDTOS = recommendationServiceImplementation.recommendMovies(MovieRecommendationChoice.GENRE);
        assertEquals("Dhoom3", movieDTOS.get(0).getName());
    }

}