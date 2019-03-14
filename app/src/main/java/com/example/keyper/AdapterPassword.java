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
        Password password = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        final View finalConvertView = convertView;

        // Lookup view for data population
        TextView passwordTitle = finalConvertView.findViewById(R.id.title);
        TextView passwordContent = finalConvertView.findViewById(R.id.content);

        LinearLayout passwordItem = finalConvertView.findViewById(R.id.passwd_item_layout);
        passwordItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AdapterPassword.setLongClick(true);

                LinearLayout passwordItem = (LinearLayout)view.getParent();

                ListView passwordList = (ListView)passwordItem.getParent();

                //Set all password item's background color to white
                for (int i = 0; i < passwordList.getChildCount(); i++) {
                    passwordList.getChildAt(i).setBackgroundColor(finalConvertView.getResources().getColor(R.color.white));
                }
                //Set selected password item's background color to blue
                passwordItem.setBackgroundColor(finalConvertView.getResources().getColor(R.color.selected));

                return false;
            }
        });

        passwordItem.setOnClickListener(new View.OnClickListener() {

            private Users db = new Users(getContext());

            @Override
            public void onClick(View view) {

                if (AdapterPassword.isLongClick()) {
                    AdapterPassword.setLongClick(false);
                }
                else {
                    LinearLayout passwordItem = (LinearLayout) view.getParent();

                    ListView passwordList = (ListView) passwordItem.getParent();

                    //Set all password item's background color to white
                    for (int i = 0; i < passwordList.getChildCount(); i++) {
                        passwordList.getChildAt(i).setBackgroundColor(finalConvertView.getResources().getColor(R.color.white));
                    }

                    TextView passwordContent = (TextView)((LinearLayout)passwordItem.getChildAt(0)).getChildAt(1);
                    int passwordContentId = (int)passwordContent.getTag();

                    String label = "List";
                    String password = this.db.getPasswordFromId(passwordContentId);
                    Toast.makeText(getContext(), R.string.clipBoardCopy, Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(label, password);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        // Populate the data into the template view using the data object
        passwordTitle.setText(password.getTitle());
        passwordContent.setText(password.getContent());
        passwordContent.setTag(password.getId());

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
