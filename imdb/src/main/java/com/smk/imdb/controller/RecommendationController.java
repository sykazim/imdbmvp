package com.smk.imdb.controller;

import com.smk.imdb.dto.MovieDTO;
import com.smk.imdb.enums.MovieRecommendationChoice;
import com.smk.imdb.service.IRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {

    @Autowired
    private IRecommendationService iRecommendationService;

    @GetMapping("/by/{choice}")
    public List<MovieDTO> recommendMovies(@RequestHeader(value = "Authorization") String jwtToken,
                                          @PathVariable("choice") MovieRecommendationChoice choice) {
        return iRecommendationService.recommendMovies(choice);
    }
}
