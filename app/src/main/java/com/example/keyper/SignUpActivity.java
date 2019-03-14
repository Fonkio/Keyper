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

    public void onClickCancel(View view) {
        Intent intentReturn = new Intent();
        intentReturn.putExtra("login", "");
        setResult(1,intentReturn);
        finish();
    }

    public void onClickValidate(View view) {

        TextView login = (TextView) findViewById(R.id.signUpLogin);
        TextView pwd = (TextView) findViewById(R.id.signUpPwd);
        TextView pwdConfirm = (TextView) findViewById(R.id.signUpConfirmPwd);
        if(!login.getText().toString().equals("")) {
            if(!db.isSignedUp(login.getText().toString())) {
                if (!(pwd.getText().toString().equals("") || pwdConfirm.getText().toString().equals(""))) {
                    if (pwd.getText().toString().equals(pwdConfirm.getText().toString())) {
                        db.createAccount(login.getText().toString(), pwd.getText().toString());
                        Intent intentReturn = new Intent();
                        intentReturn.putExtra("login", login.getText().toString());
                        intentReturn.putExtra("password", pwd.getText().toString());
                        setResult(1, intentReturn);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, R.string.passwordDoesntMatch, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.passwordMissing, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, R.string.loginAlreadyExist,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignUpActivity.this, R.string.loginMissing,Toast.LENGTH_SHORT).show();
        }



    }
}
