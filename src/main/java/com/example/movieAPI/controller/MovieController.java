package com.example.movieAPI.controller;

import com.example.movieAPI.exception.ResourceNotFoundException;
import com.example.movieAPI.model.Movie;
import com.example.movieAPI.repository.MovieRepository;
import com.example.movieAPI.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {
    @Autowired
    MovieRepository movieRepository;

//    Get all movie
    @GetMapping("/movie")
    public List<Movie> getAllMovie(){
        return movieRepository.findAll();
    }

    @Autowired
    private SequenceGeneratorService service;

//    Create new movie
    @PostMapping("/movie")
    public Movie createMovie(@Valid @RequestBody Movie movie) {
        movie.setId(service.getSequenceNumber(Movie.SEQUENCE_NAME));
        return movieRepository.save(movie);
    }

//    Get a single movie
    @GetMapping("/movie/{id}")
    public Movie getMovieById(@PathVariable(value = "id") Long movieId){
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "Id", movieId));
    }

//    update movie
    @PutMapping("/movie/{id}")
    public Movie updateBuku(@PathVariable(value = "id") Long movieId,
                           @Valid @RequestBody Movie movieDetails){
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

        movie.setMovieName(movieDetails.getMovieName());
        movie.setDescription(movieDetails.getDescription());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setGenre(movieDetails.getGenre());
        movie.setDirector(movieDetails.getDirector());
        movie.setRating(movieDetails.getRating());
        movie.setPersisted(true);

        Movie updatedMovie = movieRepository.save(movie);
        return updatedMovie;
    }

//    Delete buku
    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

        movieRepository.delete(movie);

        return ResponseEntity.ok().build();
    }
}
