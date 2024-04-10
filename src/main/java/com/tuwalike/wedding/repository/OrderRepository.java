package com.tuwalike.wedding.repository;

import com.tuwalike.wedding.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
