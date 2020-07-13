package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Greeting {

    @JsonProperty
    private String message;

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
    
}