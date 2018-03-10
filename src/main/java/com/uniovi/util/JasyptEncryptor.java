package com.uniovi.util;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * Reused from last year's Agents module.
 * 
 * Created by Nicolás on 18/02/2017.
 */
public class JasyptEncryptor {

    private StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    public boolean checkPassword(String inputPassword, String encryptedPassword){
        return encryptor.checkPassword(inputPassword, encryptedPassword);
    }

    public String encryptPassword(String password){
        String pass = encryptor.encryptPassword(password);
        return pass;
    }
}