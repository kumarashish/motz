package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import model.RegisterModel;
import utils.Utils;
import utils.Validation;

public class Register extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.signUp)
    Button signup;
    @BindView(R.id.signIn)
    TextView signIn;
    @BindView(R.id.fname)
    EditText fname;
    @BindView(R.id.lname)
    EditText lName;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mobile)
    EditText mobile;
    ProgressDialog progressDialog;
    AppController controller;
    Validation validation;
int apiCall;
int checkUserExistence=1,register=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        if( (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)&&(Build.VERSION.SDK_INT <26) ){
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }else if(Build.VERSION.SDK_INT >=26){
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//        }
        ButterKnife.bind(this);
        signIn.setOnClickListener(this);
        signup.setOnClickListener(this);
        back.setOnClickListener(this);
        controller=(AppController) getApplicationContext();
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        validation=new Validation(Register.this);
    }
 public boolean isFieldsValidated()
 {
     if((fname.getText().length()>0)&&(lName.getText().length()>0)&&(mobile.getText().length()>0)&&(email.getText().length()>0)&&(password.getText().length()>0)&&(confirmPassword.getText().length()>0))
     {
         if(validation.isPassword_ConfirmPasswordSame(password,confirmPassword))
         {
             return true;
         }
     }else{
         if(fname.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please enter First Name",Toast.LENGTH_SHORT).show();
         }
         else if(lName.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please enter Last Name",Toast.LENGTH_SHORT).show();
         }
         else if(mobile.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please enter Mobile number",Toast.LENGTH_SHORT).show();
         } else if(email.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please enter Email Id",Toast.LENGTH_SHORT).show();
         }
         else if(password.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please enter password",Toast.LENGTH_SHORT).show();
         } else if(confirmPassword.getText().length()==0)
         {
             Toast.makeText(Register.this,"Please  confirm password",Toast.LENGTH_SHORT).show();
         }else if(!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim()))
         {
             Toast.makeText(Register.this,"Password and Confirm password should be same",Toast.LENGTH_SHORT).show();
         }


     }
     return false;
 }

    public void isUserAlreadyExists()
    {   progressDialog.show();
    apiCall=checkUserExistence;
        controller.getWebApiCall().postData(Common.isUserExistUrl,"",Common.emaiId,new String[]{email.getText().toString()},Register.this);

    }
 public void register()
 { runOnUiThread(new Runnable() {
     @Override
     public void run() {
         progressDialog.show();
         apiCall=register;
         RegisterModel model=new RegisterModel(fname.getText().toString(),lName.getText().toString(),email.getText().toString(),mobile.getText().toString(),password.getText().toString());
         controller.getWebApiCall().register(Common.registerUser,model,Register.this);

     }
 });


 }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.signIn:
                finish();
                break;
            case R.id.signUp:
                if(isFieldsValidated())
                {
                    isUserAlreadyExists();

                  progressDialog.show();


                      }
                break;
        }
    }

    @Override
    public void onSucess(String value) {


        {
            switch (apiCall) {
                case 1:
                    if (Utils.getStatus(value) == true) {
                        Utils.showToast(Register.this, Utils.getMessage(value));
                        Utils.cancelProgressDialog(Register.this, progressDialog);
                    } else {
                        Utils.cancelProgressDialog(Register.this, progressDialog);
                        register();
                    }

                    break;
                case 2:
                    if (Utils.getStatus(value) == true) {
                        Utils.showToast(Register.this, Utils.getMessage(value));
                        Utils.cancelProgressDialog(Register.this, progressDialog);
                        finish();
                    } else {
                        Utils.cancelProgressDialog(Register.this, progressDialog);
                        Utils.showToast(Register.this, Utils.getMessage(value));
                    }
                    break;
            }


        }


    }

    @Override
    public void onError(String value) {
        Utils.showToast(Register.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(Register.this,progressDialog);

    }
}