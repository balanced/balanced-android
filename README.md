![balancedlogo](https://www.balancedpayments.com/images/homepage_logo-01.png)

[![Build Status](https://travis-ci.org/balanced/balanced-android.png)](https://travis-ci.org/balanced/balanced-android)

Android library for tokenizing credit cards and bank accounts in Balanced Payments.

Current version : 1.0

## Requirements

- [gson](http://code.google.com/p/google-gson/) 2.2.4
- [httpclient](http://hc.apache.org/) 4.2.1

These should be installable via maven.

## Installation

- balanced-android v1.0 is now an Android Library Project. Include it as such in your application.

## Usage

#### Imports

```java
import com.balancedpayments.android.Balanced; // Tokenizing methods
import com.balancedpayments.android.Card; // Credit cards
import com.balancedpayments.android.BankAccount; // Bank accounts
import com.balancedpayments.android.exception.*; // Exceptions
```

#### Add a card

```appContext``` should be an instance of your application's context.

```java
Balanced balanced = new Balanced(appContext);
Map<String, Object> response = null;

try {
   response = balanced.createCard("4242424242424242", 9, 2014);
}
catch (CreationFailureException e) {
}
catch (FundingInstrumentNotValidException e) {
}

Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);

String cardHref = cardResponse.get("href");
```

#### Add a card with optional fields

```appContext``` should be an instance of your application's context.

```java
Balanced balanced = new Balanced(appContext);
Map<String, Object> response = null;

HashMap<String, String> address = new HashMap<String, String>();
optionalFields.put("line1", "123 Street");
optionalFields.put("state", "CA");
optionalFields.put("city", "San Francisco");
optionalFields.put("postal_code", "94102");

HashMap<String, Object> optionalFields = new HashMap<String, String>();
optionalFields.put(OptionalFieldKeyName, "Johann Bernoulli");
optionalFields.put(OptionalFieldKeyCVV, "123");
optionalFields.put(OptionalFieldKeyAddress, address);

try {
   response = balanced.createCard("4242424242424242", 9, 2014, optionalFields);
}
catch (CreationFailureException e) {
}
catch (FundingInstrumentNotValidException e) {
}

Map<String, Object> cardResponse = (Map<String, Object>) ((ArrayList)response.get("cards")).get(0);

String cardHref = cardResponse.get("href");
```

#### Add a bank account

```appContext``` should be an instance of your application's context.

```java
Balanced balanced = new Balanced(appContext);
Map<String, Object> response = null;

try {
   response = balanced.createBankAccount("021000021", "9900000002",
                  AccountType.CHECKING, "Johann Bernoulli");
}
catch (CreationFailureException e) {
}
catch (FundingInstrumentNotValidException e) {
}

Map<String, Object> bankAccountResponse = (Map<String, Object>) ((ArrayList)response.get("bank_accounts")).get(0);

String bankAccountHref = bankAccountResponse.get("href");
```

#### Add a bank account with optional fields

```appContext``` should be an instance of your application's context.

```java
Balanced balanced = new Balanced(appContext);
Map<String, Object> response = null;

HashMap<String, String> address = new HashMap<String, String>();
optionalFields.put("line1", "123 Street");
optionalFields.put("state", "CA");
optionalFields.put("city", "San Francisco");
optionalFields.put("postal_code", "94102");

HashMap<String, Object> optionalFields = new HashMap<String, String>();
optionalFields.put(OptionalFieldKeyName, "Johann Bernoulli");
optionalFields.put(OptionalFieldKeyCVV, "123");
optionalFields.put(OptionalFieldKeyAddress, address);

try {
   response = balanced.createBankAccount("021000021", "9900000002",
     AccountType.CHECKING, "Johann Bernoulli", optionalFields);
}
catch (CreationFailureException e) {
}
catch (FundingInstrumentNotValidException e) {
}

Map<String, Object> bankAccountResponse = (Map<String, Object>) ((ArrayList)response.get("bank_accounts")).get(0);

String bankAccountHref = bankAccountResponse.get("href");
```

## Contributing

Please add yourself to the CONTRIBUTORS file when you submit your first pull request to the project.

Please follow the code conventions utilized in the existing files.

#### Tests

Please include tests with all new code. Also, all existing tests must pass before new code can be merged.

- Install the Android SDK
- Set ANDROID_HOME to the Android SDK path
- Create an Android emulator instance. For example, ```android create avd --force -n test -t android-19 --abi armeabi-v7a```. You might want to install one that is optimized for your machine.
- Run ```mvn clean install```
