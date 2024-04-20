package com.tuwalike.wedding.repository;

import com.tuwalike.wedding.entity.Guest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {

    Optional<Guest> findByQr(String qr);

    List<Guest> findAllByEventId(int eventId);

}
