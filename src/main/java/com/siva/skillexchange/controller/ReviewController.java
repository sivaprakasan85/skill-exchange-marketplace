package com.siva.skillexchange.controller;

import com.siva.skillexchange.entity.Review;
import com.siva.skillexchange.entity.ReviewCreateDTO;
import com.siva.skillexchange.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<?> submitReview(@RequestBody ReviewCreateDTO dto) {
        try {
            Review review = reviewService.submitReview(
                    dto.getRequestId(), dto.getRating(), dto.getComment());
            return new ResponseEntity<>(review, HttpStatus.CREATED);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{id}/rating")
    public ResponseEntity<?> getUserRating(@PathVariable Integer id) {
        Object result = reviewService.getAverageRating(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}