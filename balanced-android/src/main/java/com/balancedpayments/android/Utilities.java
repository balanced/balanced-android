package com.balancedpayments.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.TimeUnit;

/** 
 * Utility methods
 * 
 * @author Ben Mills
 */
public class Utilities {

   protected static Map<String, String> capabilities(Context context) {

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

      HashMap<String, String> capabilities = new HashMap<String, String>();

      capabilities.put("capabilities_system_timezone", getTimeZoneOffset());
      capabilities.put("capabilities_user_agent", userAgentString());
      capabilities.put("capabilities_ip_address", ip);
      capabilities.put("capabilities_mac_address", macAddress);
      capabilities.put("capabilities_device_model", model);
      capabilities.put("capabilities_system_version", systemVersion);
      capabilities.put("capabilities_device_name", device);
      capabilities.put("capabilities_carrier", carrier);
      capabilities.put("capabilities_language", getLocale());

      return capabilities;
   }

   /**
    * Assemble and return the user agent string.
    *
    * @return user agent as string
    */
   protected static String userAgentString() {
      return "balanced-android/" + Balanced.VERSION;
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

   protected static String getLocale() {
      String locale = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
      
      return locale;
   }

   private String getSoftwareVersion(Context context) {
      PackageInfo pi;
      try {
         pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
         return pi.versionName;
      } catch (final PackageManager.NameNotFoundException e) {
         return "na";
      }
   }
}
