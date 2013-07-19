package com.balancedpayments.android;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a bank account object
 * 
 * @author Ben Mills
 */
public class BankAccount {
   private String routingNumber;
   private String accountNumber;
   private String name;
   private HashMap<String, String> optionalFields;
   private ArrayList<String> errors;
   public static enum AccountType {
      UNKNOWN,
      CHECKING,
      SAVINGS
   }
   private AccountType type;
   private boolean valid;
   
   public BankAccount(String routingNum, String accountNum, AccountType accountType, String accountName) {
      this(routingNum, accountNum, accountType, accountName, null);
   }
   
   public BankAccount(String routingNum, String accountNum, AccountType accountType, String accountName, HashMap<String, String> optFields) {
      routingNumber = routingNum;
      accountNumber = accountNum;
      type = accountType;
      name = accountName;
      optionalFields = optFields;
      errors = new ArrayList<String>();
      
      validate();
   }
   
   private boolean isRoutingNumberValid() {
      if (routingNumber == null) { return false; }
      if (routingNumber.length() != 9) { return false; }
      
      ArrayList<Integer> digits = new ArrayList<Integer>(routingNumber.length() - 1);
      for (int i = 0; i < routingNumber.length(); i++) {
         digits.add(Integer.parseInt(routingNumber.substring(i, i + 1)));
      }
      
      return digits.get(8) == (7 * (digits.get(0) + digits.get(3) + digits.get(6)) +
            3 * (digits.get(1) + digits.get(4) + digits.get(7)) +
            9 * (digits.get(2) + digits.get(5))
            ) % 10;
   }
   
   private boolean isAccountNumberValid() {
      if (accountNumber == null) { return false; }
      return accountNumber.length() > 0;
   }
   
   private boolean isAccountTypeValid() {
      if (type == null) { return false; }
      return (type.equals(AccountType.CHECKING) || type.equals(AccountType.SAVINGS));
   }
   
   private boolean isNameValid() {
      if (name == null) { return false; }
      return name.length() > 0;
   }
   
   /**
    * Validate the bank account object
    * 
    * @return true or false for bank account validity
    */
   private void validate() {
      boolean isValid = true;
      
      if (!isRoutingNumberValid()) {
         errors.add("Routing number is not valid");
      }
      
      if (!isAccountNumberValid()) {
         errors.add("Account number is not valid");
      }
      
      if (!isAccountTypeValid()) {
         errors.add("Account type must be \"checking\" or \"savings\"");
      }
      
      if (!isNameValid()) {
         errors.add("Account name is not valid");
      }
      
      if (!errors.isEmpty()) {
         isValid = false;
      }
      
      valid = isValid;
   }
   
   public boolean isValid() {
      return valid;
   }
   
   /**
    * Returns an ArrayList of validation errors
    * 
    * @return ArrayList of errors
    */
   public ArrayList<String> getErrors() {
      return errors;
   }
   
   /**
    * Returns the routing number
    * 
    * @return String representing routing number
    */
   protected String getRoutingNumber() {
      return routingNumber;
   }
   
   /**
    * Returns the account number
    * 
    * @return String representing account number
    */
   protected String getAccountNumber() {
      return accountNumber;
   }
   
   /**
    * Returns the name on bank account
    * 
    * @return String of name on bank account
    */
   protected String getName() {
      return name;
   }
   
   /**
    * Returns a HashMap of supplied optional fields
    * 
    * @return HashMap of optional fields
    */
   protected HashMap<String, String> getOptionalFields() {
      return optionalFields;
   }
      
   protected String getAccountTypeAsString() {
      switch(type) {
         case CHECKING:
            return "checking";
         case SAVINGS:
            return "savings";
         default:
            return "unknown";
      }
   }
}
