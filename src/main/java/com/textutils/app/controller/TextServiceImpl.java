package com.textutils.app.controller;

import com.mashape.unirest.http.JsonNode;

import org.json.JSONArray;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.textutils.app.entities.Email;
import com.textutils.app.services.TextService;

public class TextServiceImpl implements TextService{

    private String apikey;
    

    @Override
    public String translate(String text, String language) throws UnirestException {
        
        HttpResponse<JsonNode> response = Unirest.post("https://rapid-translate-multi-traduction.p.rapidapi.com/t")
	.header("content-type", "application/json")
	.header("X-RapidAPI-Key", "5c192f32f1mshfb1d57b40e0417bp1a11b4jsn7dce7b721ab6")
	.header("X-RapidAPI-Host", "rapid-translate-multi-traduction.p.rapidapi.com")
	.body("{\r\"from\": \"en\",\r\"to\": \""+language+"\",\r\"e\": \"\",\r\"q\": \""+text+"\"\r}")
	.asJson();
       JSONArray newtext=response.getBody().getArray();
       text=(String) newtext.get(0);
     
        return text;
    }

    @Override
    public String sendMail(Email email) {
    String res="";
    String query="{\"personalizations\": [{\"to\": [{\"email\": \""+email.getDestination()+"\"}],\"subject\": \""+email.getSubject()+"\"}],\"from\": {\"email\": \""+email.getSource()+"\"},\"content\": [{\"type\": \"text/plain\",\"value\": \""+email.getText()+"\"}]}";

            try {
                HttpResponse<String> response = Unirest.post("https://rapidprod-sendgrid-v1.p.rapidapi.com/mail/send")
                    .header("content-type", "application/json")
                    .header("X-RapidAPI-Key", getApikey())
                    .header("X-RapidAPI-Host", "rapidprod-sendgrid-v1.p.rapidapi.com")
                    .body(query)
                    .asString();
                res=response.toString();
                } 
                catch (UnirestException e) {
                  System.out.println("Some error occured while sending mail.Please Try Again");
                }
                return res;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
    
}
