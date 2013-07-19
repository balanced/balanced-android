package com.balancedpayments.android;

import android.content.Context;
import com.balancedpayments.android.exception.BankAccountNotValidException;
import com.balancedpayments.android.exception.BankAccountRoutingNumberInvalidException;
import com.balancedpayments.android.exception.CardDeclinedException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import com.balancedpayments.android.exception.CardNotValidatedException;

/**
 * Class for card tokenization to Balanced Payments
 * 
 * @author Ben Mills
 */
public class Balanced
{
   public static final String VERSION = "0.1-SNAPSHOT";
   private static String API_URL = "https://js.balancedpayments.com";
   private static int connectionTimeout = 6000;
   private static int socketTimeout = 6000;
   private String marketplaceURI;
   private Context appContext;
   
   public Balanced(String uri, Context context) {
      marketplaceURI = uri;
      appContext = context;
   }

   public String tokenizeCard(Card card) throws Exception {
      if (card.isValid()) {
         try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
            HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost request = new HttpPost(API_URL + marketplaceURI + "/cards");

            request.setHeader("accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("User-Agent", Utilities.getUserAgentString(appContext));
            
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("card_number", card.getNumber());
            params.put("expiration_month", Integer.toString(card.getExpirationMonth()));
            params.put("expiration_year", Integer.toString(card.getExpirationYear()));
            params.put("language", Utilities.getLocale());
            params.put("security_code", card.getSecurityCode());
            params.put("system_timezone", Utilities.getTimeZoneOffset());

            if (card.getOptionalFields() != null) {
               params.putAll(card.getOptionalFields());
            }

            request.setEntity(new StringEntity(serialize(params), "UTF8"));
            HttpResponse response = httpClient.execute(request);

            BufferedReader br = new BufferedReader(
                  new InputStreamReader((response.getEntity().getContent())));

            String responseData;
            String jsonData = "";
            while ((responseData = br.readLine()) != null) {
               jsonData += responseData;
            }
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 402 && statusCode != 409) {
               System.out.println("blah");
               throw new RuntimeException("Failed : HTTP error code : "
                                          + response.getStatusLine().getStatusCode());
            }
            
            Map<String, Object> jsonResponse = deserialize(jsonData);
            
            if (statusCode == 402) {
               throw new CardDeclinedException((String)jsonResponse.get("description"));
            }
            
            if (statusCode == 409) {
               throw new CardNotValidatedException((String)jsonResponse.get("description"));
            }
            
            httpClient.getConnectionManager().shutdown();
            
            return (String)jsonResponse.get("uri");
         }
         catch (MalformedURLException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
      else {
         throw new CardNotValidatedException("Card is not valid");
      }
      
      return "";
   }
   
   public String tokenizeBankAccount(BankAccount bankAccount) throws Exception {
      if (bankAccount.isValid()) {
         try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
            HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost request = new HttpPost(API_URL + marketplaceURI + "/bank_accounts");

            request.setHeader("accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("User-Agent", Utilities.getUserAgentString(appContext));
            
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("routing_number", bankAccount.getRoutingNumber());
            params.put("account_number", bankAccount.getAccountNumber());
            params.put("type", bankAccount.getAccountTypeAsString());
            params.put("name", bankAccount.getName());
            params.put("language", Utilities.getLocale());
            params.put("system_timezone", Utilities.getTimeZoneOffset());

            if (bankAccount.getOptionalFields() != null) {
               params.putAll(bankAccount.getOptionalFields());
            }

            request.setEntity(new StringEntity(serialize(params), "UTF8"));
            HttpResponse response = httpClient.execute(request);

            BufferedReader br = new BufferedReader(
                  new InputStreamReader((response.getEntity().getContent())));

            String responseData;
            String jsonData = "";
            while ((responseData = br.readLine()) != null) {
               jsonData += responseData;
            }
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 400) {
               throw new RuntimeException("Failed : Error " + response.getStatusLine().getStatusCode());
            }
            
            Map<String, Object> jsonResponse = deserialize(jsonData);
            
            if (statusCode == 400) {
               throw new BankAccountRoutingNumberInvalidException((String)jsonResponse.get("description"));
            }
            
            httpClient.getConnectionManager().shutdown();
            
            return (String)jsonResponse.get("uri");
         }
         catch (MalformedURLException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
      else {
         throw new BankAccountNotValidException("Bank account is not valid", bankAccount.getErrors());
      }
      
      return "";
   }

   private String serialize(Object payload) {
      Gson gson = new Gson();
      String json = gson.toJson(payload);
      return json;
   }
   
   private Map<String, Object> deserialize(String body) {
      Gson gson = new Gson();
      return gson.fromJson(body, new TypeToken<Map<String, Object>>() {}.getType());
   }
}
