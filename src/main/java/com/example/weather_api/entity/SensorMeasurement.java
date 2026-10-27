package com.example.weather_api.entity;

import jakarta.persistence.*;

import java.lang.annotation.Inherited;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

@Entity //tells spring this is an entity
@Table(name = "sensor_measurements") // creates db table name
public class SensorMeasurement{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)// this along with @Id generate a unique id for each entry
    private Long id;

    @Column(nullable = false)
    private String sensorId; // set the id for a sensor

    @Column(nullable = false)
    private String metric; // sets the type for the measurement reading

    @Column(nullable = false)
    private double value; // sets the value for each reading

    @Column(nullable = false)
    private LocalDateTime time; // records the time of each reading

    public SensorMeasurement(){

    }

    public SensorMeasurement(String sensorId, String metric,
                             double value, LocalDateTime time){
        this.sensorId = sensorId;
        this.metric = metric;
        this.value = value;
        this.time = time;

    }
    //getter for id
    public Long getId(){
        return id;
    }
    //getter for sensor id
    public String getSensorId(){
        return sensorId;
    }
    //getter for metric type
    public String getMetric(){
        return metric;
    }
    //getter for value
    public double getVal(){
        return value;
    }
    //getter for time
    public LocalDateTime getTime(){
        return time;
    }



}