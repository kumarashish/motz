package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class Login  extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.signUp)
    TextView signUp;
    @BindView(R.id.facebook)
    Button fb;
    @BindView(R.id.google)
    Button google;
    @BindView(R.id.forgetpassword)
    TextView forgotPassword;
    ProgressDialog progressDialog;
    Validation validation;
    @BindView(R.id.emailId)
    EditText emailId;
    @BindView(R.id.password)
    EditText password;
    AppController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && (Build.VERSION.SDK_INT < 26)) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        controller=(AppController) getApplicationContext();
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ButterKnife.bind(this);
        fb.setOnClickListener(this);
        google.setOnClickListener(this);
        submit.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        validation=new Validation(Login.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.facebook:
                Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.google:
                Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submit:
                if( ( validation.isEmailIdValid(emailId))&&(validation.isNotNull(password))) {
                    if (Utils.isNetworkAvailable(Login.this)) {
                        progressDialog.show();
                        controller.getWebApiCall().login(Common.login,emailId.getText().toString(),password.getText().toString(),Login.this);
                    } else {

                        if (emailId.getText().length() == 0) {
                            Toast.makeText(Login.this, "Please enter valid username", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.signUp:
                startActivity(new Intent(Login.this,Register.class));
                break;
            case R.id.forgetpassword:
                startActivity(new Intent(Login.this,ForgetPassword.class));

                break;
        }

    }

    @Override
    public void onSucess(String value) {
        if(Utils.getStatus(value)==true)
        {    controller.setProfile(new RegisterModel(value));
            controller.getManager().setUserLoggedIn(true);
            controller.getManager().setUserToken(value);
            startActivity(new Intent(Login.this, Dashboard.class));
            finish();
        }
        Utils.showToast(Login.this,Utils.getMessage(value));
        progressDialog.cancel();
    }

    @Override
    public void onError(String value) {
        Utils.showToast(Login.this,Utils.getMessage(value));
        progressDialog.cancel();


    }
}
