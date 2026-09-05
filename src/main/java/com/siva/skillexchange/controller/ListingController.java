package com.siva.skillexchange.controller;

import com.siva.skillexchange.entity.Listing;
import com.siva.skillexchange.entity.ListingRequest;
import com.siva.skillexchange.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @PostMapping
    public ResponseEntity<?> createListing(@RequestBody ListingRequest request) {
        try {
            Listing listing = new Listing();
            listing.setTitle(request.getTitle());
            listing.setDescription(request.getDescription());
            listing.setCategory(request.getCategory());
            listing.setPriceOrFree(request.getPriceOrFree());

            Listing savedListing = listingService.createListing(request.getUserId(), listing);
            return new ResponseEntity<>(savedListing, HttpStatus.CREATED);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Listing>> getListings(
            @RequestParam(required = false) String category) {

        List<Listing> listings;
        if (category != null && !category.isEmpty()) {
            listings = listingService.getListingsByCategory(category);
        } else {
            listings = listingService.getAllListings();
        }
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }
}