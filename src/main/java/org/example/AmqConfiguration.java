package org.example;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.util.Optional;

@Dependent
@RegisterForReflection
public class AmqConfiguration {

    @ConfigProperty(name = "amq.hostname")
    String hostname;
    @ConfigProperty(name = "amq.port")
    String port;
    @ConfigProperty(name = "amq.hostnameFailover")
    Optional<String> hostnameFailover;
    @ConfigProperty(name = "amq.portFailover")
    Optional<String> portFailover;
    @ConfigProperty(name = "amq.username")
    String username;
    @ConfigProperty(name = "amq.password")
    String password;
    @ConfigProperty(name = "amq.transacted")
    Boolean transacted;
    @ConfigProperty(name = "amq.concurrentConsumers")
    Integer concurrentConsumers;
    @ConfigProperty(name = "amq.initialRedeliveryDelay")
    Long initialRedeliveryDelay;
    @ConfigProperty(name = "amq.redeliveryDelay")
    Long redeliveryDelay;
    @ConfigProperty(name = "amq.backOffMultiplier")
    Long backOffMultiplier;
    @ConfigProperty(name = "amq.useExponentialBackOff")
    Boolean useExponentialBackOff;
    @ConfigProperty(name = "amq.maximumRedeliveries")
    Integer maximumRedeliveries;

    @Produces
    @Named("activemq-connection-factory")
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

        RedeliveryPolicy policy = connectionFactory.getRedeliveryPolicy();
        policy.setInitialRedeliveryDelay(initialRedeliveryDelay);
        policy.setRedeliveryDelay(redeliveryDelay);
        policy.setBackOffMultiplier(backOffMultiplier);
        policy.setUseExponentialBackOff(useExponentialBackOff);
        policy.setMaximumRedeliveries(maximumRedeliveries);

        return connectionFactory;
    }

    @Produces
    @Named("activemq-component")
    public ActiveMQComponent activeMQComponent() {
        ActiveMQComponent amqComp = new ActiveMQComponent();
        amqComp.setTransacted(transacted);
        amqComp.setLazyCreateTransactionManager(true);
        amqComp.setCacheLevelName("CACHE_CONSUMER");
        amqComp.setConnectionFactory(AmqConnectionFactory());
        amqComp.setConcurrentConsumers(concurrentConsumers);
        return amqComp;
    }


}
