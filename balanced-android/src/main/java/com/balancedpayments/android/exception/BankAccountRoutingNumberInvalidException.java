package com.balancedpayments.android.exception;

/**
 * Custom exception type for invalid bank account routing number
 * 
 * @author Ben Mills
 */
public class BankAccountRoutingNumberInvalidException extends Exception {
   public BankAccountRoutingNumberInvalidException(String message) {
      super(message);
   }
}
