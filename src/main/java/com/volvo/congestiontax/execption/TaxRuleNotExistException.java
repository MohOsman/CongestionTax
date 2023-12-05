package com.volvo.congestiontax.execption;

public class TaxRuleNotExistException extends RuntimeException {
    public TaxRuleNotExistException(String message) {
        super(message);
    }
}
