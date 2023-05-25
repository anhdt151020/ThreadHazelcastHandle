package com.example.transferdataservice1.Config;

import com.example.transferdataservice1.Domain.DataTransferModel;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastInstanceConfiguration {

    final ApplicationContext applicationContext;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    public IMap<String, Object> dataMap(){
        return hazelcastInstance.getMap("data-map");
    }

    public IMap<String, Object> keyMap(){
        return hazelcastInstance.getMap("key-map");
    }
    public ITopic<DataTransferModel> dataTopic(){
        return hazelcastInstance.getTopic("data-topic");
    }

    public IQueue<DataTransferModel> dataQueue(){
        return hazelcastInstance.getQueue("data-queue");
    }

    @Bean
    public void listenerConfig(){
        IMap<String, Object> dataMap = dataMap();
        dataMap.addEntryListener(applicationContext.getBean(HazelcastListenerSubscriberConfiguration.class), true);
    }
}
