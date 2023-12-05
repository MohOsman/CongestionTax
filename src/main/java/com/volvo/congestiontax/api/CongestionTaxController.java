package com.volvo.congestiontax.api;

import com.volvo.congestiontax.execption.TaxRuleAlreadyExist;
import com.volvo.congestiontax.execption.TaxRuleNotExistException;
import com.volvo.congestiontax.model.TaxRule;
import com.volvo.congestiontax.model.Toll;
import com.volvo.congestiontax.model.TollEntry;
import com.volvo.congestiontax.model.TollFee;
import com.volvo.congestiontax.service.CongestionTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.java.JavaLoggingSystem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CongestionTaxController {
    private final Logger log = LoggerFactory.getLogger(CongestionTaxController.class);

    final CongestionTaxService congestionTaxService;

    @Autowired
    public CongestionTaxController(CongestionTaxService congestionTaxService) {
        this.congestionTaxService = congestionTaxService;
    }



    @PostMapping("/taxRule")
    public ResponseEntity<Void> createTaxRuleForCity(@RequestBody TaxRule taxRule){
     try {
         congestionTaxService.createTaxRule(taxRule);
     }catch (TaxRuleAlreadyExist e){
         log.error("Tax rule for {} already exist", taxRule.getCity());
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
     }
      return ResponseEntity.ok().build();
    }

    @PostMapping("/tollEntries")
    public ResponseEntity<List<TollFee>> getVehicleTollFee(@RequestBody Toll toll){
        final List<TollFee> tollFees;
        try {
           tollFees = congestionTaxService.getTax(toll);
        }catch (TaxRuleNotExistException e){
            log.error("Tax Rule not found, Please upload city tax rule to the the toll entries");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.of(Optional.of(tollFees));
    }

    @GetMapping("/taxRule/{city}")
    public ResponseEntity<TaxRule> getTaxRuleCity(@PathVariable  String city){
       final TaxRule taxRule;
        try {
         taxRule =congestionTaxService.getTaxRuleByCity(city);
        }catch (TaxRuleNotExistException e){
            log.error("Tax Rule not found for {}", city );
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(taxRule);
    }

    @DeleteMapping("/taxRule/{city}")
    public ResponseEntity<Void> deleteTaxRuleCity(@PathVariable  String city){
        congestionTaxService.deleteTaxRuleByCity(city);
        return ResponseEntity.ok().build();
    }

}
