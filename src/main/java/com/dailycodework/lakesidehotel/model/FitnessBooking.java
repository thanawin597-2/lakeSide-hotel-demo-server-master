package com.dailycodework.lakesidehotel.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp; 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name = "fitness_booking")
@Data
public class FitnessBooking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId; 
    
    @Column(name = "user_id")
    private Long userId; 
    
    @Column(name = "activity_type")
    private String activityType; 
    
    @Column(name = "booking_date")
    private LocalDate bookingDate; 
    
   /* @Column(name = "start_time")
    private LocalTime startTime; 
    
    @Column(name = "end_time")
    private LocalTime endTime; */
    
    @Column(name = "confirmation_code")
    private String confirmationCode; 
    
    @CreationTimestamp 
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 🌟 ใช้ LocalDateTime
    
    @JsonFormat(pattern = "HH:mm") // 🌟 กำหนดให้เป็น HH:mm (เช่น 09:00 หรือ 17:00) 🌟
    private LocalTime startTime; 

    @JsonFormat(pattern = "HH:mm") // 🌟 กำหนดให้เป็น HH:mm
    private LocalTime endTime;
    
    @Column(name = "guest_name")
    private String guestName;
}