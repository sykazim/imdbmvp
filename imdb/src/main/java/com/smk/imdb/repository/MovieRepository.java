package com.smk.imdb.repository;

import com.smk.imdb.entity.Movie;
import com.smk.imdb.enums.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByNameLike(String name);

    @Query("select m from Movie m where m.id not in :movieIDS and m.director in :directors order by m.rating DESC")
    List<Movie> recommendMovieByDirector(List<Long> movieIDS, List<String>directors);

    @Query("select m from Movie m where m.id not in :movieIDS and m.genre in :genre order by m.rating DESC")
    List<Movie> recommendMovieByGenre(List<Long> movieIDS, List<MovieGenre>genre);
}
