package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        //Get TextFields
        TextView login = (TextView) findViewById(R.id.signUpLogin);
        TextView pwd = (TextView) findViewById(R.id.signUpPwd);
        TextView pwdConfirm = (TextView) findViewById(R.id.signUpConfirmPwd);


        if(!login.getText().toString().equals("")) { //Si le login est remplis

            if(!db.isSignedUp(login.getText().toString())) { //Si le login existe en db

                if (!(pwd.getText().toString().equals("") || pwdConfirm.getText().toString().equals(""))) { //Si tout les champs mot de passe sont remplis

                        if (pwd.getText().toString().equals(pwdConfirm.getText().toString())) { //Si les deux mot de passe sont identiques

                            //Alors on inscrit l'utilisateur dans la db
                            db.createAccount(login.getText().toString(), pwd.getText().toString());
                            //Création de l'intent de retour
                            Intent intentReturn = new Intent();
                            //On donne l'id et le mot de passe en paramètres pour le pré-remplir sur l'activité de login
                            intentReturn.putExtra("login", login.getText().toString());
                            intentReturn.putExtra("password", pwd.getText().toString());
                            setResult(1, intentReturn);
                            finish();

                        } else { //Les mot de passe ne correspondent pas

                            pwd.setError(getString(R.string.passwordDoesntMatch));
                            pwdConfirm.setError(getString(R.string.passwordDoesntMatch));

                        }
                } else { //Si un des deux champs mot de passe est vide
                    //Affichage de l'erreur sur celui qui est vide
                    if(pwd.getText().toString().equals("")) {
                        pwd.setError(getString(R.string.allPasswordMissing));
                    }
                    if(pwdConfirm.getText().toString().equals("")) {
                        pwdConfirm.setError(getString(R.string.passwordMissing));
                    }

                }
            } else { //Si le login n'existe pas en db

                login.setError(getString(R.string.loginAlreadyExist));
            }
        } else { // Si le login n'est pas remplis

            login.setError(getString(R.string.loginMissing));
        }



    }
}
