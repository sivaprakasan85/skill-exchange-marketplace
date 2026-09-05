package com.siva.skillexchange.entity;

public class RequestCreateDTO {

    private Integer listingId;
    private Integer requesterId;

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }
}