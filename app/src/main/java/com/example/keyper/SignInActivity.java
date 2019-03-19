package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    private Users db = new Users(SignInActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        this.db.createAdminAccount();
    }

    //Clic sur se connecter
    public void connectChangeActivity(View v) {
        //Recuperation des TextFields
        EditText usernameField = findViewById(R.id.loginUsername);
        EditText passwordField = findViewById(R.id.loginPassword);
        //Récupération des valeurs
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.equals("")) { //Si login vide
            usernameField.setError(getString(R.string.username_missing));
        } else if (password.equals("")) { //Si mot de passe vide
            passwordField.setError(getString(R.string.password_missing));
        }
        else if(!db.isSignedUp(username)) { //Si login introuvabe en db
            usernameField.setError(getString(R.string.incorrect_username));
        }
        else if(!db.checkPassword(username, password)){ //Si mauvais mot de passe
            passwordField.setError(getString(R.string.incorrect_password));
        }
        else { //Tout est bon, connexion
            //Enregistrement de l'id de l'utilisateur
            MainActivity.setUserID(db.getId(username));
            //Lancement du MainActivity
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Clic sur s'inscrire
    public void onClickSignUp(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivityForResult(intent,1); //Attente d'un résultat
    }

    @Override //Retour d'une activité
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==1 && !(data.getStringExtra("login") == null)) { //Si un retour de l'activité SignUp non null (Pas de cancel)
            //Récuperation des valeurs passées
            String usernameValue = data.getStringExtra("login");
            String passwordValue = data.getStringExtra("password");
            //Toast de confirmation
            Toast.makeText(SignInActivity.this, R.string.sign_up_prompt, Toast.LENGTH_SHORT).show();
            //On remplit les champs avec l'id et le mdp du nouvel inscrit
            TextView usernametextView = findViewById(R.id.loginUsername);
            TextView passwordTextView = findViewById(R.id.loginPassword);
            usernametextView.setText(usernameValue);
            passwordTextView.setText(passwordValue);
        }
    }
}
