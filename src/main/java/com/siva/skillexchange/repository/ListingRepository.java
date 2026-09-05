package com.siva.skillexchange.repository;

import com.siva.skillexchange.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Integer> {

    long countByUserId(Integer userId);

    List<Listing> findByCategory(String category);
}