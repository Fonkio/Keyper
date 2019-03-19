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

        //Ajout du compte
        values.put("username", "admin");
        values.put("password", "$iutinfo");
        dbwritable.insert("User", null, values);

        //Fermeture
        dbwritable.close();
    }

    //Création d'un compte
    public void createAccount(String username, String password) {
        //Ouverture
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        //Ajout des valeurs
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        dbwritable.insert("User", null, values);
        //Fermeture
        dbwritable.close();
    }

    //Verif id dans la db
    public boolean isSignedUp(String loggerUsername) {
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution requête
        String[] col = {"username"};
        String[] select = {};
        Cursor curs = dbreadable.query("User", col, "", select, null,null, null);
        //Test sur résultat
        if(curs.moveToFirst()) {
            do {
                String username = curs.getString(curs.getColumnIndexOrThrow("username"));
                if (loggerUsername.equals(username)) {
                    return true;
                }
            } while (curs.moveToNext());
        }
        //Fermeture
        curs.close();
        return false;
    }

    // Return : true = mots de passe identiques / false = mots de passe differents
    public boolean checkPassword(String loggerUsername, String loggerPassword) {
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution requête
        String[] col = {"password"};
        String[] select = {loggerUsername};
        Cursor curs = dbreadable.query("User", col, "username=?", select, null,null, null);
        //Consultation des résultats
        if(curs.moveToFirst()) {
            String password = curs.getString(curs.getColumnIndexOrThrow("password"));
            if(loggerPassword.equals(password)) {
                return true;
            }
        }
        //Fermeture
        curs.close();
        return false;
    }

    //Compte le nombre de mot de passe d'un utilisateur
    public ArrayList<Password> listPasswordUser(Integer idUser,String search) {
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution de la requête
        String[] col = {"*"};
        String[] select = {idUser.toString(), "%"+search+"%"};
        Cursor curs = dbreadable.query("Password", col, "ID_User=? and title LIKE ?", select, null,null, null);
        //Création de la liste
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
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution de la requête
        String[] col = {"ID_User"};
        String[] select = {userName};
        Cursor curs = dbreadable.query("User", col, "username=?", select, null,null, null);
        curs.moveToFirst();
        return curs.getInt(curs.getColumnIndexOrThrow("ID_User"));
    }
    //Ajout d'un mot de passe pour un utilisateur
    public void addPassword(String pwd, int idUser, String title){
        //Ouverture
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        //Ajout des données
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", pwd);
        values.put("ID_User", idUser);
        dbwritable.insert("Password", null, values);
        //Fermeture
        dbwritable.close();
    }

    //Retourne le mot de passe en fonction de l'id
    public String getPasswordFromId(int id) {
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution requête
        String[] col = {"content"};
        String[] select = {Integer.toString(id)};
        Cursor curs = dbreadable.query("Password", col, "ID_Password=?", select, null,null, null);
        //Envoi du mot de passe
        if(curs.moveToFirst()) {
            return curs.getString(curs.getColumnIndexOrThrow("content"));
        }
        //fermeture
        curs.close();
        return "Password not found !";
    }

    //Retourne l'id en fonction de l'id
    public String getTitleFromId(int id) {
        //Ouverture
        SQLiteDatabase dbreadable = this.db.getReadableDatabase();
        //Execution requête
        String[] col = {"title"};
        String[] select = {Integer.toString(id)};
        Cursor curs = dbreadable.query("Password", col, "ID_Password=?", select, null,null, null);
        //Retour du résultat
        if(curs.moveToFirst()) {
            return curs.getString(curs.getColumnIndexOrThrow("title"));
        }
        //Fermeture
        curs.close();
        return "Title not found !";
    }

    //suppression d'un mot de passe
    public void removePassword(Integer id) {
        //Ouverture
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        //Execution requête
        String[] delete = {id.toString()};
        dbwritable.delete("Password", "ID_Password=?",delete);
    }

    public void modifyPassword(String content, String title, Integer id) {
        //Ouverture
        SQLiteDatabase dbwritable = this.db.getWritableDatabase();
        //Modif des données
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("title", title);
        String[] select = {id.toString()};
        dbwritable.update("Password",cv,"ID_Password=?", select);
    }
}
