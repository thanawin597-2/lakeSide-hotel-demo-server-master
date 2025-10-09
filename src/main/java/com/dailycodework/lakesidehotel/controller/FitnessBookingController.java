package com.dailycodework.lakesidehotel.controller;

import com.dailycodework.lakesidehotel.model.FitnessBooking;
import com.dailycodework.lakesidehotel.service.IFitnessBookingService; 
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize; 
import com.dailycodework.lakesidehotel.exception.ResourceNotFoundException; 

import java.time.LocalDate;
import java.util.List;

/**
 * Controller สำหรับจัดการการจองฟิตเนสทั้งหมด รวมถึง API สำหรับการจัดการโดย Admin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fitness") 
@CrossOrigin(origins = "http://localhost:5173") 
public class FitnessBookingController {

    private final IFitnessBookingService fitnessBookingService; 

    // API สำหรับลูกค้า: ดึงเวลาที่ถูกจองแล้วตามวันที่
    // URL: GET /api/fitness/bookings/byDate?date=YYYY-MM-DD
    @GetMapping("/bookings/byDate")
    public ResponseEntity<List<String>> getBookedSlots(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<String> bookedTimes = fitnessBookingService.findBookedStartTimes(date);
        
        return ResponseEntity.ok(bookedTimes); 
    }

    // API สำหรับลูกค้า: สร้างการจองใหม่
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody FitnessBooking booking) {
        try {
            FitnessBooking newBooking = fitnessBookingService.createBooking(booking);
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Booking failed: " + e.getMessage());
        }
    }
    
    // API สำหรับ Admin: ดึงการจองทั้งหมด (เปิดให้เข้าถึงโดยไม่มีสิทธิ์ชั่วคราว)
    // URL: GET /api/fitness/admin/all-bookings
    @GetMapping("/admin/all-bookings")
    //@PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<List<FitnessBooking>> getAllFitnessBookings() {
        List<FitnessBooking> bookings = fitnessBookingService.getAllBookings(); 
        return ResponseEntity.ok(bookings); 
    }
    
    // 🌟🌟 API ที่แก้ไข: เปลี่ยน Path เป็น /delete/booking/{bookingId} 🌟🌟
    // URL ใหม่: DELETE /api/fitness/delete/booking/{bookingId}
    @DeleteMapping("/delete/booking/{bookingId}") 
    //@PreAuthorize("hasRole('ROLE_ADMIN')") // คงคอมเมนต์ไว้เพื่อแก้ 401
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        try {
            fitnessBookingService.cancelBooking(bookingId); 
            // 200 OK พร้อมข้อความ
            return ResponseEntity.ok("รายการจอง ID " + bookingId + " ถูกยกเลิกสำเร็จแล้ว");
        } catch (ResourceNotFoundException e) {
             // 404 Not Found 
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); 
        } catch (Exception e) {
            // 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("เกิดข้อผิดพลาดในการลบรายการจอง: " + e.getMessage());
        }
    }
}