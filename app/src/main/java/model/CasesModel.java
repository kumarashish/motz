package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CasesModel {
  String id;
    String category_id;
    String category_name;
    String case_manager;
    String title;
    String description;
    String status;
    String last_modified_on;



    public CasesModel(JSONObject jsonObject)
    {
        try{

            id=jsonObject.isNull("id")?"":jsonObject.getString("id");
             category_id=jsonObject.isNull("icategory_id")?"":jsonObject.getString("category_id");
            category_name=jsonObject.isNull("category_name")?"":jsonObject.getString("category_name");
            case_manager=jsonObject.isNull("case_manager")?"":jsonObject.getString("case_manager");
            title=jsonObject.isNull("title")?"":jsonObject.getString("title");
             description=jsonObject.isNull("description")?"":jsonObject.getString("description");
             status=jsonObject.isNull("status")?"":jsonObject.getString("status");
            last_modified_on=jsonObject.isNull("last_modified_on")?"":jsonObject.getString("last_modified_on");
        }catch(Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCase_manager() {
        return case_manager;
    }

    public String getLast_modified_on() {
        return last_modified_on;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }



}
