package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Users db = new Users(SignUpActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    //Annulation de l'inscription
    public void onClickCancel(View view) {
        Intent intentReturn = new Intent();
        setResult(1,intentReturn);
        finish();
    }

    public void onClickValidate(View view) {

        //Get EditTexts
        EditText usernameField = (EditText) findViewById(R.id.signUpLogin);
        EditText passwordField = (EditText) findViewById(R.id.signUpPwd);
        EditText passwordConfirmField = (EditText) findViewById(R.id.signUpConfirmPwd);

        if(usernameField.getText().toString().equals("")) {
            usernameField.setError(getString(R.string.username_missing));
        }
        else if (passwordField.getText().toString().equals("")){
            passwordField.setError(getString(R.string.password_missing));
        }
        else if (passwordConfirmField.getText().toString().equals("")) {
            passwordConfirmField.setError(getString(R.string.password_confirm_missing));
        }
        else if (!passwordField.getText().toString().equals(passwordConfirmField.getText().toString())) {
            passwordConfirmField.setError(getString(R.string.passwords_not_matching));
        }
        else if (this.db.isSignedUp(usernameField.getText().toString())) {
            usernameField.setError(getString(R.string.username_already_exists));
        }
        else {
            //Alors on inscrit l'utilisateur dans la db
            db.createAccount(usernameField.getText().toString(), passwordField.getText().toString());
            //Création de l'intent de retour
            Intent intentReturn = new Intent();
            //On donne l'id et le mot de passe en paramètres pour le pré-remplir sur l'activité de login
            intentReturn.putExtra("login", usernameField.getText().toString());
            intentReturn.putExtra("password", passwordField.getText().toString());
            setResult(1, intentReturn);
            finish();
        }
    }
}
