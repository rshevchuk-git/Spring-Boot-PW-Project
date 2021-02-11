package com.bookly.backend.services;

import com.bookly.backend.dao.BookingRepository;
import com.bookly.backend.dao.ItemRepository;
import com.bookly.backend.dao.UserRepository;
import com.bookly.backend.models.Booking;
import com.bookly.backend.models.ItemType;
import com.bookly.backend.models.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class BookingService {

    private final int PAGE_SIZE = 5;

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public Booking saveBooking(Booking newBooking) {
        return bookingRepository.save(newBooking);
    }

    public Page<Booking> filterAllBookings(String status, String type, String owner, int page, String sort, String sortOrder) {
        Boolean isActive = !status.equals("") ? status.equals("active") : null;
        ItemType car = type.toLowerCase().contains("car") ? ItemType.Car : null;
        ItemType parking = type.toLowerCase().contains("parking") ? ItemType.Parking : null;
        ItemType room = type.toLowerCase().contains("room") ? ItemType.Room : null;
        User user = !owner.equals("") ? userRepository.getByLogin(owner).orElse(null) : null;
        Sort.Direction sortDir = sortOrder.toLowerCase().equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageConfig;
        if (!sort.equals("empty")) {
            pageConfig = PageRequest.of(page, PAGE_SIZE, Sort.by(sortDir, sort));
        } else {
            pageConfig = PageRequest.of(page, PAGE_SIZE);
        }

        return bookingRepository.findAllByIsActiveAndItemTypeAndOwner(isActive, car, parking, room, user, pageConfig);
    }

    public void deleteBookingById(Long bookingId) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            Long id = booking.getItem().getId();
            itemRepository.deleteById(id);
        });
    }
}
