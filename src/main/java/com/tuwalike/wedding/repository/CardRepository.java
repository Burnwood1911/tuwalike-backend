package com.tuwalike.wedding.repository;

import com.tuwalike.wedding.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
