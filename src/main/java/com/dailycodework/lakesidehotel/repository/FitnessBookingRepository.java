package com.dailycodework.lakesidehotel.repository;

import com.dailycodework.lakesidehotel.model.FitnessBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FitnessBookingRepository extends JpaRepository<FitnessBooking, Long> {

    // Query สำหรับดึงเวลาที่ถูกจองแล้ว (ใช้ใน GET API)
    @Query("SELECT b.startTime FROM FitnessBooking b " +
           "WHERE b.bookingDate = :date AND b.endTime > :currentTime")
    List<LocalTime> findBookedStartTimes(LocalDate date, LocalTime currentTime);

    // Query สำหรับนับจำนวนการจอง
    @Query("SELECT COUNT(b) FROM FitnessBooking b " +
           "WHERE b.bookingDate = :date AND b.startTime = :startTime")
    int countBookingsAtSlot(LocalDate date, LocalTime startTime);
}