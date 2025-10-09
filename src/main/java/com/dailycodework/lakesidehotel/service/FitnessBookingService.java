package com.dailycodework.lakesidehotel.service;

import com.dailycodework.lakesidehotel.exception.ResourceNotFoundException;
import com.dailycodework.lakesidehotel.model.FitnessBooking;
import com.dailycodework.lakesidehotel.repository.FitnessBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FitnessBookingService implements IFitnessBookingService { 

    private final FitnessBookingRepository bookingRepository;
    private final int MAX_CAPACITY = 5; 

    @Override
    public List<String> findBookedStartTimes(LocalDate date) {
        
        // 🌟 Logic การกรองเวลาที่แม่นยำ
        LocalTime currentTime = date.isEqual(LocalDate.now()) ? LocalTime.now() : LocalTime.MIN;
        
        List<LocalTime> bookedTimes = bookingRepository.findBookedStartTimes(date, currentTime); 
        
        // แปลง LocalTime เป็น String
        return bookedTimes.stream().map(LocalTime::toString).toList();
    }
    
    @Override
    public FitnessBooking createBooking(FitnessBooking booking) {
        
        booking.setEndTime(booking.getStartTime().plusHours(1));

        int currentBookings = bookingRepository.countBookingsAtSlot(
            booking.getBookingDate(), 
            booking.getStartTime()
        );
        
        if (currentBookings >= MAX_CAPACITY) {
            throw new IllegalStateException("ช่วงเวลานี้ถูกจองเต็มแล้ว (เต็ม " + MAX_CAPACITY + " คน)");
        }
        
        booking.setConfirmationCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        booking.setActivityType("Fitness"); 
        
        return bookingRepository.save(booking);
    }
    
 // 🌟 Implementation สำหรับ Admin
    public List<FitnessBooking> getAllBookings() {
        return bookingRepository.findAll(); 
    }
    
    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        // 1. ตรวจสอบว่ารายการจองมีอยู่จริงหรือไม่
        if (!bookingRepository.existsById(bookingId)) {
            
            throw new ResourceNotFoundException("Booking not found for ID: " + bookingId);
        }
        
        // 2. ถ้ามี ให้ทำการลบ
        bookingRepository.deleteById(bookingId);
    }
 }
