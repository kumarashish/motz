package com.motzapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;

public class Profile  extends Activity implements View.OnClickListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        controller=(AppController) getApplicationContext();
        back.setOnClickListener(this);
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
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }
}