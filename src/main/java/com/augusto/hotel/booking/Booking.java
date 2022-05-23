package com.augusto.hotel.booking;

import com.augusto.hotel.commons.DefaultEntity;
import com.augusto.hotel.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking extends DefaultEntity {
    @Column(unique=true)
    LocalDate checkIn;
    LocalDate checkOut;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
}
