package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddActivity extends AppCompatActivity {

    //Création de la BDD
    private Users db = new Users(AddActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add); //Association du layout activity_add
        RadioGroup specialsGroup = findViewById(R.id.specials_radiogroup); //Recupération du radioGroup
        //Recupération du radioButton "Oui/Yes" du radiogroup
        RadioButton specials = (RadioButton)specialsGroup.getChildAt(1);
        //On met selectionne de base le radioButton "Oui/yes" du radioGroup
        specials.setChecked(true);
    }

    @Override //Creation du menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    //Méthode de génération d'un MdP
    private String generatePassword(int nbChar) {
        boolean specials; //Variable pour l'utilisation des caratères spéciaux ou non.
        RadioGroup specialsGroup = findViewById(R.id.specials_radiogroup); //Recupération du radioGroup
        //Si le radioButton "Oui/Yes" est séléctionné
        if(((RadioButton)findViewById(specialsGroup.getCheckedRadioButtonId())).getText().equals(getString(R.string.yes)))
            specials = true; //On set le booleen à true
        else
            specials = false; //Sinon on le set a false
        Generator gen = new Generator(specials);//On créé le générateur de MdP en fonction du boolean
        return gen.createPassword(nbChar); //On retourne le MdP généré en fonction de la longueur souhaitée

    }

    public void onClickButtonOk(View v) {
        EditText charNumberField = findViewById(R.id.password_length_field);
        EditText generatedPasswordField = findViewById(R.id.generated_password_field);
        Button validate = findViewById(R.id.button_validate);

        if(!charNumberField.getText().toString().equals("")) {
            int nbChar = Integer.parseInt(charNumberField.getText().toString());

            //Active le bouton valider si le mot de passe est > 0
            if (nbChar > 0) {
                validate.setEnabled(true);
            } else {
                validate.setEnabled(false);
            }
            generatedPasswordField.setText(this.generatePassword(nbChar));
        }
    }

    public void onClickButtonValidate(View v) {
        EditText generatedPasswordField = findViewById(R.id.generated_password_field);
        EditText titleField = findViewById(R.id.password_title_field);
        this.db.addPassword(generatedPasswordField.getText().toString(), MainActivity.userID, titleField.getText().toString() );

        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickButtonCancel(View v) {
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
