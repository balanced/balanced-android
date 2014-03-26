package com.balancedpayments.android;

import android.content.Context;
import com.balancedpayments.android.exception.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for adding credit card and bank account information to Balanced Payments
 */
public class Balanced
{
   public static final String VERSION = "1.1-SNAPSHOT";
   private static String API_URL = "https://api.balancedpayments.com";
   private static String API_VERSION = "1.1";
   private static int connectionTimeout = 6000;
   private static int socketTimeout = 6000;
   private Context appContext;

   public enum BPFundingInstrumentType {
      Card,
      BankAccount
   }

   public Balanced(Context context) {
      appContext = context;
   }

   public Map<String, Object> createCard(String number, Integer expMonth, Integer expYear) throws CreationFailureException, FundingInstrumentNotValidException {
      return createCard(number, expMonth, expYear, null);
   }

   public Map<String, Object> createCard(String number,
                                         Integer expMonth,
                                         Integer expYear,
                                         Map<String, Object> optionalFields)
         throws FundingInstrumentNotValidException, CreationFailureException {
      Card card = new Card(number, expMonth, expYear);

      if (card.isValid()) {
         HashMap<String, Object> payload = new HashMap<String, Object>();
         payload.put("number", card.getNumber());
         payload.put("expiration_month", Integer.toString(card.getExpirationMonth()));
         payload.put("expiration_year", Integer.toString(card.getExpirationYear()));

         if (optionalFields != null && optionalFields.size() > 0) {
            payload.putAll(optionalFields);
         }

         return createFundingInstrument(payload, Balanced.BPFundingInstrumentType.Card);
      }
      else {
         throw new FundingInstrumentNotValidException("Card is not valid");
      }
   }

   public Map<String, Object> createBankAccount(String routingNum,
                                                String accountNum,
                                                BankAccount.AccountType accountType,
                                                String accountName)
         throws CreationFailureException, FundingInstrumentNotValidException {
      return createBankAccount(routingNum, accountNum, accountType, accountName, null);
   }

   public Map<String, Object> createBankAccount(String routingNum,
                                                String accountNum,
                                                BankAccount.AccountType accountType,
                                                String accountName,
                                                HashMap<String, Object> optionalFields)
         throws FundingInstrumentNotValidException, CreationFailureException {
      BankAccount bankAccount = new BankAccount(routingNum, accountNum, accountType, accountName, optionalFields);

      if (bankAccount.isValid()) {
         HashMap<String, Object> payload = new HashMap<String, Object>();
         payload.put("routing_number", routingNum);
         payload.put("account_number", accountNum);
         payload.put("name", accountName);
         payload.put("account_type", accountType);

         if (optionalFields != null && optionalFields.size() > 0) {
            payload.putAll(optionalFields);
         }

         return createFundingInstrument(payload, BPFundingInstrumentType.BankAccount);
      }
      else {
         throw new FundingInstrumentNotValidException("Card is not valid");
      }
   }

   public Map<String, Object> createFundingInstrument(Map<String, Object> payload, BPFundingInstrumentType type) throws CreationFailureException {
      payload.put("meta", Utilities.capabilities(appContext));
      try {
         HttpParams httpParameters = new BasicHttpParams();
         HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
         HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
         DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
         HttpPost request = new HttpPost(
               API_URL +
               (type.equals(BPFundingInstrumentType.Card) ? "/cards" : "/bank_accounts")
         );

         request.setHeader("accept", "application/json");
         request.setHeader("Content-Type", "application/json");
         request.setHeader("User-Agent", Utilities.userAgentString());

         request.setEntity(new StringEntity(serialize(payload), "UTF8"));
         HttpResponse response = httpClient.execute(request);

         BufferedReader br = new BufferedReader(
               new InputStreamReader((response.getEntity().getContent())));

         String responseData;
         String jsonData = "";
         while ((responseData = br.readLine()) != null) {
            jsonData += responseData;
         }

         int statusCode = response.getStatusLine().getStatusCode();
         Map<String, Object> jsonResponse = deserialize(jsonData);

         if (statusCode != 201) {
            throw new CreationFailureException((String)jsonResponse.get("description"));
         }

         httpClient.getConnectionManager().shutdown();

         jsonResponse.put("status_code", statusCode);

         return jsonResponse;
      }
      catch (MalformedURLException e) {
         throw new RuntimeException(e.getMessage());
      }
      catch (IOException e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   private String serialize(Object payload) {
      Gson gson = new Gson();
      String json = gson.toJson(payload);
      return json;
   }
   
   private Map<String, Object> deserialize(String body) {
      Gson gson = new Gson();
      return gson.fromJson(body, HashMap.class);
   }
}
