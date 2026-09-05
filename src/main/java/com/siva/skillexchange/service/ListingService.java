package com.siva.skillexchange.service;

import com.siva.skillexchange.entity.Listing;
import com.siva.skillexchange.entity.User;
import com.siva.skillexchange.repository.ListingRepository;
import com.siva.skillexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserRepository userRepository;

    public Listing createListing(Integer userId, Listing listing) {

        // TC05: user must exist
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        listing.setUser(user);

        // TC04: all good, save the listing
        return listingRepository.save(listing);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public List<Listing> getListingsByCategory(String category) {
        return listingRepository.findByCategory(category);
    }
}