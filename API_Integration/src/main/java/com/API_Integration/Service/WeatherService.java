package com.API_Integration.Service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.API_Integration.Entity.WeatherRecord;
import com.API_Integration.Repository.WeatherRecordRepository;

@Service
public class WeatherService {
	private final WeatherRecordRepository weatherRecordRepository;

	// Inject API configuration from properties file
	@Value("${external.api.url}")
	private String apiUrl;

	@Value("${external.api.key}")
	private String apiKey;

	@Autowired
	public WeatherService(WeatherRecordRepository weatherRecordRepository) {
		this.weatherRecordRepository = weatherRecordRepository;
	}

	// Method to fetch weather data from an external API and save it to the database
	public WeatherRecord fetchAndSaveWeatherData(String city) {
		RestTemplate restTemplate = new RestTemplate();

		// Build the API URL with query parameters
		String url = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("q", city).queryParam("appid", apiKey)
				.queryParam("units", "metric") // Optional: for Celsius temperature
				.toUriString();

		// Make the API request and parse the response
		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Map<String, Object> body = response.getBody();

			// Extract necessary data from API response
			Map<String, Object> main = (Map<String, Object>) body.get("main");
			String description = (String) ((Map<String, Object>) ((Map<String, Object>) body.get("weather")).get(0))
					.get("description");

			double temperature = (Double) main.get("temp");

			// Create a new WeatherRecord object
			WeatherRecord weatherRecord = new WeatherRecord();
			weatherRecord.setCity(city);
			weatherRecord.setTemperature(temperature);
			weatherRecord.setDescription(description);
			weatherRecord.setTimestamp(LocalDateTime.now());

			// Save the record in the database
			return weatherRecordRepository.save(weatherRecord);
		} else {
			throw new RuntimeException("Failed to fetch weather data from the external API");
		}
	}
}
