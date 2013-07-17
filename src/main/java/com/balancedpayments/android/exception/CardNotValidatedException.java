package com.balancedpayments.android.exception;

/**
 * Custom exception type for card not validated error from Balanced Payments
 * 
 * @author Ben Mills
 */
public class CardNotValidatedException extends Exception {
   public CardNotValidatedException(String message) {
      super(message);
   }
}
