package com.balancedpayments.android;

import android.test.AndroidTestCase;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.BankAccount.AccountType;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.exception.FundingInstrumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BalancedTest extends AndroidTestCase {

   public void testTokenizeCard() {
      Balanced balanced = new Balanced(getContext());
      Map<String, Object> response = null;

      try {
         response = balanced.createCard("4242424242424242", 9, 2014);
      }
      catch (Exception e) {
          System.out.println(e.getMessage());
      }

      assertEquals(201, response.get("status_code"));
      Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);
      assertNotNull(cardResponse.get("href"));
   }


   public void testTokenizeCardDeclined() {
      Map<String, Object> optionalFields = new HashMap<String, Object>();
      optionalFields.put("cvv", "123");

      Balanced balanced = new Balanced(getContext());
      Map<String, Object> response = null;

      try {
         response = balanced.createCard("4222222222222220", 9, 2014, optionalFields);
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
      }

      assertEquals(201, response.get("status_code"));
      Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);
      assertNotNull(cardResponse.get("href"));
   }


   public void testTokenizeBankAccount() {
      Balanced balanced = new Balanced(getContext());
      Map<String, Object> response = null;

      try {
         response = balanced.createBankAccount("021000021", "9900000002",
               AccountType.CHECKING, "Johann Bernoulli");
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
      }

      assertEquals(201, response.get("status_code"));
      Map<String, Object> bankAccountResponse = (Map<String, Object>) ((ArrayList)response.get("bank_accounts")).get(0);
      assertNotNull(bankAccountResponse.get("href"));
   }
   
   public void testTokenizeBankAccountInvalid() {
      Balanced balanced = new Balanced(getContext());
      Map<String, Object> response = null;

      try {
         response = balanced.createBankAccount("100000007", "8887776665555",
               AccountType.CHECKING, "Johann Bernoulli");
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
      }

      assertEquals(201, response.get("status_code"));
      Map<String, Object> bankAccountResponse = (Map<String, Object>) ((ArrayList)response.get("bank_accounts")).get(0);
      assertNotNull(bankAccountResponse.get("href"));
   }
}
