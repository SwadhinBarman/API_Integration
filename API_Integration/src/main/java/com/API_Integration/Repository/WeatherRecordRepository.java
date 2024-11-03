package com.API_Integration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.API_Integration.Entity.WeatherRecord;

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

}
