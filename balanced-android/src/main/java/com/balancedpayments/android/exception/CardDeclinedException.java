package com.balancedpayments.android.exception;

/**
 * Custom exception handler for card declined error
 * 
 * @author Ben Mills
 */
public class CardDeclinedException extends Exception {
   public CardDeclinedException(String message) {
      super(message);
   }
}
