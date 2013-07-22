package com.balancedpayments.android.exception;

import java.util.ArrayList;

/**
 * Custom exception type for invalid bank account
 * 
 * @author Ben Mills
 */
public class BankAccountNotValidException extends Exception {
   private ArrayList<String> errors;
   
   public BankAccountNotValidException(String message) {
      this(message, null);
   }
   
   public BankAccountNotValidException(String message, ArrayList<String> errorList) {
      super(message);
      errors = errorList;
   }
   
   public ArrayList<String> getErrors() {
      return errors;
   }
}
