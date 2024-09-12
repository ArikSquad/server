package net.swofty.service.generic;

import lombok.RequiredArgsConstructor;
import net.swofty.commons.Configuration;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.service.generic.redis.PingEndpoint;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceRedisManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;

    public void init() {
        System.out.println("Initializing service " + service.getType().name() + "...");
        ItemAttribute.registerItemAttributes();

        /**
         * Register Redis
         */
        ServiceRedisManager.connect(Configuration.get("redis-uri"), service.getType());
        List<ServiceEndpoint> endpoints = new ArrayList<>(service.getEndpoints());
        endpoints.add(new PingEndpoint());

        endpoints.forEach(endpoint -> {
            ProtocolObject protocolObject = endpoint.associatedProtocolObject();

            RedisAPI.getInstance().registerChannel(protocolObject.channel(), message -> {
                System.out.println("Received message: " + message.message);
                // Everything after the first semicolon is the actual message
                String realMessage = message.message.substring(message.message.indexOf(";") + 1);
                ServiceProxyRequest request = ServiceProxyRequest.fromJSON(new JSONObject(realMessage));

                Object messageData = protocolObject.translateFromString(request.getMessage());

                Thread.startVirtualThread(() -> {
                    Object rawResponse = endpoint.onMessage(request, messageData);
                    String response = protocolObject.translateReturnToString(rawResponse);

                    RedisAPI.getInstance().publishMessage(request.getRequestServer(),
                            ChannelRegistry.getFromName(protocolObject.channel()),
                            request.getRequestId() + "}=-=---={" + response);

                    System.out.println("Giving response: " + response);
                    System.out.println("Published message to " + request.getRequestServer());
                    System.out.println("Channel: " + protocolObject.channel());
                });
            });
        });

        RedisAPI.getInstance().startListeners();
        System.out.println("Service " + service.getType().name() + " initialized!");
    }
}
