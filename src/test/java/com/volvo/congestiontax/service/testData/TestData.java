package com.volvo.congestiontax.service.testData;

import com.volvo.congestiontax.model.TaxRule;
import com.volvo.congestiontax.model.TollTimeFee;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static TaxRule GetTaxRule(){
        TaxRule taxRule = new TaxRule();
        taxRule.setId(1L);
        taxRule.setCity("Gothenburg");
        taxRule.setMaxAmountPerDay(60);
        taxRule.setFreeOnWeekend(true);
        taxRule.setTaxFreeDates(List.of("1-1", "3-28",
                "3-29",
                "4-1",
                "4-30",
                "5-01",
                "5-8",
                "5-9",
                "6-5",
                "6-6",
                "6-21",
                "7",
                "11-1",
                "12-24",
                "12-25",
                "12-26", "12-31"));

        List<TollTimeFee> tollTimeFeeList = new ArrayList<>();

        // taxTime 1
        TollTimeFee taxTime1 = new TollTimeFee();
        taxTime1.setStartTime("6:29");
        taxTime1.setEndTime("6:59");
        taxTime1.setFee(8);
        tollTimeFeeList.add(taxTime1);

        // taxTime 2
        TollTimeFee taxTime2 = new TollTimeFee();
        taxTime2.setStartTime("7:0");
        taxTime2.setEndTime("7:59");
        taxTime2.setFee(18);
        tollTimeFeeList.add(taxTime2);

        // taxTime 3
        TollTimeFee taxTime3 = new TollTimeFee();
        taxTime3.setStartTime("8:0");
        taxTime3.setEndTime("8:30");
        taxTime3.setFee(13);
        tollTimeFeeList.add(taxTime3);

        // taxTime 4
        TollTimeFee taxTime4 = new TollTimeFee();
        taxTime4.setStartTime("8:30");
        taxTime4.setEndTime("14:59");
        taxTime4.setFee(8);
        tollTimeFeeList.add(taxTime4);

        // taxTime 5
        TollTimeFee taxTime5 = new TollTimeFee();
        taxTime5.setStartTime("15:0");
        taxTime5.setEndTime("15:29");
        taxTime5.setFee(13);
        tollTimeFeeList.add(taxTime5);

        // taxTime 6
        TollTimeFee taxTime6 = new TollTimeFee();
        taxTime6.setStartTime("15:30");
        taxTime6.setEndTime("16:59");
        taxTime6.setFee(18);
        tollTimeFeeList.add(taxTime6);

        // taxTime 7
        TollTimeFee taxTime7 = new TollTimeFee();
        taxTime7.setStartTime("18:0");
        taxTime7.setEndTime("18:29");
        taxTime7.setFee(8);
        tollTimeFeeList.add(taxTime7);

        // taxTime 8
        TollTimeFee taxTime8 = new TollTimeFee();
        taxTime8.setStartTime("18:30");
        taxTime8.setEndTime("5:59");
        taxTime8.setFee(0);
        tollTimeFeeList.add(taxTime8);

        taxRule.setTollTimeFees(tollTimeFeeList);
        return taxRule;
    }
}
