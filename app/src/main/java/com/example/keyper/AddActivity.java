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
        Generator generator = new Generator(specials);//On créé le générateur de MdP en fonction du boolean
        return generator.createPassword(nbChar); //On retourne le MdP généré en fonction de la longueur souhaitée

    }

    //Sur le click du ButtonImage de génération du MdP
    public void onClickButtonOk(View v) {
        //Recupération des champs et du bouton de validation
        EditText passwordLengthField = findViewById(R.id.password_length_field);
        EditText generatedPasswordField = findViewById(R.id.generated_password_field);
        Button validateButton = findViewById(R.id.button_validate);

        //Si le champ de la longueur du MdP est rempli
        if(!passwordLengthField.getText().toString().equals("")) {
            int passwordLength = Integer.parseInt(passwordLengthField.getText().toString());//On recupère le nombre de chars souhaité
            validateButton.setEnabled(false);
            if(passwordLength > 50) {
                passwordLengthField.setError(getString(R.string.password_too_long));
            }
            else {
                //Active le bouton valider si la longueur du MdP est > 0
                if (passwordLength > 0) {
                    validateButton.setEnabled(true);
                }
                generatedPasswordField.setText(this.generatePassword(passwordLength)); //On génère un MdP dans le champ destiné au MdP
            }
        }
    }

    //Sur le click du bouton de validation
    public void onClickButtonValidate(View v) {
        //Recupération des champs
        EditText generatedPasswordField = findViewById(R.id.generated_password_field);
        EditText passwordTitleField = findViewById(R.id.password_title_field);
        //On ajoute le Mdp à la BDD
        this.db.addPassword(generatedPasswordField.getText().toString(), MainActivity.userID, passwordTitleField.getText().toString() );

        //On change d'activité (vers MainActivity)
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Sur le click du bouton d'Annulation
    public void onClickButtonCancel(View v) {
        //On change d'activité (vers MainActivity)
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
