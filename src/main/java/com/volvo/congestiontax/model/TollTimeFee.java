package com.volvo.congestiontax.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class TollTimeFee {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String startTime;
    private String endTime;
    private int fee;
}
