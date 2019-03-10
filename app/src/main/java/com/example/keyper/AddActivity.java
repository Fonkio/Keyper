package com.example.keyper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

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
        int nbChar = Integer.parseInt(charNumberField.getText().toString());
        generatedPasswordField.setText(this.generatePassword(nbChar));
    }
}
