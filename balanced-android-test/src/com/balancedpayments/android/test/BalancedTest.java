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
		Context context = getContext();
		
		
		Card card = new Card("4242424242424242", 9, 2014, "123");
	      Balanced balanced = new Balanced(marketplaceURI, context);
	      try {
	          cardURI = balanced.tokenizeCard(card);
	      }
	      catch (CardNotValidatedException e) {
	          
	      }
	      catch (CardDeclinedException e) {
	          
	      }
	      catch (Exception e) {
	          e.printStackTrace();
	      }
	      
	      Log.d("BalancedTest", "********** CARD URI: " + cardURI);
	}
}
