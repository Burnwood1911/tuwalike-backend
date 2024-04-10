package com.tuwalike.wedding.repository;

import com.tuwalike.wedding.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
