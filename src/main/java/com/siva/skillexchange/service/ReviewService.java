package com.siva.skillexchange.service;

import com.siva.skillexchange.entity.Request;
import com.siva.skillexchange.entity.RequestStatus;
import com.siva.skillexchange.entity.Review;
import com.siva.skillexchange.repository.RequestRepository;
import com.siva.skillexchange.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Review submitReview(Integer requestId, Integer rating, String comment) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));

        // TC17: request must be COMPLETED before reviewing
        if (request.getStatus() != RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot review incomplete request");
        }

        // TC18: rating must be between 1 and 5
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review(request, rating, comment);
        return reviewRepository.save(review);
    }

    public Object getAverageRating(Integer userId) {
        List<Review> reviews = reviewRepository.findByRequest_Listing_User_Id(userId);

        // TC20: no reviews yet
        if (reviews.isEmpty()) {
            return "No ratings yet";
        }

        // TC19: calculate the average
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        double average = sum / reviews.size();

        return average;
    }
}