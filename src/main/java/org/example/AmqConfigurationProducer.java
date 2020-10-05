package org.example;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.Optional;

@Dependent
@RegisterForReflection
public class AmqConfigurationProducer {

    @ConfigProperty(name = "amq.producer.hostname")
    String hostname;
    @ConfigProperty(name = "amq.producer.port")
    String port;
    @ConfigProperty(name = "amq.producer.hostnameFailover")
    Optional<String> hostnameFailover;
    @ConfigProperty(name = "amq.producer.portFailover")
    Optional<String> portFailover;
    @ConfigProperty(name = "amq.producer.username")
    String username;
    @ConfigProperty(name = "amq.producer.password")
    String password;

    @Produces
    @Named("activemq-producer-connection-factory")
    public ActiveMQConnectionFactory AmqConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        String brokerURL;
        if (hostnameFailover.isPresent() && portFailover.isPresent()) {
            brokerURL = "failover:(tcp://" + hostname + ":" + port + ",tcp://" + hostnameFailover.get() + ":" + portFailover.get() + ")?maxReconnectAttempts=3";
        } else {
            brokerURL = "tcp://" + hostname + ":" + port;
        }

        connectionFactory.setBrokerURL(brokerURL);
        connectionFactory.setUserName(username);
        connectionFactory.setPassword(password);

        return connectionFactory;
    }

    @Produces
    @Named("activemqproducer-component")
    public ActiveMQComponent activeMQComponent() {
        ActiveMQComponent amqComp = new ActiveMQComponent();
        amqComp.setConnectionFactory(AmqConnectionFactory());
        return amqComp;
    }


}