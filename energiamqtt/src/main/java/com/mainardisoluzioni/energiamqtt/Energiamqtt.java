/*
 * Copyright (C) 2024 Davide Mainardi <davide@mainardisoluzioni.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.energiamqtt;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient.Mqtt5Publishes;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davide Mainardi <davide@mainardisoluzioni.com>
 */
public class Energiamqtt {
    
    private final Mqtt5BlockingClient client;
    private final String topic;

    public Energiamqtt(String indirizzoBroker, String topic) {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(indirizzoBroker)
                .buildBlocking();
        this.topic = topic;
    }
    
    public void sottoscrivitiEAspetta() {
        if (topic != null && !topic.isBlank()) {
            client.connect();
            Mqtt5SubAck mqtt5SubAck = null;
            try (final Mqtt5Publishes publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {

                mqtt5SubAck = client.subscribeWith().topicFilter(topic).qos(MqttQos.AT_LEAST_ONCE).send();

                Optional<Mqtt5Publish> messaggioOptional = publishes.receive(1, TimeUnit.MINUTES);
                if (messaggioOptional.isPresent())
                    System.out.println("Il valore del payload ricevuto è: " + new String(messaggioOptional.get().getPayloadAsBytes()));

            } catch (InterruptedException ex) {
                Logger.getLogger(Energiamqtt.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (mqtt5SubAck != null)
                    client.unsubscribeWith().topicFilter(topic).send();
                client.disconnect();
            }
        }
    }
    
    public void pubblica() {
        if (topic != null && !topic.isBlank()) {
            client.connect();
            client.publishWith()
                    .topic(topic)
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .payload("Se mi leggi vuole dire che funziona tutto!".getBytes())
                    .send();
            client.disconnect();
        }
    }
}
