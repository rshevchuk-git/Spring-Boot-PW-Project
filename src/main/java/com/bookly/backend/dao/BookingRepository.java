package com.bookly.backend.dao;

import com.bookly.backend.models.Booking;
import com.bookly.backend.models.ItemType;
import com.bookly.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE (:isActive is null or b.isActive = :isActive) and " +
            "(:car is null and :parking is null and :room is null or b.itemType in (:car, :parking , :room)) and " +
            "(:owner is null or b.owner = :owner)")
    Page<Booking> findAllByIsActiveAndItemTypeAndOwner(@Param("isActive") Boolean isActive,
                                                       @Param("car") ItemType car,
                                                       @Param("parking") ItemType parking,
                                                       @Param("room") ItemType room,
                                                       @Param("owner") User user, Pageable pageable);
}
