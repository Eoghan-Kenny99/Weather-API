
package com.example.weather_api.service;

import com.example.weather_api.entity.SensorMeasurement;
import com.example.weather_api.repository.SensorMeasurementRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SensorMeasurementServiceTest {

    private final SensorMeasurementRepository repository =
            mock(SensorMeasurementRepository.class);

    private final SensorMeasurementService service =
            new SensorMeasurementService(repository);

    @Test
    void calculateAverageReturnsCorrectValue() {
        // create two sensor readings
        SensorMeasurement first = new SensorMeasurement(
                "sensor-1",
                "TEMPERATURE",
                21.5,
                LocalDateTime.parse("2026-10-22T10:30:00")
        );

        SensorMeasurement second = new SensorMeasurement(
                "sensor-1",
                "TEMPERATURE",
                25.5,
                LocalDateTime.parse("2026-10-22T11:30:00")
        );

        List<SensorMeasurement> measurements = List.of(first, second);

        // calculate the average
        double result = service.calculateStatistics(
                measurements, "AVERAGE");

        // verify the expected result
        assertEquals(23.5, result, 0.001);
    }

    
    @Test
    void calculateStatisticsThrowsExceptionWhenNoMeasurementsExist() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateStatistics(List.of(), "AVERAGE")
        );

        assertEquals("Measurement not found", exception.getMessage());
}
}

