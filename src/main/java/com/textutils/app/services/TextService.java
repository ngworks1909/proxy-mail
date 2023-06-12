package com.textutils.app.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.textutils.app.entities.Email;

public interface TextService {
    
    public String translate(String text,String language) throws UnirestException;

    public String sendMail(Email email);
}
