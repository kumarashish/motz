package model;

import org.json.JSONObject;

/**
 * Created by ashish.kumar on 20-02-2019.
 */

public class CategoriesModel {
   String id;
    String name;
    String fullname;
    String parent_id;

    public CategoriesModel(JSONObject jsonObject)
    {
        try{
           this.id=jsonObject.isNull("id")?"":jsonObject.getString("id");
            this.name=jsonObject.isNull("name")?"":jsonObject.getString("name");
            this.fullname=jsonObject.isNull("fullname")?"":jsonObject.getString("fullname");
            this.parent_id=jsonObject.isNull("parent_id")?"":jsonObject.getString("parent_id");
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullname() {
        return fullname;
    }

    public String getParent_id() {
        return parent_id;
    }
}
