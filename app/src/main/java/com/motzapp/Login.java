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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

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
    @BindView(R.id.btn_fblogin)
    LoginButton btn_fblogin;
    @BindView(R.id.facebook)
    Button fbLogin;
    CallbackManager  callbackManager;
    int apiCall;
    int login=1,fb_Login=2;
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
        btn_fblogin.setOnClickListener(this);
        fbLogin.setOnClickListener(this);
        google.setOnClickListener(this);
        submit.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        validation=new Validation(Login.this);
        btn_fblogin.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();

        btn_fblogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
                        if (loginResult.getAccessToken().isExpired()) {
                            LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
                        }
                        btn_fblogin.setVisibility(View.GONE);
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            // Application code
                                            String id = response.getJSONObject().getString("id");
                                            String name = response.getJSONObject().getString("name");
                                            String email = response.getJSONObject().getString("email");

                                            apiCall = fb_Login;
                                            progressDialog.show();
                                            controller.getWebApiCall().loginWithFb(Common.fbLoginUrl, id, email, name, utils.Utils.getDeviceID(Login.this), loginResult.getAccessToken().getToken(), Login.this);
                                            LoginManager.getInstance().logOut();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ;
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Log.d("Status", loginResult.toString());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("Status", exception.toString());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.facebook:
              btn_fblogin.performClick();
                break;
            case R.id.google:
                Toast.makeText(Login.this, "Logged in Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submit:
                if( ( validation.isEmailIdValid(emailId))&&(validation.isNotNull(password))) {
                    if (Utils.isNetworkAvailable(Login.this)) {
                        apiCall=login;
                        progressDialog.show();
                        controller.getWebApiCall().login(Common.login,emailId.getText().toString(),password.getText().toString(),Login.this);
                    }} else {

                        if (emailId.getText().length() == 0) {
                            Toast.makeText(Login.this, "Please enter valid username", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();
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
        {


                controller.setProfile(new RegisterModel(value));
                controller.getManager().setUserLoggedIn(true);
                controller.getManager().setUserToken(value);
                startActivity(new Intent(Login.this, Dashboard.class));
                finish();


        }
        Utils.showToast(Login.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(Login.this,progressDialog);
    }

    @Override
    public void onError(String value) {
        Utils.showToast(Login.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(Login.this,progressDialog);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
