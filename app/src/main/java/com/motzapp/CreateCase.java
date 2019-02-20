package com.motzapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.WebApiResponseCallback;
import model.CategoriesModel;
import utils.Utils;

public class CreateCase  extends Activity implements View.OnClickListener, WebApiResponseCallback {
@BindView(R.id.back)
    ImageView back;
@BindView(R.id.send)
Button send;
@BindView(R.id.category)
    Spinner category;
    Dialog dialog;
    AppController controller;
    ProgressDialog progressDialog;
    ArrayList<CategoriesModel>list=new ArrayList<>();
    ArrayList<String>categoryNameList=new ArrayList<>();
    int apiCall;
    int getCategoryApiCall=1,createCase=2;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.description)
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_case);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (Utils.isNetworkAvailable(CreateCase.this)) {
            apiCall=getCategoryApiCall;
            progressDialog.show();
            controller.getWebApiCall().postData(Common.getCategoryUrl,controller.getProfile().getUserId(),controller.getManager().getUserToken(),CreateCase.this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.send:
              if((title.getText().length()>0)&&(description.getText().length()>0))
              {
                  apiCall=createCase;
                  progressDialog.show();
                  controller.getWebApiCall().createCase(Common.createCase,controller.getProfile().getUserId(),list.get(category.getSelectedItemPosition()).getId(),title.getText().toString(),description.getText().toString(),controller.getManager().getUserToken(),CreateCase.this);

              }else{
                  if(title.getText().length()==0)
                  {
                      Utils.showToast(CreateCase.this,"Please enter title");
                  }
                  else if(description.getText().length()>0)
                  {
                      Utils.showToast(CreateCase.this,"Please enter description");
                  }
              }
                break;
        }
    }


    public void sucessAlert(String caseId)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.sucess_alert, null);
        TextView caseId_TV=(TextView)dialogLayout.findViewById(R.id.caseId);
        caseId_TV.setText("Case Id | "+caseId+" ");
        builder.setView(dialogLayout);
        final Button done=(Button) dialogLayout.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",true);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

       dialog= builder.create();
        dialog.show();

    }

    @Override
    public void onSucess( final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (Utils.getStatus(value) == true) {

                    switch (apiCall) {
                        case 1:
                        JSONArray jsonArray = Utils.getJSONArray(value, "categories_details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                CategoriesModel model = new CategoriesModel(jsonArray.getJSONObject(i));
                                list.add(model);
                                categoryNameList.add(model.getName());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (categoryNameList.size() > 0) {
                            category.setAdapter(new ArrayAdapter<>(CreateCase.this, android.R.layout.simple_spinner_dropdown_item, categoryNameList));
                        }
break;
                        case 2:
                                  sucessAlert(Utils.getCaseId(value));
                            break;
                    }
                    } else{
                        Utils.showToast(CreateCase.this, Utils.getMessage(value));
                    }

                Utils.cancelProgressDialog(CreateCase.this,progressDialog);
            }
        });


    }

    @Override
    public void onError(String value) {
        Utils.showToast(CreateCase.this,Utils.getMessage(value));
        Utils.cancelProgressDialog(CreateCase.this,progressDialog);
    }
}