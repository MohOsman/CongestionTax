package com.volvo.congestiontax.service;

import com.volvo.congestiontax.execption.TaxRuleAlreadyExist;
import com.volvo.congestiontax.execption.TaxRuleNotExistException;
import com.volvo.congestiontax.model.*;
import com.volvo.congestiontax.repository.TaxRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CongestionTaxServiceImplementation implements CongestionTaxService {

    private Logger log = LoggerFactory.getLogger(CongestionTaxService.class);


    private TaxRuleRepository taxRuleRepository;

    private TaxRule taxRule;

    @Autowired
    public CongestionTaxServiceImplementation(TaxRuleRepository taxRuleRepository) {
        this.taxRuleRepository = taxRuleRepository;
    }

    @Override
    public List<TollFee> getTax(Toll toll) throws TaxRuleNotExistException {
        this.taxRule = getTaxRuleByCity(toll.city());
        List<TollFee> vehicaleTotalList= new ArrayList<>();
        log.info("Grouping the list by date and registrationNumbers  ");
        Map<LocalDate, Map<String, List<TollEntry>>> entriesByDateAndRegNr = getVehiclesGroupedByDateAndRegNr(toll.tollEntryList());

        for (Map.Entry<LocalDate, Map<String, List<TollEntry>>> dateEntry : entriesByDateAndRegNr.entrySet()) {
            Map<String, List<TollEntry>> regNrEntries = dateEntry.getValue();

            for (Map.Entry<String, List<TollEntry>> regNrEntry : regNrEntries.entrySet()) {
                List<TollEntry> entries = regNrEntry.getValue();

                TollFee tollFee = new TollFee(entries.get(0).vehicle(), countTollForMultipleEntriesForSameVehicle(entries),
                        entries.stream().map(TollEntry::date).toList());
                log.info("Vehicle of type {} with registrationNumber {}, fee amount {} ",
                        tollFee.vehicle().getType(), tollFee.vehicle().getRegistrationNumber(), tollFee.fee() );
                vehicaleTotalList.add(tollFee);
            }
        }

        return vehicaleTotalList;
    }

    @Override
    public TaxRule createTaxRule(TaxRule taxRule) throws TaxRuleAlreadyExist {
       if(this.taxRuleRepository.findByCity(taxRule.getCity()).isEmpty()){
           log.info("Adding new tax rule for {}", taxRule.getCity());
           return this.taxRuleRepository.save(taxRule);

       }
       throw new TaxRuleAlreadyExist();
    }

    @Override
    public TaxRule getTaxRuleByCity(String city) {
        log.info("Fetching tax Rule for  {}", city);
        TaxRule taxRule1 = taxRuleRepository.findByCity(city)
              .orElseThrow(() -> new TaxRuleNotExistException("Tax rule does not exist for this city"));
        return taxRule1;
    }

    @Override
    public void deleteTaxRuleByCity(String city) {
        this.taxRuleRepository.deleteByCity(city);
    }

    private static Map<LocalDate, Map<String, List<TollEntry>>> getVehiclesGroupedByDateAndRegNr(List<TollEntry> tollEntryList) {
    return tollEntryList.stream()
                .collect(Collectors.groupingBy(tollEntry -> Util.GetLocalDateTimeFromString(tollEntry.date()).toLocalDate(),
                 Collectors.groupingBy(tollEntry-> tollEntry.vehicle().getRegistrationNumber())));
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        return Arrays.stream(TollFreeVehicles.values()).anyMatch(freeVehicle ->
                vehicle.getType().equalsIgnoreCase(freeVehicle.name()));
    }



    private int countTollForMultipleEntriesForSameVehicle(List<TollEntry> tollEntryList) {
        int totalFee = 0;
        LocalDateTime firstEntry = Util.GetLocalDateTimeFromString(tollEntryList.get(0).date());
        for (TollEntry entry : tollEntryList) {
            final LocalDateTime localDateTime = Util.GetLocalDateTimeFromString(entry.date());
            int nextFee = getTollFee(localDateTime, entry.vehicle());
            int tempFee = getTollFee(firstEntry, entry.vehicle());
            final Duration duration = Duration.between(firstEntry, localDateTime);
            if (duration.toMinutes() <= this.taxRule.getMaxAmountPerDay()) {
                if (totalFee > 0)
                    totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }

        }
        return Math.min(totalFee, taxRule.getMaxAmountPerDay());

    }
    private int getTollFee(LocalDateTime localDateTime, Vehicle vehicle) {
        final int hour = localDateTime.getHour();
        final int minutes = localDateTime.getMinute();
        if (isTollFreeDay(localDateTime) || isTollFreeVehicle(vehicle) )  {
            return 0;
        } else {
            return  getHourlyTaxFeeMap(hour,minutes);
        }
    }


    private int getHourlyTaxFeeMap(int hour, int minute) {
      List<TollTimeFee> tollTimeFeeList = this.taxRule.getTollTimeFees();
        for (TollTimeFee tollTimeFee:  tollTimeFeeList) {
            String [] starthourAndMinute  = tollTimeFee.getStartTime().split(":");
            String [] endHourAndMinut = tollTimeFee.getEndTime().split(":");
            int startHour  = Integer.parseInt(starthourAndMinute[0]);
            int startMinute  = Integer.parseInt(starthourAndMinute[1]);
            int endHour  = Integer.parseInt(endHourAndMinut[0]);
            int endMinute  = Integer.parseInt(endHourAndMinut[1]);

            if (isInInterval(hour, minute, startHour, startMinute, endHour, endMinute ) ) {
                return tollTimeFee.getFee();
            }

        }
        return 0;
    }


    private boolean isInInterval(int enterHour, int enterMinute ,int startHour, int startMinute,
                                 int endHour, int endMinute) {
        return (enterHour >= startHour && enterMinute >= startMinute && enterHour <= endHour && enterMinute <= endMinute);
    }


    private boolean isTollFreeDay(LocalDateTime localDateTime) {
        final DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        final int month = localDateTime.getMonthValue();
        final int day = localDateTime.getDayOfMonth();
        final int year = localDateTime.getYear();

        return (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) ||
                (year == 2013 && istollFreeHoliday(month, day));
    }

    private  boolean istollFreeHoliday(int month, int day) {
        List<String> tollfreedates = this.taxRule.getTaxFreeDates();
        return (tollfreedates.contains(month + "-" + day) || tollfreedates.contains(String.valueOf(month)));
    }
}
