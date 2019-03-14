package com.example.keyper;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterPassword extends ArrayAdapter<Password> {



    private static boolean longClick = false;


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

        LinearLayout passwordItem = (LinearLayout) convertView.findViewById(R.id.passwd_item_layout);
        final View finalConvertView = convertView;
        passwordItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setLongClick(true);
                LinearLayout l = (LinearLayout)view.getParent();

                ListView lv = (ListView) l.getParent();

                for (int i = 0; i <lv.getChildCount(); i++) {
                    ((LinearLayout)lv.getChildAt(i)).setBackgroundColor(finalConvertView.getResources().getColor(R.color.white));
                }

                l.setBackgroundColor(finalConvertView.getResources().getColor(R.color.paleBlue));

                return false;
            }
        });
        passwordItem.setOnClickListener(new View.OnClickListener() {
            private Users db = new Users(getContext());
            @Override
            public void onClick(View view) {
                if (isLongClick()) {
                    setLongClick(false);
                } else {
                    LinearLayout pv = (LinearLayout) view.getParent(); //Le linearLayout

                    TextView r = (TextView) ((LinearLayout) pv.getChildAt(0)).getChildAt(1);
                    int rId = (int) r.getTag();

                    String label = "List";
                    String password = this.db.getPasswordFromId(rId);
                    Toast.makeText(getContext(), R.string.clipBoardCopy, Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(label, password);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        // Populate the data into the template view using the data object
        tvLib.setText(pwd.getLib());
        tvPwd.setText(pwd.getPwd());
        tvPwd.setTag(pwd.getId());
        // Return the completed view to render on screen
        return convertView;
    }

    public static void setLongClick(boolean longClick) {
        AdapterPassword.longClick = longClick;
    }

    public static boolean isLongClick() {
        return longClick;
    }
}
