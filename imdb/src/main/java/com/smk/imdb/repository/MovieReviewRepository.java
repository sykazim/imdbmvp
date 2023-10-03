package com.smk.imdb.repository;

import com.smk.imdb.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {

    @Query("select count(mr) from MovieReview mr where mr.movieId=:movieId")
    Integer countReviewByMovieId(@Param("movieId") Long movieId);

    @Query("select mr from MovieReview mr where mr.userEmail=:userEmail order by mr.rating DESC")
    List<MovieReview> listMostLikedByUser(@Param("userEmail")String userEmail);
}
