package org.example;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class TransformationComponent {

    public void removeIdKey(Map result){
        result.remove("id");
    }

}
