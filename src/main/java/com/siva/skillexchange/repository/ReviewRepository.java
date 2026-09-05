package com.siva.skillexchange.repository;

import com.siva.skillexchange.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByRequest_Listing_User_Id(Integer userId);
}