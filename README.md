![balancedlogo](https://www.balancedpayments.com/images/homepage_logo-01.png)

[![Build Status](https://travis-ci.org/balanced/balanced-android.png)](https://travis-ci.org/balanced/balanced-android)

Android library for working with Balanced Payments.
Current version : 0.1-SNAPSHOT

## Requirements

- junit 4.8.2
- [gson](http://code.google.com/p/google-gson/) 2.2.2
- [httpclient](http://hc.apache.org/) 4.2.1
- [commons-lang](http://commons.apache.org/lang/) 3.1

These should be installable via maven.

## Installation

- Add balanced-android-VERSION.jar as a dependency in your project

## Usage

#### Imports

    import com.balancedpayments.android.Balanced; // Tokenizing methods
    import com.balancedpayments.android.Card; // Credit cards
    import com.balancedpayments.android.BankAccount; // Bank accounts
    import com.balancedpayments.android.exception.*; // Exceptions

#### Define a marketplace URI

    private static String marketplaceURI = "/v1/marketplaces/TEST-MP11mTuSoch2Eb1vrdwsSi2i";
    
#### Create a balanced object

Instantiate a balanced instance with your marketplace URI and an Android application context.

    Balanced balanced = new Balanced(marketplaceURI, context);

#### Create a card object

##### With only required fields

    Card card = new Card("4242424242424242", 9, 2014, "123");

##### With optional fields

Use a HashMap for additional card fields you wish to specify.

    HashMap<String, String> optionalFields = new HashMap<String, String>();
    optionalFields.put(OptionalFieldKeyNameOnCard, "Test");
    optionalFields.put(OptionalFieldKeyNameOnCard, "Johann Bernoulli");
    optionalFields.put(OptionalFieldKeyStreetAddress, "123 Main Street");
    optionalFields.put(OptionalFieldKeyPostalCode, "11111");

    Card card = new Card("4242424242424242", 9, 2014, "123", optionalFields);

#### Tokenize a card

    Balanced balanced = new Balanced(marketplaceURI, context);
    Card card = new Card("4242424242424242", 9, 2014, "123");
		
    String cardURI = "";

	Card card = new Card("4242424242424242", 9, 2014, "123");
	Balanced balanced = new Balanced(marketplaceURI, context);
	try {
		cardURI = balanced.tokenizeCard(card);
	}
	catch (CardNotValidatedException e) {
		error = e;
	}
	catch (CardDeclinedException e) {
		error = e;
	}
	catch (Exception e) {
		e.printStackTrace();
	}

#### Create a bank account object

    BankAccount bankAccount = new BankAccount("053101273", "111111111111", AccountType.CHECKING, "Johann Bernoulli");

#### Tokenize a bank account

    String bankAccountURI = "";
		
	Balanced balanced = new Balanced(marketplaceURI, context);
    BankAccount bankAccount = new BankAccount("053101273", "111111111111", AccountType.CHECKING, "Johann Bernoulli");

	try {
		bankAccountURI = balanced.tokenizeBankAccount(bankAccount);
	}
	catch (BankAccountRoutingNumberInvalidException e) {
		error = e;
	}
	catch (Exception e) {
		e.printStackTrace();
	}

## Contributing

Please add yourself to the CONTRIBUTORS file when you submit your first pull request to the project.

Please follow the code conventions utilized in the existing files.

#### Tests

Please include tests with all new code. Also, all existing tests must pass before new code can be merged.
