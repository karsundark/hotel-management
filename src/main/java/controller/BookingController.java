package controller;

import model.Booking;
import service.BookingService;
import security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public BookingController(BookingService bookingService, UserService userService, JwtUtil jwtUtil) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBooking(@RequestHeader("Authorization") String token, @RequestBody Booking booking) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Optional<?> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(403).body("Access Denied: You must be a registered user to add a booking.");
        }

        return ResponseEntity.ok(bookingService.addBooking(booking));
    }
}
