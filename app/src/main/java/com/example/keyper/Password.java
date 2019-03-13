package com.example.keyper;

public class Password {

    private String lib;
    private String pwd;
    private int id;

    public String getLib() {
        return lib;
    }

    public String getPwd() {
        return pwd;
    }

    public int getId() {
        return id;
    }

    public Password (String lib, String pwd, int id) {
        this.lib = lib;
        this.pwd = pwd;
        this.id = id;
    }
}
