package com.volvo.congestiontax.service;


import com.volvo.congestiontax.model.*;
import com.volvo.congestiontax.repository.TaxRuleRepository;
import com.volvo.congestiontax.service.testData.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class CongestionTaxServiceTest {




    TaxRuleRepository mockTaxRuleRepository;
    CongestionTaxService congestionTaxService;

    @BeforeEach
    void setUp() {
        mockTaxRuleRepository = mock(TaxRuleRepository.class);
        congestionTaxService = new CongestionTaxServiceImplementation(mockTaxRuleRepository);
        Mockito.when(mockTaxRuleRepository.findByCity(Mockito.anyString())).thenReturn(Optional.of(TestData.GetTaxRule()));

    }

    @Test
    void  test_vehicle_toll_free_day_Sunday_should_return_0() {
        Vehicle car = new Car("car","XWA112");
        TollEntry tollEntry = new TollEntry("2013-02-09 08:35:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(0, taxFee);
    }

    @Test
    void  test_vehicle_toll_free_holiday_should_return_0 () {
        Vehicle car = new Car("car","XWA112");

        TollEntry tollEntry = new TollEntry("2013-03-28 18:35:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(0, taxFee);
    }

    @Test
    void  test_vehicle_toll_free_month_should_return_0() {
        Vehicle car = new Car("car","XWA112");
        TollEntry tollEntry = new TollEntry("2013-07-18 08:00:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(0, taxFee);
    }


    @Test
    void  test_toll_free_vehicle_return_0() {
        Vehicle car = new Car("car","XWA112");

        TollEntry tollEntry = new TollEntry("2013-07-18 08:00:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(0, taxFee);
    }

    @Test
    void  test_vehicle_Car_should_return_8_if_time_is_between_08_14() {
        Vehicle car = new Car("car","XWA112");
        TollEntry tollEntry = new TollEntry("2013-08-19 09:48:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(8, taxFee);
    }



    @Test
    void  test_vehicle_Car_should_return_18_if_time_is_between_15_30_16_59() {
        Vehicle car = new Car("car","XWA112");
        TollEntry tollEntry = new TollEntry("2013-08-19 15:48:00", car);
        Toll toll  = new Toll("Gothenburg", List.of(tollEntry));
        int taxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(18, taxFee);
    }


    @Test
    void test_vehicle_that_goes_through_multipule_tolls_under_60Min_should_taxed_once_highest() {
        Vehicle car = new Car("car","XWA112");

        TollEntry toll1 = new TollEntry("2013-08-19 08:20:00", car);
        TollEntry toll2 = new TollEntry("2013-08-19 08:45:00", car);
        TollEntry toll3 = new TollEntry("2013-08-19 09:15:00", car);
        List<TollEntry> tollEntryList = Arrays.asList(toll1, toll2,toll3);


        Toll toll  = new Toll("Gothenburg", tollEntryList);
        int totaltaxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(13, totaltaxFee);
    }

    @Test
    void test_multiple_vehicle_that_goes_through_multiple_tolls_under_60Min_Should_taxed_once() {
        Vehicle car = new Car("car","XWA112");

        Vehicle car2 = new Car("car","XWA115");


        TollEntry toll1 = new TollEntry("2013-08-19 08:35:00", car);
        TollEntry toll2 = new TollEntry("2013-08-19 08:45:00", car);
        TollEntry toll3 = new TollEntry("2013-08-19 09:15:00", car);
        TollEntry tollcar2 = new TollEntry("2013-08-19 08:20:00", car2);
        TollEntry toll2car2 = new TollEntry("2013-08-19 08:45:00", car2);
        TollEntry toll3car3 = new TollEntry("2013-08-19 09:15:00", car2);
        List<TollEntry> tollEntryList = Arrays.asList(toll1,
                toll2,toll3 ,tollcar2,toll2car2 ,toll3car3);

        Toll toll  = new Toll("Gothenburg", tollEntryList);
        int taxFeeCar1 = congestionTaxService.getTax(toll).get(0).fee();
        int taxFeeCar2 = congestionTaxService.getTax(toll).get(1).fee();

        assertEquals(13, taxFeeCar1);
        assertEquals(8, taxFeeCar2);

    }

    @Test
    void test_vehicle_that_goes_through_multipule_tolls_under_sameday_should_taxed_max_60() {
        Vehicle car = new Car("car","XWA112");

        TollEntry toll1 = new TollEntry("2013-08-19 07:20:00", car);
        TollEntry toll2 = new TollEntry("2013-08-19 08:25:00", car);
        TollEntry toll3 = new TollEntry("2013-08-19 14:45:00", car);
        TollEntry toll4 = new TollEntry("2013-08-19 15:45:00", car);
        TollEntry toll5 = new TollEntry("2013-08-19 16:47:00", car);
        TollEntry toll6 = new TollEntry("2013-08-19 18:15:00", car);
        List<TollEntry> tollEntryList = Arrays.asList(toll1, toll2,toll3,toll4,toll5,toll6);


        Toll toll  = new Toll("Gothenburg", tollEntryList);
        int totaltaxFee = congestionTaxService.getTax(toll).get(0).fee();
        assertEquals(60, totaltaxFee);
    }


    @Test
    void test_same_vehicle_that_goes_through_multiple_tolls_under_different_days() {
        Vehicle car = new Car("car","XWA112");


        TollEntry toll1Day1 = new TollEntry("2013-08-19 08:35:00", car);
        TollEntry toll2Day1 = new TollEntry("2013-08-19 08:45:00", car);
        TollEntry toll3Day1 = new TollEntry("2013-08-19 09:15:00", car);
        TollEntry toll1Day2 = new TollEntry("2013-08-20 08:35:00", car);
        TollEntry toll2Day2 = new TollEntry("2013-08-20 08:45:00", car);
        TollEntry toll3Day2 = new TollEntry("2013-08-20 09:15:00", car);

        List<TollEntry> tollEntryList = Arrays.asList( toll1Day1, toll2Day1, toll3Day1, toll1Day2, toll2Day2,toll3Day2);

        Toll toll  = new Toll("Gothenburg", tollEntryList);
        int totaltaxFee = congestionTaxService.getTax(toll).get(0).fee();
        int totaltaxFee2 = congestionTaxService.getTax(toll).get(1).fee();


        assertEquals(8, totaltaxFee);
        assertEquals(8, totaltaxFee2);

    }

}
