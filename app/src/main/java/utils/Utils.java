package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;


import com.motzapp.Login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by ashish.kumar on 22-10-2018.
 */

public class Utils {
    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static String getDeviceID(Activity act) {
        String deviceId = "";
        deviceId = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
    public static boolean getStatus(String data) {
        boolean status = false;
        try {
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                return true;
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return status;
    }

    public static  void cancelProgressDialog(Activity act, final ProgressDialog dialog)
    {
       act. runOnUiThread(new Runnable() {
           @Override
           public void run() {
               dialog.cancel();
           }
       });

    }
    public static String getTotalCasesCount(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray cases = jsonObject.getJSONArray("total_cases_count");
           for(int i=0;i<cases.length();i++)
           {
               return cases.getJSONObject(i).getString( "total_cases");
           }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return "0";
    }
    public static void showToast(final Activity act, final String message) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, message, Toast.LENGTH_LONG).show();
            }
        });

    }
    public static String getMessage(String data) {
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            message = jsonObject.getString("msg");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return message;
    }

    public static JSONArray getJSONArray(String value,String key)
    {
        try{
            JSONObject jsonObject=new JSONObject(value);
            JSONArray jsonArray=jsonObject.getJSONArray(key);
            return jsonArray;
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return null;
    }



    public static String getCompleteAddressString(final Activity act, final double LATITUDE, final double LONGITUDE) {
        List<Address> list;
        String strAdd=null;
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            list = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            for (int n = 0; n <= list.get(0).getMaxAddressLineIndex()-2; n++) {
                strAdd +=   list.get(0).getAddressLine(n) + ", ";
            }

            strAdd = list.get(0).getLocality();
            if(strAdd.length()>22)
            {
                strAdd= strAdd.substring(0,22);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAdd;

    }
public static JSONArray getJSonArray(String value)
{
    try{
        JSONArray jsonArray=new JSONArray(value);
        return jsonArray;
    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }
    return null;
}

    public static JSONObject getTimers(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            return jsonObject.getJSONObject("users_timer");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return null;
    }

    public static JSONObject getLocationDetails(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            JSONArray jsonArray = jsonObject.getJSONArray("Location Details");

            return jsonArray.getJSONObject(0);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return null;
    }
    public static JSONArray getLocationsArray(String value)
    {
        try{
            JSONObject jsonObject=new JSONObject(value);
            JSONArray jsonArray=jsonObject.getJSONArray("locations");
            return jsonArray;
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return null;
    }

    public static boolean[] isTimerStarted(String value)
    {
        try{JSONObject jsonObj=new JSONObject(value);
            JSONObject jsonObject=jsonObj.getJSONObject("users_timer");

            return new boolean[]{jsonObject.getBoolean("IsTimerStarted"),jsonObject.getBoolean("IsTimerStoped"),jsonObject.getBoolean("IsPaymentDone")};
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return new boolean[]{};
    }

    public static String getcarPlateNumber(String value) {
        try{
            JSONObject jsonObject=new JSONObject(value);
           return jsonObject.getString("parking_carplate_no");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return "";
    }

    public static String getTotalParkingAmount(String value) {
        try{
            JSONObject jsonObject=new JSONObject(value);
            return jsonObject.getString("total_parking_fees");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return null;
    }

    public static void logout(final Activity act) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                act.startActivity(intent);
            }

        });

    }

    public static String getCaseId(String value) {
        JSONArray jsonArray = Utils.getJSONArray(value, "inserted_case");
        try {
            return jsonArray.getJSONObject(0).getString("id");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
        return "";
    }
}
