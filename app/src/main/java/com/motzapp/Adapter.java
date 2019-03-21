package com.motzapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.motzapp.R;

import java.util.ArrayList;

import model.CasesModel;

public class Adapter extends BaseAdapter {
    ArrayList<CasesModel> list;
    Activity act;
    LayoutInflater   layoutInflater;
    public Adapter(ArrayList<CasesModel> list,Activity act)
    {
        this.list=list;
        this.act=act;
        layoutInflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CasesModel model=list.get(position);
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.case_row,null);
            holder.view=(View)convertView.findViewById(R.id.view);
            holder.initials=(TextView)convertView.findViewById(R.id.initials);
            holder.heading=(TextView)convertView.findViewById(R.id.heading);
            holder.details=(TextView)convertView.findViewById(R.id.details);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(model.getStatus().equalsIgnoreCase("New"))
        {
            holder.view.setBackgroundColor(act.getResources().getColor(R.color.colorPrimary));
        }else{
            holder.view.setBackgroundColor(act.getResources().getColor(R.color.dark));
        }
        if(model.getCategory_name().length()>1) {
            holder.initials.setText(model.getCategory_name().substring(0,4));
        }else{
            holder.initials.setText("N/A"+position);

        }
        holder.heading.setText(model.getTitle());
        holder.details.setText(model.getDescription());
        convertView.setTag(holder);
        return convertView;
    }

    public class ViewHolder{
       View view;
              TextView initials;
        TextView  heading;
        TextView  details;
    }
}
