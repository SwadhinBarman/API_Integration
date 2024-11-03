package com.API_Integration.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API_Integration.Entity.WeatherRecord;
import com.API_Integration.Service.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // GET request to fetch weather data and save it to the database
    @GetMapping("/fetch-and-save")
    public ResponseEntity<WeatherRecord> fetchAndSaveWeatherData(@RequestParam String city) {
        WeatherRecord savedRecord = weatherService.fetchAndSaveWeatherData(city);
        return ResponseEntity.ok(savedRecord);
    }
}
