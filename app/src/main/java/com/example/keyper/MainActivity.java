package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Users db = new Users(MainActivity.this);
    private static int userLogID;

    //Enregistre l'ID de l'utilisateur connecté
    public static void setUserLogID(int userLogID) {
        MainActivity.userLogID = userLogID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Affichage du layout
        setContentView(R.layout.activity_main);

        //Récuperation de la liste
        ListView l = (ListView) findViewById(R.id.passwordList);

        //Compte le nombre de password du user

        //Création du tableau pour la liste
        String[] s = db.tabPasswordUser(MainActivity.userLogID);



        Toast.makeText(MainActivity.this, s.toString() , Toast.LENGTH_SHORT).show();


    }

    public void addChangeActivity() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
        finish();
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
            case R.id.menu_add:
                this.addChangeActivity();
                return true;
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
}
