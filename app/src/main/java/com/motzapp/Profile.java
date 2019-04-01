package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import model.RegisterModel;
import utils.Utils;
import utils.Validation;

public class Profile  extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.back)
    ImageView back;
    AppController controller;
    @BindView(R.id.fname)
    EditText fname;
    @BindView(R.id.lname)
    EditText lname;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.submit)
    Button submit;
    ProgressDialog progressDialog;
    Validation validation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        validation=new Validation(this);
        controller=(AppController) getApplicationContext();
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        email.setEnabled(false);
        setData();

    }
public void setData()
{
    fname.setText(controller.getProfile().getFname());
    lname.setText(controller.getProfile().getLname());
    email.setText(controller.getProfile().getEmailId());
    mobile.setText(controller.getProfile().getMobile());
}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if ((fname.getText().length() > 0) && (lname.getText().length() > 0)&& (mobile.getText().length() > 0)) {

                    if (Utils.isNetworkAvailable(Profile.this)) {
                        progressDialog.show();
                        controller.getWebApiCall().postData(Common.updateProfileUrl,controller.getManager().getUserToken(),Common.updateProfileKeys,new String[]{fname.getText().toString(),lname .getText().toString(),mobile.getText().toString()} ,Profile.this);
                    }
                } else {

                    if (fname.getText().length() == 0) {
                        Toast.makeText(Profile.this, "Please enter first name", Toast.LENGTH_SHORT).show();
                    } else if (lname.getText().length() == 0) {
                        Toast.makeText(Profile.this, "Please enter last name", Toast.LENGTH_SHORT).show();
                    }   else if (mobile.getText().length() == 0) {
                        Toast.makeText(Profile.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    } else
                        break;
                }
        }
    }

    @Override
    public void onSucess(String value) {
        if (Utils.getStatus(value) == true) {

           String emailId= controller.getProfile().getEmailId();
           RegisterModel model=new RegisterModel(value);
           model.setEmailId(emailId);
            controller.setProfile(model);
            Utils.showToast(Profile.this, "Profile Updated Sucessfully.");
            finish();

        } else {
            Utils.showToast(Profile.this, Utils.getMessage(value));
        }

        Utils.cancelProgressDialog(Profile.this,progressDialog);
    }

    @Override
    public void onError(String value) {
        Utils.showToast(Profile.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(Profile.this,progressDialog);
    }
}