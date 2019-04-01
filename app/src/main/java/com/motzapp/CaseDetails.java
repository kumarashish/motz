package com.motzapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import model.CasesModel;
import model.CategoriesModel;
import utils.Utils;

public class CaseDetails  extends Activity implements View.OnClickListener, WebApiResponseCallback {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.category)
    TextView category;
   public static CasesModel model;
    AppController controller;
    ProgressDialog progressDialog;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.last_mordified_on)
    TextView lastMordification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_details);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setValue();
    }
public void setValue()
{   category.setText(model.getCategory_name());
    title.setText(model.getTitle());
    description.setText(model.getDescription());
    lastMordification.setText("Last modification : "+model.getLast_modified_on());
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




    @Override
    public void onSucess( final String value) {



    }


    @Override
    public void onError(String value) {

    }
}
