package com.example.keyper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Users {

    private UsersHelper db;

    public Users(Context context) {
        this.db = new UsersHelper(context);
    }

    public void createAdminAccount() {
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", "admin");
        values.put("password", "$iutinfo");
        dbwritable.insert("User", null, values);

        dbwritable.close();
    }

    public boolean isSignedUp(String loggerUsername) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();

        String[] col = {"username"};
        String[] select = {};
        Cursor curs = dbreadable.query("User", col, "", select, null,null, null);

        if(curs.moveToFirst()) {
            do {
                String username = curs.getString(curs.getColumnIndexOrThrow("username"));
                if (loggerUsername.equals(username)) {
                    return true;
                }
            } while (curs.moveToNext());
        }
        curs.close();
        return false;
    }

    // Return : true = password matches / false = password doesn't match
    public boolean checkPassword(String loggerUsername, String loggerPassword) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();

        String[] col = {"password"};
        String[] select = {loggerUsername};
        Cursor curs = dbreadable.query("User", col, "username=?", select, null,null, null);

        if(curs.moveToFirst()) {
            String password = curs.getString(curs.getColumnIndexOrThrow("password"));
            if(loggerPassword.equals(password)) {
                return true;
            }
        }
        curs.close();
        return false;
    }

    //Compte le nombre de mot de passe d'un utilisateur
    public String[] tabPasswordUser(Integer idUser) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();

        String[] col = {"*"};
        String[] select = {idUser.toString()};
        Cursor curs = dbreadable.query("Password", col, "ID_User=?", select, null,null, null);
        String[] tabPass;
        if(curs.moveToFirst()) {
            tabPass = new String[curs.getCount()];
            int i =0;
            do {
                tabPass[i] = curs.getString(curs.getColumnIndexOrThrow("content"));
                i++;
            } while (curs.moveToNext());
        } else {
            tabPass = new String[0];
        }
        return tabPass;
    }

    public int getId(String userName) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        String[] col = {"ID_User"};
        String[] select = {userName};
        Cursor curs = dbreadable.query("User", col, "username=?", select, null,null, null);
        curs.moveToFirst();
        return curs.getInt(curs.getColumnIndexOrThrow("ID_User"));
    }
}
