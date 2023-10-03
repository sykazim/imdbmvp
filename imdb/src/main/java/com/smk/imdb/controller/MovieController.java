package com.smk.imdb.controller;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.dto.MovieReviewDTO;
import com.smk.imdb.dto.ResponseDTO;
import com.smk.imdb.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private IMovieService iMovieService;

    @GetMapping("")
    public List<MovieDTO> listAllMovies(@RequestHeader(value = "Authorization") String jwtToken,
                                        @RequestParam(defaultValue = "0") Integer pageNumber,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        return iMovieService.listAllMovies(pageNumber, pageSize);
    }

    @GetMapping("/name/{movieName}")
    public List<MovieDTO> findMovieByName(@RequestHeader(value = "Authorization") String jwtToken,
                                          @PathVariable(name = "movieName") String movieName) {
        return iMovieService.findMovieByName(movieName);
    }

    @PostMapping("/review")
    public ResponseDTO postReview(@RequestHeader(value = "Authorization") String jwtToken,
                                  @RequestBody MovieReviewDTO movieReviewDTO) {

        iMovieService.submitReview(movieReviewDTO);

        return ResponseDTO.builder().status(HttpStatus.CREATED)
                .message("Review Posted successfully.")
                .build();
    }
}
