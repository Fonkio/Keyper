package com.example.keyper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "users.db";

    public static final String CREATE_USER = "CREATE TABLE User (ID_User INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);",
                                DROP_USER = "DROP TABLE IF EXISTS User;",
                                CREATE_PASSWORD = "CREATE TABLE Password (ID_Password INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, FOREIGN KEY (ID_User) REFERENCES User(ID_User));",
                                DROP_PASSWORD = "DROP TABLE IF EXISTS Password;";



    public UsersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_PASSWORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
        db.execSQL(DROP_USER);
        db.execSQL(DROP_PASSWORD);
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVers, int newVers) {
        this.onUpgrade(db, oldVers, newVers);
    }
}
