package com.actech.uber.services.messagequeue;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Service
public class MessageQueue {
    private final Map<String, Queue<MQMessage>> topics = new HashMap<>();

    public void sendMessage(String topic, MQMessage message) {
        System.out.printf("KafakeService: appended to %s: %s", topic, message.toString());
        topics.putIfAbsent(topic, new LinkedList<>());
        topics.get(topic).add(message);
    }

    public MQMessage consumeMessage(String topic) {
        MQMessage message = topics.getOrDefault(topic, new LinkedList<>()).poll();
        System.out.printf("Kafake consuming from %s: %s", topic, message.toString());
        return message;
    }
}
