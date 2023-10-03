package com.smk.imdb.service.impl;

import com.smk.imdb.enums.MovieGenre;
import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.dto.MovieReviewDTO;
import com.smk.imdb.entity.Customer;
import com.smk.imdb.entity.Movie;
import com.smk.imdb.repository.CustomerRepository;
import com.smk.imdb.repository.MovieRepository;
import com.smk.imdb.repository.MovieReviewRepository;
import com.smk.imdb.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovieServiceImplementationTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieReviewRepository movieReviewRepository;

    @InjectMocks
    private MovieServiceImplementation movieServiceImplementation;

    @Test
    public void listAllMoviesTest() {
        Movie race3 = Movie.builder()
                .id(1L)
                .genre(MovieGenre.ACTION)
                .name("RACE3")
                .rating(4.4)
                .build();

        Page<Movie> page = new PageImpl<>(List.of(race3));
        PageRequest pageRequest = PageRequest.of(1, 10);
        when(movieRepository.findAll(pageRequest)).thenReturn(page);

        List<MovieDTO> movieDTOS = movieServiceImplementation.listAllMovies(1, 10);

        assertEquals(1, movieDTOS.size());
        verify(movieRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    public void findMovieByNameTest() {
        Movie race3 = Movie.builder()
                .id(1L)
                .genre(MovieGenre.ACTION)
                .name("RACE3")
                .rating(4.4)
                .build();

        when(movieRepository.findByNameLike(anyString())).thenReturn(List.of(race3));
        List<MovieDTO> movieDTOList = movieServiceImplementation.findMovieByName("RACE3");

        assertEquals(1, movieDTOList.size());
        assertEquals(4.4, movieDTOList.get(0).getRating());
        verify(movieRepository, times(1)).findByNameLike(any());
    }

    @Test
    public void submitReviewTest() {
        Movie race3 = Movie.builder()
                .id(1L)
                .genre(MovieGenre.ACTION)
                .name("RACE3")
                .rating(4.4)
                .build();

        Customer customer = Customer.builder()
                .email("samkaz@gmail.com")
                .id(1)
                .build();

        MovieReviewDTO movieReviewDTO = MovieReviewDTO.builder()
                .movieId(1L)
                .rating(4.5)
                .reviewDescription("fantastic movie")
                .build();

        MockedStatic<SecurityUtil> secUtil = Mockito.mockStatic(SecurityUtil.class);
        secUtil.when(() -> SecurityUtil.getEmailOfLoggedInUser()).thenReturn("samkaz@gmail.com");

        when(movieRepository.findById(any())).thenReturn(Optional.ofNullable(race3));
        when(customerRepository.findByEmail(any())).thenReturn(List.of(customer));

        movieServiceImplementation.submitReview(movieReviewDTO);

        verify(movieRepository, times(1)).save(any());
        verify(movieReviewRepository, times(1)).save(any());
    }
}