package com.dailycodework.lakesidehotel.service;

import com.dailycodework.lakesidehotel.model.FitnessBooking;
import java.time.LocalDate;
import java.util.List;

public interface IFitnessBookingService {
    List<String> findBookedStartTimes(LocalDate date);
    FitnessBooking createBooking(FitnessBooking booking);
    List<FitnessBooking> getAllBookings(); // 🌟 เมธอด Admin
    void cancelBooking(Long bookingId); 
}