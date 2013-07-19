package com.balancedpayments.android.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.exception.CardDeclinedException;
import com.balancedpayments.android.exception.CardNotValidatedException;

public class BalancedTest extends AndroidTestCase {
   private String marketplaceURI = "/v1/marketplaces/TEST-MP11mTuSoch2Eb1vrdwsSi2i";
   
   public void testTokenizeCard() {
      String cardURI = "";

      Card card = new Card("4242424242424242", 9, 2014, "123");
      Balanced balanced = new Balanced(marketplaceURI, getContext());
      try {
         cardURI = balanced.tokenizeCard(card);
      }
      catch (CardNotValidatedException e) {}
      catch (CardDeclinedException e) {}
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
}
