package com.balancedpayments.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.util.InetAddressUtils;

/** 
 * Utility methods
 * 
 * @author Ben Mills
 */
public class Utilities {
   protected static String getLocale() {
      String locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
      
      return locale;
   }
   
   /**
    * Get the current time zone offset.
    * 
    * @return time zone offset as string
    */
   protected static String getTimeZoneOffset() {
      Calendar mCalendar = new GregorianCalendar();
      TimeZone mTimeZone = mCalendar.getTimeZone();
      int mGMTOffset = mTimeZone.getOffset(mCalendar.getTimeInMillis());
      return Long.toString(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));
   }
   
   /**
    * Create user agent string
    * 
    * @param context Android application context
    * @return user agent string 
    */
   protected static String getUserAgentString(Context context) {
      TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String device = android.os.Build.MANUFACTURER;
      if (device == null || device.equals("unknown")) {
         device = "Simulator";
      }
      String model = android.os.Build.MODEL;
      String systemVersion = android.os.Build.VERSION.RELEASE;
      String carrier = manager.getNetworkOperatorName();
      String ip;
      String macAddress = "";
      if (carrier == null || carrier.equals("") || carrier.equals("Android")) {
         carrier = "Wi-Fi";
         ip = "";
         macAddress = Utilities.getMACAddress("eth0");
      }
      else
      {
         ip = Utilities.getIPAddress(true);
         macAddress = Utilities.getMACAddress("wlan0");
      }
      
      String agent = context.getPackageName() +
                     " balanced-android " +
                     Balanced.VERSION + ";" +
                     device + ";";
      
      if (model != null) {
         agent += model + ";";
      }
      
      if (systemVersion != null) {
         agent += systemVersion + ";";
      }
      
      if (! ip.equals("")) {
         agent += ip + ";";
      }
      
      if (! macAddress.equals("")) {
          agent += macAddress + ";";
       }
      
      agent += carrier;
      
      return agent;
   }
   
   /**
    * Returns MAC address of the given interface name.
    *
    * @param interfaceName eth0, wlan0 or NULL=use first interface
    * @return mac address or empty string
    */
   protected static String getMACAddress(String interfaceName) {
      try {
         List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
         for (NetworkInterface intf : interfaces) {
            if (interfaceName != null) {
               if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                  continue;
               }
            }
            byte[] mac = intf.getHardwareAddress();
            if (mac == null) { return ""; }
            StringBuilder buf = new StringBuilder();
            for (int idx = 0; idx < mac.length; idx++) {
               buf.append(String.format("%02X:", mac[idx]));
            }
            if (buf.length() > 0) {
               buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
         }
      }
      catch (Exception ex) {} // ignore exceptions
      
      return "";
   }

   /**
    * Get IP address from first non-localhost interface
    *
    * @param useIPv4 true=return ipv4 address, false=return ipv6 address
    * @return address or empty string
    */
   protected static String getIPAddress(boolean useIPv4)
   {
      try {
         List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
         for (NetworkInterface intf : interfaces) {
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
            for (InetAddress addr : addrs) {
               if (!addr.isLoopbackAddress()) {
                  String sAddr = addr.getHostAddress().toUpperCase();
                  boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                  if (useIPv4) {
                     if (isIPv4) {
                        return sAddr;
                     }
                  }
                  else {
                     if (!isIPv4) {
                        int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                        return delim < 0 ? sAddr : sAddr.substring(0, delim);
                     }
                  }
               }
            }
         }
      }
      catch (Exception ex) {} // ignore exceptions
      
      return "";
   }
}