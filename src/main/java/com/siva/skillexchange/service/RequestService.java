package com.siva.skillexchange.service;
import com.siva.skillexchange.entity.Listing;
import com.siva.skillexchange.entity.Request;
import com.siva.skillexchange.entity.RequestStatus;
import com.siva.skillexchange.entity.User;
import com.siva.skillexchange.repository.ListingRepository;
import com.siva.skillexchange.repository.RequestRepository;
import com.siva.skillexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserRepository userRepository;

    public Request createRequest(Integer listingId, Integer requesterId) {

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));

        User requester = userRepository.findByIdAndActiveTrue(requesterId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // TC10: cannot request your own listing
        if (listing.getUser().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Cannot request your own listing");
        }

        Request request = new Request(listing, requester);
        // status defaults to PENDING automatically (set in the entity)

        return requestRepository.save(request);
    }

    public Request acceptRequest(Integer requestId) {
        Request request = getRequestOrThrow(requestId);

        // TC12: cannot accept an already-accepted request
        if (request.getStatus() == RequestStatus.ACCEPTED) {
            throw new IllegalStateException("Request already accepted");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        return requestRepository.save(request);
    }

    public Request rejectRequest(Integer requestId) {
        Request request = getRequestOrThrow(requestId);

        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    public Request completeRequest(Integer requestId) {
        Request request = getRequestOrThrow(requestId);

        // TC15: must be ACCEPTED before it can be COMPLETED
        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new IllegalStateException("Request must be accepted first");
        }

        request.setStatus(RequestStatus.COMPLETED);
        return requestRepository.save(request);
    }

    private Request getRequestOrThrow(Integer requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
    }
}
