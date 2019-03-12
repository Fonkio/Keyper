package com.example.keyper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private Users db = new Users(AddActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    private String generatePassword(int nbChar) {
        Generator gen = new Generator();
        return gen.createPassword(nbChar);

    }

    public void onClickButtonOk(View v) {
        EditText charNumberField = (EditText)findViewById(R.id.char_number_passwd);
        EditText generatedPasswordField = (EditText)findViewById(R.id.generated_password);
        Button validate = (Button)findViewById(R.id.button_validate);

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
        EditText generatedPasswordField = (EditText)findViewById(R.id.generated_password);
        EditText titleField = (EditText)findViewById(R.id.title_Password);
        this.db.addPassword(generatedPasswordField.getText().toString(), MainActivity.userLogID, titleField.getText().toString() );

        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}