package org.example;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Body;
import org.example.domain.Greeting;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterForReflection
public class TransformationComponent {

    public Greeting sayHello(){
        Greeting greeting = new Greeting();
        greeting.setMessage("Hello world !!");
        return greeting;
    }

    public Greeting sayBye(){
        Greeting greeting = new Greeting();
        greeting.setMessage("Bye !!");
        return greeting;
    }

    public Greeting sayMessage(@Body String bodyMessage){
        Greeting greeting = new Greeting();
        greeting.setMessage(bodyMessage);
        return greeting;
    }



}
