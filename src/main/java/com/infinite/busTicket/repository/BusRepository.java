package com.infinite.busTicket.repository;

import com.infinite.busTicket.entity.BusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<BusEntity,Long> {
    List<BusEntity> findBySource(String source);
    List<BusEntity> findByDestination(String destination);
    List<BusEntity> findByDateOfJourney(LocalDate dateOfJourney);
}
