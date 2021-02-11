package com.bookly.backend.services;

import com.bookly.backend.dao.BookingRepository;
import com.bookly.backend.dao.ItemRepository;
import com.bookly.backend.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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

    private final String CARLY_BOOKINGS = "http://52.3.250.46:5050/public/bookings";
    private final String CARLY_TOKEN = "?apiKey=BTKJPMKRP";
    private final String PARKLY_API = "http://parkly-env.eba-u2qumtf7.us-east-2.elasticbeanstalk.com/b/bookings";
    private final String PARKLY_TOKEN = "?apiKey=1AC4FCOPR";
    private final String FLATLY_API = "http://flatly-env.eba-pftr9jj2.eu-central-1.elasticbeanstalk.com/ext/bookings";
    private final String FLATLY_TOKEN = "?apiKey=savekey";

    private UserService userService;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;
    private BookingService bookingService;

    private final Map<ItemType, Function<Reservation, Boolean>> reservationByTypes = new HashMap<>(){{
           put(ItemType.Car, ReservationService.this::carlyReservation);
           put(ItemType.Parking, ReservationService.this::parklyReservation);
           put(ItemType.Room, ReservationService.this::flatlyReservation);
    }};

    private final Map<ItemType, Function<Booking, Boolean>> cancellationsByTypes = new HashMap<>(){{
        put(ItemType.Car, ReservationService.this::carlyCancellation);
        put(ItemType.Parking, ReservationService.this::parklyCancellation);
        put(ItemType.Room, ReservationService.this::flatlyCancellation);
    }};

    public Boolean makeReservation(Reservation reservation) {
        return reservationByTypes.get(reservation.getItemType()).apply(reservation);
    }

    @Transactional
    public void removeReservation(Long bookingId) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            cancellationsByTypes.get(booking.getItemType()).apply(booking);
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
            ObjectMapper mapper = new ObjectMapper();
            try {
                Item newItem = itemRepository.save(new Item(0L, response.getBody().getId(), ItemType.Car.toString(), response.getBody().getStartDateTime(), response.getBody().getEndDateTime(), true, mapper.writeValueAsString(response.getBody())));
                bookingRepository.save(new Booking((long) response.getBody().getId(), loggedUser, newItem.getStartDateTime(), true, newItem, ItemType.Car));
                return true;
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean carlyCancellation(Booking booking) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(CARLY_BOOKINGS + "/" + booking.getItem().getItemId() + CARLY_TOKEN);
        return true;
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
        ResponseEntity<ParklyResponseObject> response = restTemplate.postForEntity(PARKLY_API + PARKLY_TOKEN, parklyReservationObject, ParklyResponseObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Item newItem = itemRepository.save(new Item(0L, response.getBody().getId(), ItemType.Parking.toString(), reservation.getFromDate(), reservation.getToDate(), true, mapper.writeValueAsString(response.getBody())));
                bookingRepository.save(new Booking(0L, loggedUser, newItem.getStartDateTime(), true, newItem, ItemType.Parking));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean parklyCancellation(Booking booking) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(PARKLY_API + "/" + booking.getItem().getItemId() + PARKLY_TOKEN);
        return true;
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

        ResponseEntity<FlatlyResponseObject> response = restTemplate.postForEntity(FLATLY_API + FLATLY_TOKEN, flatlyReservationObject, FlatlyResponseObject.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Item newItem = itemRepository.save(new Item(0L, response.getBody().getId(), ItemType.Room.toString(), reservation.getFromDate(), reservation.getToDate(), true, mapper.writeValueAsString(response.getBody())));
                bookingRepository.save(new Booking(0L, loggedUser, newItem.getStartDateTime(), true, newItem, ItemType.Room));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean flatlyCancellation(Booking booking) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(FLATLY_API + "/" + booking.getItem().getItemId() + FLATLY_TOKEN);
        return true;
    }
}
