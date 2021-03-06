package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.FirebaseInstanceIDService;
import interfaces.WebApiResponseCallback;
import model.RegisterModel;
import utils.Utils;
import utils.Validation;

public class Login  extends FragmentActivity implements View.OnClickListener, WebApiResponseCallback, GoogleApiClient.OnConnectionFailedListener  {
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
    int  RC_SIGN_IN=222;
    @BindView(R.id.btn_sign_in)
    SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

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
                                            controller.getWebApiCall().loginWithSocialNetwork(Common.fbLoginUrl, id, email, name, utils.Utils.getDeviceID(Login.this), loginResult.getAccessToken().getToken(), Login.this);
                                            LoginManager.getInstance().logOut();
                                        } catch (Exception e) {
                                            e.printStackTrace();
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
                signIn();
                break;
            case R.id.submit:
                if( ( validation.isEmailIdValid(emailId))&&(validation.isNotNull(password))) {
                    if (Utils.isNetworkAvailable(Login.this)) {
                        apiCall=login;
                        progressDialog.show();
                        controller.getWebApiCall().login(Common.login,emailId.getText().toString(),password.getText().toString(),utils.Utils.getDeviceID(Login.this),Login.this);
                    }} else {

                        if (emailId.getText().length() == 0) {
                            Toast.makeText(Login.this, "Please enter valid username", Toast.LENGTH_SHORT).show();
                        } else if(password.getText().length()==0){
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
        Utils.showToast(Login.this,value);
        Utils.cancelProgressDialog(Login.this,progressDialog);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       // updateUI(false);
                    }
                });
    }



    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String id=acct.getId();
            String name = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            apiCall = fb_Login;
            progressDialog.show();
            controller.getWebApiCall().loginWithSocialNetwork(Common.googleLoginUrl, id, email, name, utils.Utils.getDeviceID(Login.this), id, Login.this);

            signOut();




        } else {
            // Signed out, show unauthenticated UI.

        }
    }
}
