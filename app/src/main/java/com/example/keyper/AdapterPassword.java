package com.example.keyper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPassword extends ArrayAdapter<Password> {

    public AdapterPassword(Context context, ArrayList<Password> pwd) {
        super(context, 0, pwd);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Password pwd = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }
        // Lookup view for data population
        TextView tvLib = (TextView) convertView.findViewById(R.id.lib);
        TextView tvPwd = (TextView) convertView.findViewById(R.id.pwd);

        LinearLayout l = (LinearLayout)convertView.findViewById(R.id.passwd_item_layout);
        l.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        // Populate the data into the template view using the data object
        tvLib.setText(pwd.getLib());
        tvPwd.setText(pwd.getPwd());
        tvPwd.setTag(pwd.getId());
        // Return the completed view to render on screen
        return convertView;
    }

}
