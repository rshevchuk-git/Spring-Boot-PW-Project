package com.bookly.backend.controllers;

import com.bookly.backend.models.Booking;
import com.bookly.backend.models.Reservation;
import com.bookly.backend.services.BookingService;
import com.bookly.backend.services.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Bookings Management")
@RestController
@AllArgsConstructor
@RequestMapping("/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ReservationService reservationService;

    @GetMapping
    @ApiOperation(value = "Fetches all the bookings with possibility to add a filter")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Page<Booking>> getBookings(@RequestParam(required = false, defaultValue = "") String status,
                                                     @RequestParam(required = false, defaultValue = "") String type,
                                                     @RequestParam(required = false, defaultValue = "") String owner,
                                                     @RequestParam(required = false, defaultValue = "asc") String sortOrder,
                                                     @RequestParam(required = false, defaultValue = "empty") String sort,
                                                     @RequestParam(required = false, defaultValue = "0") Integer page) {
        Page<Booking> responseBookingList = bookingService.filterAllBookings(status, type, owner, page, sort, sortOrder);
        return new ResponseEntity<>(responseBookingList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieves the booking with specified ID if one exists")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Booking> getBookingById(@PathVariable(name = "id") Long bookingId) {
        return bookingService.getBookingById(bookingId)
                .map(booking  -> new ResponseEntity<>(booking, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiOperation(value = "Adds new booking to the database")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Booking> addNewBooking(@RequestBody Booking newBooking) {
        return new ResponseEntity<>(bookingService.saveBooking(newBooking), HttpStatus.OK);
    }

    @PostMapping("/reservations")
    @ApiOperation(value = "Make reservation of some item")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Boolean> makeReservation(@RequestBody Reservation reservation) {
        if (reservation.getFromDate() == null || reservation.getToDate() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (reservation.getItemId() == null || reservation.getItemId() < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (reservation.getUserId() == null || reservation.getUserId() < 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (reservation.getItemType() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(reservationService.makeReservation(reservation), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates specific booking if one exists")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<Booking> updateBooking(@PathVariable(name = "id") Long bookingId,
                                                 @RequestBody Booking targetBooking) {
        return bookingService.getBookingById(bookingId)
                .map(booking  -> new ResponseEntity<>(bookingService.saveBooking(targetBooking), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes specific booking from the database")
    @ApiImplicitParam(name = "Security-Token", paramType = "header", dataTypeClass = String.class)
    public ResponseEntity<String> deleteBooking(@PathVariable(name = "id") Long bookingId) {
        reservationService.removeReservation(bookingId);
        return new ResponseEntity<>("Booking deleted", HttpStatus.OK);
    }
}
