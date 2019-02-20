package model;

import com.motzapp.Register;

import org.json.JSONObject;

/**
 * Created by ashish.kumar on 20-02-2019.
 */

public class RegisterModel {
    String fname;
    String lname;
    String emailId;
    String password;
    String mobile;
    String userId;
    public RegisterModel(String fname,String lname,String mobile,String emailId,String password)
    {
        this.fname=fname;
        this.lname=lname;
        this.emailId=emailId;
        this.password=password;
        this.mobile=mobile;
    }
public RegisterModel()
{
    this.fname="";
    this.lname="";
    this.emailId="";
    this.password="";
    this.mobile="";
}
    public RegisterModel(String value)
    {try {
        JSONObject jsonObjectt=new JSONObject(value);
        JSONObject jsonObject=jsonObjectt.getJSONObject("motzuser_details");
        this.fname =jsonObject.isNull("first_name")?"":jsonObject.getString("first_name");
        this.lname =jsonObject.isNull("last_name")?"":jsonObject.getString("last_name");
        this.emailId =jsonObject.isNull("email")?"":jsonObject.getString("email");
        this.mobile= jsonObject.isNull("phone")?"":jsonObject.getString("phone");
        this.userId  =jsonObject.isNull("user_id")?"":jsonObject.getString("user_id");
    }catch (Exception ex)
    {
        ex.fillInStackTrace();
    }
    }

    public String getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPassword() {
        return password;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }
}
