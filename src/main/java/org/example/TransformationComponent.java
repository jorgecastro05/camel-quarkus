package org.example;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
@RegisterForReflection
public class TransformationComponent {

    public void removeIdKey(Map result){
        result.remove("id");
    }

}
