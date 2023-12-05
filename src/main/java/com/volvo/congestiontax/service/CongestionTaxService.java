package com.volvo.congestiontax.service;

import com.volvo.congestiontax.execption.TaxRuleAlreadyExist;
import com.volvo.congestiontax.execption.TaxRuleNotExistException;
import com.volvo.congestiontax.model.TaxRule;
import com.volvo.congestiontax.model.Toll;
import com.volvo.congestiontax.model.TollEntry;
import com.volvo.congestiontax.model.TollFee;

import java.util.List;

public interface CongestionTaxService {
   List<TollFee> getTax(Toll toll) throws  TaxRuleNotExistException;
   TaxRule createTaxRule(TaxRule taxRule) throws TaxRuleAlreadyExist;
   TaxRule getTaxRuleByCity(String city);

   void deleteTaxRuleByCity(String city);


}
