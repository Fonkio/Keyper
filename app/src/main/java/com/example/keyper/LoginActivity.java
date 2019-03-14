package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Users db = new Users(LoginActivity.this);
    private static final String TAG = "KeyperLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.db.createAdminAccount();
    }

    public void connectChangeActivity(View v) {
        EditText usernameField = (EditText)findViewById(R.id.loginUsername);
        EditText passwordField = (EditText)findViewById(R.id.loginPassword);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.equals("")) {
            usernameField.setError("Veuillez entrer votre identifiant!");
        } else if (password.equals("")) {
            passwordField.setError("Veuillez entrer votre mot de passe!");
        }
        else if(!db.isSignedUp(username)) {
            usernameField.setError("Vous n'etes pas inscrit / mauvais identifiant!");
        }
        else if(!db.checkPassword(username, password)){
            passwordField.setError("Mauvais mot de passe!");
        }
        else {
            MainActivity.setUserLogID(db.getId(username));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSignUp(View v) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && !(data == null)) {
            String valLog = data.getStringExtra("login");
            String valPwd = data.getStringExtra("password");
            if(!valLog.equals(""))
                Toast.makeText(LoginActivity.this, "Vous vous êtes bien inscrit.", Toast.LENGTH_SHORT).show();

            TextView login = (TextView)findViewById(R.id.loginUsername);
            TextView password = (TextView)findViewById(R.id.loginPassword);
            login.setText(valLog);
            password.setText(valPwd);
        }
    }
}
