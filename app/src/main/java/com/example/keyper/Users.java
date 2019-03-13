package com.example.keyper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Users {

    private UsersHelper db;

    public Users(Context context) {
        this.db = new UsersHelper(context);
    }

    //Création compte administrateur
    public void createAdminAccount() {
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", "admin");
        values.put("password", "$iutinfo");
        dbwritable.insert("User", null, values);

        dbwritable.close();
    }

    //Verif id dans la db
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
    public ArrayList<Password> tabPasswordUser(Integer idUser) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();

        String[] col = {"*"};
        String[] select = {idUser.toString()};
        Cursor curs = dbreadable.query("Password", col, "ID_User=?", select, null,null, null);
        ArrayList<Password> l = new ArrayList<>();
        if(curs.moveToFirst()) {
            int i =0;
            do {
                l.add(new Password(curs.getString(curs.getColumnIndexOrThrow("title")),MainActivity.hidePassword(curs.getString(curs.getColumnIndexOrThrow("content")).length()),curs.getInt(curs.getColumnIndexOrThrow("ID_Password"))));
                i++;
            } while (curs.moveToNext());
        }
        return l;
    }

    //Donne l'id à partir du username
    public int getId(String userName) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        String[] col = {"ID_User"};
        String[] select = {userName};
        Cursor curs = dbreadable.query("User", col, "username=?", select, null,null, null);
        curs.moveToFirst();
        return curs.getInt(curs.getColumnIndexOrThrow("ID_User"));
    }

    public void addPassword(String pwd, int idUser, String title){
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("content", pwd);
        values.put("ID_User", idUser);
        dbwritable.insert("Password", null, values);

        dbwritable.close();
    }

    public String getPasswordFromId(int id) {
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();

        String[] col = {"content"};
        String[] select = {Integer.toString(id)};
        Cursor curs = dbreadable.query("Password", col, "ID_Password=?", select, null,null, null);

        if(curs.moveToFirst()) {
            return curs.getString(curs.getColumnIndexOrThrow("content"));
        }
        curs.close();
        return "Password not found !";
    }
}
