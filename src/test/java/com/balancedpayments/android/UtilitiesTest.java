package com.balancedpayments.android;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

   public void testGetTimeZoneOffset() {
      Calendar mCalendar = new GregorianCalendar();
      TimeZone mTimeZone = mCalendar.getTimeZone();
      int mGMTOffset = mTimeZone.getOffset(mCalendar.getTimeInMillis());
      assertTrue(Long.toString(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS)).equals(Utilities.getTimeZoneOffset()));
   }
   
   public void testGetLocale() {
      String locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
      
      assertTrue(locale.equals(Utilities.getLocale()));
   }
}
