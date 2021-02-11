package com.bookly.backend.services;

import com.bookly.backend.dao.BookingRepository;
import com.bookly.backend.dao.ItemRepository;
import com.bookly.backend.models.*;
import com.bookly.backend.models.carly.CarlyReservationObject;
import com.bookly.backend.models.carly.CarlyResponseObject;
import com.bookly.backend.models.flatly.FlatlyReservationObject;
import com.bookly.backend.models.flatly.FlatlyResponseObject;
import com.bookly.backend.models.parkly.ParklyReservationObject;
import com.bookly.backend.models.parkly.ParklyResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ReservationService {

    @Value(value = "carly.bookings.url")
    private String CARLY_BOOKINGS;
    @Value(value = "carly.bookings.token")
    private String CARLY_TOKEN;
    @Value(value = "parkly.bookings.url")
    private String PARKLY_BOOKINGS;
    @Value(value = "parkly.bookings.token")
    private String PARKLY_TOKEN;
    @Value(value = "flatly.bookings.url")
    private String FLATLY_BOOKINGS;
    @Value(value = "flatly.bookings.token")
    private String FLATLY_TOKEN;

    private UserService userService;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    private final Map<ItemType, Function<Reservation, Boolean>> reservationByTypes = new HashMap<>() {{
        put(ItemType.Car, ReservationService.this::carlyReservation);
        put(ItemType.Parking, ReservationService.this::parklyReservation);
        put(ItemType.Room, ReservationService.this::flatlyReservation);
    }};

    private final Map<ItemType, String[]> cancellationsByTypes = new HashMap<>() {{
        put(ItemType.Car, new String[]{CARLY_BOOKINGS, CARLY_TOKEN});
        put(ItemType.Parking, new String[]{PARKLY_BOOKINGS, PARKLY_TOKEN});
        put(ItemType.Room, new String[]{FLATLY_BOOKINGS, FLATLY_TOKEN});
    }};

    public Boolean makeReservation(Reservation reservation) {
        return reservationByTypes.get(reservation.getItemType()).apply(reservation);
    }

    @Transactional
    public void removeReservation(Long bookingId) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            String[] API = cancellationsByTypes.get(booking.getItemType());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(API[0] + "/" + booking.getItem().getItemId() + API[1]);

            booking.setActive(false);
            booking.getItem().setActive(false);
        });
    }

    private boolean carlyReservation(Reservation reservation) {
        RestTemplate restTemplate = new RestTemplate();
        User loggedUser = userService.getUserById(reservation.getUserId());
        CarlyReservationObject carlyReservationObject = new CarlyReservationObject();
        carlyReservationObject.setCarId(reservation.getItemId());
        carlyReservationObject.setTenantId(reservation.getUserId());
        carlyReservationObject.setTenantFirstname(loggedUser.getFirstName());
        carlyReservationObject.setTenantSurname(loggedUser.getLastName());
        carlyReservationObject.setStartDateTime(reservation.getFromDate());
        carlyReservationObject.setEndDateTime(reservation.getToDate());
        ResponseEntity<CarlyResponseObject> response = restTemplate.postForEntity(CARLY_BOOKINGS + CARLY_TOKEN, carlyReservationObject, CarlyResponseObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return addNewBooking(reservation, loggedUser, response, response.getBody().getId());
        }
        return false;
    }

    private boolean parklyReservation(Reservation reservation) {
        RestTemplate restTemplate = new RestTemplate();
        User loggedUser = userService.getUserById(reservation.getUserId());
        ParklyReservationObject parklyReservationObject = new ParklyReservationObject();
        parklyReservationObject.setUserId(reservation.getUserId());
        parklyReservationObject.setParkingId(reservation.getItemId());
        parklyReservationObject.setUserFirstName(loggedUser.getFirstName());
        parklyReservationObject.setUserLastName(loggedUser.getLastName());
        parklyReservationObject.setStartDateTime(reservation.getFromDate());
        parklyReservationObject.setEndDateTime(reservation.getToDate());
        ResponseEntity<ParklyResponseObject> response = restTemplate.postForEntity(PARKLY_BOOKINGS + PARKLY_TOKEN, parklyReservationObject, ParklyResponseObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return addNewBooking(reservation, loggedUser, response, response.getBody().getId());
        }
        return false;
    }

    private boolean flatlyReservation(Reservation reservation) {
        RestTemplate restTemplate = new RestTemplate();
        User loggedUser = userService.getUserById(reservation.getUserId());
        FlatlyReservationObject flatlyReservationObject = new FlatlyReservationObject();
        flatlyReservationObject.setCustomerId(Math.toIntExact(reservation.getUserId()));
        flatlyReservationObject.setFlatId(Math.toIntExact(reservation.getItemId()));
        flatlyReservationObject.setNoOfGuests(1);
        flatlyReservationObject.setStartDate(reservation.getFromDate().toLocalDate());
        flatlyReservationObject.setEndDate(reservation.getToDate().toLocalDate());
        ResponseEntity<FlatlyResponseObject> response = restTemplate.postForEntity(FLATLY_BOOKINGS + FLATLY_TOKEN, flatlyReservationObject, FlatlyResponseObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return addNewBooking(reservation, loggedUser, response, response.getBody().getId());
        }
        return false;
    }

    private boolean addNewBooking(Reservation reservation, User user, ResponseEntity<?> response, int itemId) {
        ObjectMapper mapper = new ObjectMapper();
        Item newItem;
        try {
            newItem = itemRepository.save(new Item(0L, itemId, reservation.getItemType().toString(), reservation.getFromDate(), reservation.getToDate(), true, mapper.writeValueAsString(response.getBody())));
            bookingRepository.save(new Booking(0L, user, newItem.getStartDateTime(), true, newItem, reservation.getItemType()));
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
