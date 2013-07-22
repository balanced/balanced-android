package com.balancedpayments.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a credit card object
 * 
 * @author Ben Mills
 */
public class Card {
   public static final String OptionalFieldKeyNameOnCard = "name";
   public static final String OptionalFieldKeyStreetAddress = "street_address";
   public static final String OptionalFieldKeyPhoneNumber = "phone_number";
   public static final String OptionalFieldKeyPostalCode = "postal_code";
   public static final String OptionalFieldKeyCity = "city";
   public static final String OptionalFieldKeyCountryCode = "country_code";
   public static final String OptionalFieldKeyMeta = "meta";
   public static final String OptionalFieldKeyState = "state";
   public static enum CardType {
      UNKNOWN,
      VISA,
      MASTERCARD,
      AMERICANEXPRESS,
      DISCOVER
   }
   private CardType type;
   private int expirationMonth;
   private int expirationYear;
   private String number;
   private HashMap<String, String> optionalFields;
   private String securityCode;
   private ArrayList<String> errors;
   private boolean valid;
   
   public Card(String cardNumber, int expirationMonth, int expirationYear, String securityCode) {
      this(cardNumber, expirationMonth, expirationYear, securityCode, null);
   }
   
   public Card(String cardNumber, int expMonth, int expYear, String code, HashMap<String, String> optFields) {
      if (cardNumber != null) {
         number = cardNumber.replaceAll("[^\\d]", "");
      }
      expirationMonth = expMonth;
      expirationYear = expYear;
      securityCode = code;
      optionalFields = optFields;
      errors = new ArrayList<String>();
      
      validate();
   }
   
   private boolean isNumberValid() {
      if (number == null) { return false; }
      if (number.length() < 12) { return false; }

      boolean odd = true;
      int total = 0;
      for (int i = number.length() - 1; i >= 0; i--) {
         int value = Integer.parseInt(number.substring(i, i + 1));
         total += (odd = !odd) ? 2 * value - (value > 4 ? 9 : 0) : value;
      }

      return (total % 10) == 0;
   }
   
   private boolean isSecurityCodeValid() {
      if (securityCode == null) { return false; }
      if (securityCode.length() == 0) { return false; }
      if (type == CardType.UNKNOWN) { return false; }
      int requiredLength = (type == CardType.AMERICANEXPRESS) ? 4 : 3;
      
      return securityCode.length() == requiredLength;
   }
   
   private boolean isExpired() {
      if (expirationMonth > 12 || expirationYear < 1) { return false; }
    
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
    
      return cal.get(Calendar.YEAR) > expirationYear || (cal.get(Calendar.YEAR) == expirationYear
                                                         && cal.get(Calendar.MONTH) >= expirationMonth);
   }
   
   private void validate() {
      boolean isValid = true;
    
      if (!isNumberValid()) {
        errors.add("Card number is not valid");
      }
      
      type = getType();
    
      if (isExpired()) {
        errors.add("Card is expired");
      }
    
      if (!isSecurityCodeValid()) {
        errors.add("Security code is not valid");
      }
      
      if (!errors.isEmpty()) {
         isValid = false;
      }
      
      valid = isValid;
   }
   
   protected String getNumber() {
      return number;
   }
   
   protected int getExpirationMonth() {
      return expirationMonth;
   }
   
   protected int getExpirationYear() {
      return expirationYear;
   }
   
   protected String getSecurityCode() {
      return securityCode;
   }
   
   protected HashMap<String, String> getOptionalFields() {
      return optionalFields;
   }
   
   public CardType getType() {
      if (type != null) { return type; }
      if (number == null) { return CardType.UNKNOWN; }
      if (number.length() < 12) { return CardType.UNKNOWN; }

      int digits = Integer.parseInt(number.substring(0, 2));
    
      if (digits >= 40 && digits <= 49) {
         return CardType.VISA;
      }
      else if (digits >= 50 && digits <= 59) {
         return CardType.MASTERCARD;
      }
      else if (digits == 60 || digits == 62 || digits == 64 || digits == 65) {
         return CardType.DISCOVER;
      }
      else if (digits == 34 || digits == 37) {
         return CardType.AMERICANEXPRESS;
      }
      else {
         return CardType.UNKNOWN;
      }
   }
   
   public boolean isValid() {
      return valid;
   }
   
   public ArrayList<String> getErrors() {
      return errors;
   }
}
