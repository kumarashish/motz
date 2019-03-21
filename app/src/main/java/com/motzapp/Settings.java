package com.motzapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import utils.Utils;

public class Settings  extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.oldPassword)
    EditText oldPassword;
    ProgressDialog progressDialog;
    AppController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
controller=(AppController)getApplicationContext();

        ButterKnife.bind(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
public boolean isAllFieldsValidated()
{boolean status=false;
    if((oldPassword.getText().length()>0)&&(newPassword.getText().length()>0)&&(confirmPassword.getText().length()>0))
    {
                 if(confirmPassword.getText().toString().equals(newPassword.getText().toString()))
                 {

                 }else {
                     Toast.makeText(Settings.this,"Password and Confirm Password must be same",Toast.LENGTH_SHORT).show();
                 }
    }else{
                if(oldPassword.getText().length()==0)
                {
                    Toast.makeText(Settings.this,"Please enter old password",Toast.LENGTH_SHORT).show();
                }else if(newPassword.getText().length()==0)
                {
                    Toast.makeText(Settings.this,"Please enter new password",Toast.LENGTH_SHORT).show();
                }
                else if(confirmPassword.getText().length()==0)
                {
                    Toast.makeText(Settings.this,"Please enter confirm password",Toast.LENGTH_SHORT).show();
                }
    }
    return status;
}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if(isAllFieldsValidated())
                {
                    if(Utils.isNetworkAvailable(Settings.this))
                    {
                         progressDialog.show();
                         controller.getWebApiCall().postData(Common.updatePasswordUrl,controller.getManager().getUserToken(),Common.updatePasswordKeys,new String[]{oldPassword.getText().toString(),newPassword.getText().toString()},Settings.this);
                    }
                }
                break;
        }
    }
public void cancelDialog()
{
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if(progressDialog!=null)
            {
                progressDialog.cancel();
            }
        }
    });
}
    @Override
    public void onSucess(String value) {
        if(Utils.getStatus(value)==true)
        {
            Utils.showToast(Settings.this,"Password updated sucessfully");
        }else{
            Utils.showToast(Settings.this,Utils.getMessage(value));
        }

        cancelDialog();

    }

    @Override
    public void onError(String value) {
        cancelDialog();
        Utils.showToast(Settings.this,Utils.getMessage(value));
    }
}