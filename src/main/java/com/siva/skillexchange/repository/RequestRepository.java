package com.siva.skillexchange.repository;

import com.siva.skillexchange.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}