# camel-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework and camel Integration framework
Uses the following technologies.
- Data source with mariadb
- MDC Logging
- Infinispan

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `camel-quarkus-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware Lang it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/camel-quarkus-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/camel-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.


## Deploy directly into Openshift

Use this command to deploy on Openshift (must be logged first)

 mvn clean package oc:build oc:resource oc:apply
 
See the next example if you want to change the base image

            <plugin>
                <groupId>org.eclipse.jkube</groupId>
                <artifactId>openshift-maven-plugin</artifactId>
                <version>1.0.0-alpha-3</version>
                <configuration>
                    <buildStrategy>docker</buildStrategy>
                    <generator>
                        <includes>
                            <include>quarkus</include>
                        </includes>
                        <config>
                            <quarkus>
                                <from>registry.access.redhat.com/openjdk/openjdk-11-rhel7</from>
                            </quarkus>
                        </config>
                    </generator>
                </configuration>
            </plugin>


##### Create image native and push into quay.io
    
    podman build -f src/main/docker/Dockerfile.native -t camel-quarkus .
    podman login quay.io
    podman push camel-quarkus docker://quay.io/jorgecastro05/camel-quarkus
    
##### Run native image on openshift
    
    oc new-app --name quarkus-native --docker-image quay.io/jorgecastro05/camel-quarkus

##### Run native image on kubernetes

    kubectl create deployment quarkus-native --image=quay.io/jorgecastro05/camel-quarkus

For expose the application you can create a service with type `NodePort`:

    kubectl expose deployment quarkus-native --type=NodePort --port=8080
    kubectl get svc

Check the random port and test ex `curl http://localhost:30652/swagger-ui/`

    NAME               TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
    quarkus-native     NodePort   10.43.242.183   <none>        8080:30652/TCP   3s
   
#### Load Openshift console into eclipse che

Execute the task download and configure oc, then open a new terminal and execute the following command

     export PATH=$PATH:$(pwd)/oc-console

    
    

