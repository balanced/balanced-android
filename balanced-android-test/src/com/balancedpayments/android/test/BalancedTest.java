package com.balancedpayments.android.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.BankAccount.AccountType;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.exception.BankAccountRoutingNumberInvalidException;
import com.balancedpayments.android.exception.CardDeclinedException;
import com.balancedpayments.android.exception.CardNotValidatedException;

public class BalancedTest extends AndroidTestCase {
   private String marketplaceURI = "/v1/marketplaces/TEST-MP11mTuSoch2Eb1vrdwsSi2i";
   
   public void testTokenizeCard() {
      String cardURI = "";
      Balanced balanced = new Balanced(marketplaceURI, getContext());
      Card card = new Card("4242424242424242", 9, 2014, "123");
      
      assertTrue(card.isValid());
      
      try {
         cardURI = balanced.tokenizeCard(card);
      }
      catch (Exception e) {}

      assertTrue(cardURI.contains("TEST-MP"));
   }
   
   public void testTokenizeCardDeclined() {
      String cardURI = "";
      String error = "";
      
      Card card = new Card("4222222222222220", 9, 2014, "123");
      
      assertTrue(card.isValid());
      
      Balanced balanced = new Balanced(marketplaceURI, getContext());
      try {
         cardURI = balanced.tokenizeCard(card);
      }
      catch (CardNotValidatedException e) {
         error = e.getMessage();
      }
      catch (CardDeclinedException e) {
         error = e.getMessage();
      }
      catch (Exception e) {}
      
      assertTrue(error.contains("Customer call bank"));
   }
   
   public void testTokenizeBankAccount() {
      String bankAccountURI = "";
      Balanced balanced = new Balanced(marketplaceURI, getContext());
      BankAccount bankAccount = new BankAccount("053101273", "111111111111",
                                 AccountType.CHECKING, "Johann Bernoulli");
      
      assertTrue(bankAccount.isValid());
      
      try {
         bankAccountURI = balanced.tokenizeBankAccount(bankAccount);
      }
      catch (Exception e) {}
      
      assertTrue(bankAccountURI.contains("TEST-MP"));
   }
   
   public void testTokenizeBankAccountInvalid() {
      String error = "";
      String bankAccountURI = "";
      Balanced balanced = new Balanced(marketplaceURI, getContext());
      BankAccount bankAccount = new BankAccount("100000007", "8887776665555",
                                 AccountType.CHECKING, "Johann Bernoulli");
      
      assertTrue(bankAccount.isValid());
      
      try {
         bankAccountURI = balanced.tokenizeBankAccount(bankAccount);
      }
      catch (BankAccountRoutingNumberInvalidException e) {
         error = e.getMessage();
      }
      catch (Exception e) {}
      
      assertTrue(error.contains("Routing number is invalid"));
   }
}
