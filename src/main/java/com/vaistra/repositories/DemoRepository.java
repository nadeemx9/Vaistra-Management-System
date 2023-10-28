package com.vaistra.repositories;

import com.vaistra.entities.DemoCSV;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface DemoRepository extends JpaRepository<DemoCSV,Long> {
//        List<DemoCSV> findByDateBetween(LocalDate of, LocalDate of1);
//        Page<DemoCSV> findByDateBetween(String s, String s1, Pageable pageable);

//        @Query("SELECT CONCAT(d.date, ' ', d.time) AS minuteTimestamp FROM DemoCSV d " +
//                "WHERE (d.date <= '2000-12-31' AND d.time <= '23:59:00') " +
//                "AND (d.date >= '1990-01-01' AND d.time >= '00:00:00') " +
//                "ORDER BY minuteTimestamp")

//        @Query("SELECT CONCAT(d.date, ' ', d.time) AS minuteTimestamp FROM DemoCSV d " +
//                "WHERE (d.date <= '2000-12-31' AND (d.date || ' ' || d.time) <= '2000-12-31 23:59:00') " +
//                "AND (d.date >= '1990-01-01' AND (d.date || ' ' || d.time) >= '1990-01-01 00:00: 00') " +
//                "ORDER BY minuteTimestamp")

        @Query("SELECT d from DemoCSV d WHERE d.id in" +
               "(SELECT d1.id from DemoCSV d1 WHERE d1.date >= ?1 AND d1.date <= ?2)" +
               "ORDER BY d.date,d.time")
        Page<DemoCSV> findMinuteTimestamps(LocalDate d1,LocalDate d2,Pageable pageable);

        Page<DemoCSV> findByDateBetween(LocalDate date1, LocalDate date2, Pageable pageable);
}