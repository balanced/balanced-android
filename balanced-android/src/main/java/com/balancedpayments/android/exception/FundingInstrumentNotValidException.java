package com.balancedpayments.android.exception;

/**
 * Custom exception type for card not validated error from Balanced Payments
 * 
 * @author Ben Mills
 */
public class FundingInstrumentNotValidException extends Exception {
   public FundingInstrumentNotValidException(String message) {
      super(message);
   }
}
