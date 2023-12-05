package com.volvo.congestiontax.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class TaxRule {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "city")
        private String city;

        @Column(name = "max_amount_per_day")
        private int maxAmountPerDay;

        @Column(name = "is_free_on_weekend")
        private boolean isFreeOnWeekend;


        @CollectionTable(name = "tax_free_dates")
        private List<String> taxFreeDates;

        @CollectionTable(name = "toll_time_fees")
        @OneToMany(cascade = CascadeType.ALL)
        private List<TollTimeFee> tollTimeFees;

}
