package com.example.transferdataservice1.Config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastInstanceConfiguration {

    final ApplicationContext applicationContext;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    public IMap<String, Object> dataMap(){
        return hazelcastInstance.getMap("data-map");
    }

    public IMap<String, Object> authMap(){
        return hazelcastInstance.getMap("auth-map");
    }

    @Bean
    public void listenerConfig(){
        IMap<String, Object> dataMap = dataMap();
        dataMap.addEntryListener(applicationContext.getBean(HazelcastListenerSubscriberConfiguration.class), true);
    }
}
