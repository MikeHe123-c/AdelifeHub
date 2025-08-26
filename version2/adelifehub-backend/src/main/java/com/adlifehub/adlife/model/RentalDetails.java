package com.adlifehub.adlife.model;

import lombok.Data;

@Data
public class RentalDetails {
    public Long listingId;
    public Integer bedrooms;
    public Integer bathrooms;
    public Integer parking;
    public Boolean furnished;
    public Integer bond;
    public java.time.LocalDate availableFrom;
    public String leaseTerm;
}
