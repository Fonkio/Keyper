package com.example.keyper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Users db = new Users(MainActivity.this);
    public static int userLogID;

    //Enregistre l'ID de l'utilisateur connecté
    public static void setUserLogID(int userLogID) {
        MainActivity.userLogID = userLogID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Affichage du layout
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        //Récuperation de la liste
        ListView passwordList = (ListView) findViewById(R.id.passwordList);

        //Compte le nombre de password du user

        //Création du tableau pour la liste
        ArrayList tabPwd = db.tabPasswordUser(MainActivity.userLogID);

        AdapterPassword adapter = new AdapterPassword(MainActivity.this,tabPwd);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.activity_list_item , s);*/
        passwordList.setAdapter(adapter);
    }

    //Création du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    //Traitement des évenements sur les items du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_disconnect:
                Toast.makeText(MainActivity.this, R.string.toastDisconnect, Toast.LENGTH_SHORT).show();
                this.disconnectChangeActivity();
                return true;
            case R.id.menu_credits:
                Toast.makeText(MainActivity.this, R.string.app_credits, Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Renvoie sur l'activité de login
    public void disconnectChangeActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void addChangeActivity() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickItemList(View v) {
        LinearLayout pv = (LinearLayout)v.getParent(); //Le linearLayout

        TextView r = (TextView)((LinearLayout)pv.getChildAt(0)).getChildAt(1);
        int rId = (int)r.getTag();

        String label = "List";
        String password = this.db.getPasswordFromId(rId);
        Toast.makeText(MainActivity.this, R.string.clipBoardCopy, Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, password);
        clipboard.setPrimaryClip(clip);
    }

    public void onClickVisibilityButton(View v) {

        //On récupère le bouton image
        AppCompatImageButton lin = (AppCompatImageButton) v;
        //On récupère le LinearLayout horizontal
        LinearLayout plin = (LinearLayout)lin.getParent();
        //Puis le LinearLayout vertical à l'interieur du précédent
        LinearLayout cplin = (LinearLayout) plin.getChildAt(0);
        //On peut donc accéder aux 2 TextView
        TextView passwdField = ((TextView) cplin.getChildAt(1));
        TextView titleField = (TextView) cplin.getChildAt(0);
        //On peut récupérer le titre
        String title = titleField.getText().toString();
        //Et le mot de passe
        String passwd = passwdField.getText().toString();

        if (passwd.charAt(0) == '•') {
            String notHidden = this.db.getPasswordFromId((int)passwdField.getTag());
            passwdField.setText(notHidden);
        } else {
            passwdField.setText(MainActivity.hidePassword(passwd.length()));
        }
    }

    public static String hidePassword(int length) {
        String hiddenPassword = "";
        for (int i = 0; i < length; i++) {
            hiddenPassword += "•";
        }
        return hiddenPassword;
    }


}
