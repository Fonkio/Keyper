package com.example.keyper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private static Menu menu;
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

        //Recupération du +
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        //Clic sur le +
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }

        });

        //Affichage de la liste
        majList();

    }

    //Afficher la liste
    public void majList() {
        //Récuperation de la liste
        ListView passwordList = (ListView) findViewById(R.id.passwordList);

        //Création du tableau pour la liste
        ArrayList tabPwd = db.tabPasswordUser(MainActivity.userLogID);

        //Création de l'adapter personnalisé
        AdapterPassword adapter = new AdapterPassword(MainActivity.this,tabPwd);

        //Ajout de l'adapter
        passwordList.setAdapter(adapter);
        for (int i = 0; i < passwordList.getChildCount(); i++) {
            passwordList.getChildAt(i).setTag(new Boolean(false));
        }
    }

    //Création du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MainActivity.menu = menu;
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
                //Retour sur l'écran de connexion
                this.disconnectChangeActivity();
                return true;
            case R.id.menu_credits:
                Toast.makeText(MainActivity.this, R.string.app_credits, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                db.removePassword(this.getSelectedId());
                Toast.makeText(MainActivity.this, R.string.toastRemove, Toast.LENGTH_SHORT).show();
                this.majList();
                return true;
            case R.id.menu_edit:
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                intent.putExtra("id",this.getSelectedId());
                startActivity(intent);
                return true;
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

        if (passwd.charAt(0) == '•') { //Si le mot de passe est caché
            //Affichage du mot de passe
            String notHidden = this.db.getPasswordFromId((int)passwdField.getTag());
            passwdField.setText(notHidden);
        } else { //Si le mot de passe est visible
            //Chacher le mot de passe
            passwdField.setText(MainActivity.hidePassword(passwd.length()));
        }
    }

    //Cache le mot de passe
    public static String hidePassword(int length) {
        String hiddenPassword = "";
        for (int i = 0; i < length; i++) {
            hiddenPassword += "•";
        }
        return hiddenPassword;
    }

    public int getSelectedId() {
        //Recup de la liste
        ListView listPassword = findViewById(R.id.passwordList);
        //Parcours de la liste pour trouver un élément selectionné
        for(int i = 0; i < listPassword.getChildCount(); i++) {
            LinearLayout passwordItem = (LinearLayout)listPassword.getChildAt(i); //Récup de l'item

            Boolean sel = (Boolean) passwordItem.getTag();

            if(sel) {
                LinearLayout tvPassword = (LinearLayout) passwordItem.getChildAt(0);
                TextView tvContent = (TextView) tvPassword.getChildAt(1);
                return (int)tvContent.getTag();
            }
        }
        return -1;
    }

    public static void showMenuItem () {
        MainActivity.menu.getItem(0).setVisible(true);
        MainActivity.menu.getItem(1).setVisible(true);
    }

    public static void hideMenuItem () {
        MainActivity.menu.getItem(0).setVisible(false);
        MainActivity.menu.getItem(1).setVisible(false);
    }

    public void clickBackground(View v) {

        ListView passwordList = findViewById(R.id.passwordList); //Recup de la liste complète

        for (int i = 0; i < passwordList.getChildCount(); i++) {
            passwordList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
            passwordList.getChildAt(i).setTag(false);
        }

        MainActivity.hideMenuItem();
    }

}
