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
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    //Création de la BDD
    private Users db = new Users(EditActivity.this);
    //Champ id du mot de passe
    private int idPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //Initialisation du radiogroup
        RadioGroup specialsGroup = findViewById(R.id.specials_radiogroup);
        RadioButton specials = (RadioButton)specialsGroup.getChildAt(1);
        specials.setChecked(true);
        //Recupération de l'id du mot de passe pour modification
        this.idPassword = getIntent().getIntExtra("id",0);
        //Recup du contenu
        TextView title = findViewById(R.id.password_title_field);
        title.setText(db.getTitleFromId(this.idPassword));
        TextView content = findViewById(R.id.generated_password_field);
        content.setText(db.getPasswordFromId(this.idPassword));
    }
    @Override //Création du menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    //Appel au Generator pour modifier le mot de passe
    private String regeneratePassword(int nbChar) {
        boolean specials;
        RadioGroup specialsGroup = findViewById(R.id.specials_radiogroup);
        if(((RadioButton)findViewById(specialsGroup.getCheckedRadioButtonId())).getText().equals(getString(R.string.yes)))
            specials = true;
        else
            specials = false;
        Generator gen = new Generator(specials);
        return gen.createPassword(nbChar);

    }

    //Code du bouton de generation du mot de passe
    public void onClickButtonRegen(View v) {
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
            generatedPasswordField.setText(this.regeneratePassword(nbChar));
        }
    }
    //Enregistrement du nouveau mot de passe
    public void onClickButtonValidate(View v) {
        //Recup des TextViews
        EditText generatedPasswordField = findViewById(R.id.generated_password_field);
        EditText titleField = findViewById(R.id.password_title_field);
        //Modif en base de données
        this.db.modifyPassword(generatedPasswordField.getText().toString(), titleField.getText().toString(), this.idPassword);
        //retour sur le main activity
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Retour sur le main activity
    public void onClickButtonCancel(View v) {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
