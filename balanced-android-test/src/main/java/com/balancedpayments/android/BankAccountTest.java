package com.balancedpayments.android;

import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.BankAccount.AccountType;

import android.test.AndroidTestCase;

public class BankAccountTest extends AndroidTestCase {
   public void testValidRoutingNumbers() {
      String[] routingNumbers = { "053101273",
                                  "114900685",
                                  "113002186",
                                  "122242607",
                                  "267090617",
                                  "011100106",
                                  "081501065",
                                  "021411089",
                                  "107000327",
                                  "021203501",
                                  "065201048",
                                  "107000233" };
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      for (int i = 0; i < routingNumbers.length; i++) {
         BankAccount ba = new BankAccount(routingNumbers[i], accountNumber, type, name);
         
         assertTrue(ba.isValid());
         assertTrue(ba.getErrors().isEmpty());
      }
   }
   
   public void testRoutingNumberValidWithNull() {
      String routingNumber = null;
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Routing number is not valid"));
   }
   
   public void testRoutingNumberValidWithBlank() {
      String routingNumber = "";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertTrue(!ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Routing number is not valid"));
   }
   
   public void testAccountNumberValid() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertTrue(ba.isValid());
      assertTrue(ba.getErrors().isEmpty());
   }
   
   public void testAccountNumberValidWithNull() {
      String routingNumber = "053101273";
      String accountNumber = null;
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account number is not valid"));
   }
   
   public void testAccountNumberValidWithBlank() {
      String routingNumber = "053101273";
      String accountNumber = "";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account number is not valid"));
   }
   
   public void testNameValid() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertTrue(ba.isValid());
      assertTrue(ba.getErrors().isEmpty());
   }
   
   public void testNameValidWithNull() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = null;
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account name is not valid"));
   }
   
   public void testNameValidWithBlank() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account name is not valid"));
   }
   
   public void testAccountTypeValidWithChecking() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.CHECKING;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertTrue(ba.isValid());
      assertTrue(ba.getErrors().isEmpty());
   }
   
   public void testAccountTypeValidWithSavings() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.SAVINGS;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertTrue(ba.isValid());
      assertTrue(ba.getErrors().isEmpty());
   }
   
   public void testAccountTypeValidWithUnknown() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = AccountType.UNKNOWN;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account type must be \"checking\" or \"savings\""));
   }
   
   public void testAccountTypeValidWitNull() {
      String routingNumber = "053101273";
      String accountNumber = "111111111111";
      AccountType type = null;
      String name = "Johann Bernoulli";
      
      BankAccount ba = new BankAccount(routingNumber, accountNumber, type, name);
      
      assertFalse(ba.isValid());
      assertFalse(ba.getErrors().isEmpty());
      assertTrue(ba.getErrors().contains("Account type must be \"checking\" or \"savings\""));
   }
}
