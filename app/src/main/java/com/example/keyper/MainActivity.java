package com.example.keyper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        updateList("");

        //Recupération de la barre de recherche (mode paysage)
        TextView searchBar = findViewById(R.id.search);

        //Test si le téléphone est en mode paysage (si il ne l'est pas, la search bar n'existe pas et est donc à null)
        if(searchBar != null) {

            searchBar.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                //Même code que le méthode "updateList" de MainAcitivity, mais impossible de l'utiliser dans une classe anonyme
                @Override
                public void afterTextChanged(Editable editable) {
                    //Récuperation de la liste
                    ListView passwordListView = findViewById(R.id.passwordList);

                    //Création du tableau pour la liste
                    ArrayList passwordList = db.listPasswordUser(MainActivity.userLogID, editable.toString());

                    //Création de l'adapter personnalisé
                    AdapterPassword adapter = new AdapterPassword(MainActivity.this, passwordList);

                    //Ajout de l'adapter
                    passwordListView.setAdapter(adapter);

                    //Mise en place du tag de selection pour chaque items de la liste
                    for (int i = 0; i < passwordListView.getChildCount(); i++) {
                        passwordListView.getChildAt(i).setTag(new Boolean(false));
                    }
                }
            });
        }

    }

    //MàJ de la ListView
    public void updateList(String search) {
        //Récuperation de la liste
        ListView passwordListView = (ListView) findViewById(R.id.passwordList);

        //Création du tableau pour la liste
        ArrayList passwordList = db.listPasswordUser(MainActivity.userLogID, search);

        //Création de l'adapter personnalisé
        AdapterPassword adapter = new AdapterPassword(MainActivity.this, passwordList);

        //Ajout de l'adapter
        passwordListView.setAdapter(adapter);

        //Mise en place du tag de selection pour chaque items de la liste
        for (int i = 0; i < passwordListView.getChildCount(); i++) {
            passwordListView.getChildAt(i).setTag(new Boolean(false));
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
                Toast.makeText(MainActivity.this, R.string.disconnect_prompt, Toast.LENGTH_SHORT).show();
                //Retour sur l'écran de connexion
                this.disconnectChangeActivity();
                return true;

            case R.id.menu_credits:
                Toast.makeText(MainActivity.this, R.string.app_credits, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_delete:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.delete_dialog_title)
                        .setMessage(R.string.delete_dialog_text)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.removePassword(this.getSelectedId());
                                Toast.makeText(MainActivity.this, R.string.delete_prompt, Toast.LENGTH_SHORT).show();
                                this.updateList("");
                                MainActivity.hideMenuItem();
                            }


                            //Ici je reprend les deux méthodes de MainActivity car je ne peux pas les utiliser dans une classe anonyme
                            public void updateList(String search) {
                                //Récuperation de la liste
                                ListView passwordListView = (ListView) findViewById(R.id.passwordList);

                                //Création du tableau pour la liste
                                ArrayList passwordList = db.listPasswordUser(MainActivity.userLogID, search);

                                //Création de l'adapter personnalisé
                                AdapterPassword adapter = new AdapterPassword(MainActivity.this, passwordList);

                                //Ajout de l'adapter
                                passwordListView.setAdapter(adapter);

                                //Mise en place du tag de selection pour chaque items de la liste
                                for (int i = 0; i < passwordListView.getChildCount(); i++) {
                                    passwordListView.getChildAt(i).setTag(new Boolean(false));
                                }
                            }

                            public int getSelectedId() {
                                //Recupération de la liste
                                ListView passwordListView = findViewById(R.id.passwordList);

                                //Parcours de la liste pour trouver l'élément selectionné
                                for(int i = 0; i < passwordListView.getChildCount(); i++) {
                                    LinearLayout passwordListItem = (LinearLayout)passwordListView.getChildAt(i); //Récup de l'item

                                    Boolean selected = (Boolean) passwordListItem.getTag();

                                    if(selected) {
                                        LinearLayout tvPassword = (LinearLayout) passwordListItem.getChildAt(0);
                                        TextView tvContent = (TextView) tvPassword.getChildAt(1);
                                        return (int)tvContent.getTag();
                                    }
                                }
                                return -1;
                            }
                        })

                        .setNegativeButton(R.string.no, null).show();

                return true;

            case R.id.menu_edit:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id",this.getSelectedId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Renvoie sur l'activité de login
    public void disconnectChangeActivity() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
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
