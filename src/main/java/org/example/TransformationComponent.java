package org.example;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;

import org.example.domain.Greeting;

import java.util.Map;

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

}
