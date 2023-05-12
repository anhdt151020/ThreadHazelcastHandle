package com.example.transferdataservice1.Config;

import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Service.CacheEventHandleService;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HazelcastListenerSubscriberConfiguration implements
        EntryAddedListener<String, Object>,
        EntryUpdatedListener<String, Object>,
        EntryEvictedListener<String, Object>{

    private final CacheEventHandleService cacheEventHandleService;
    @Override
    public void entryAdded(EntryEvent<String, Object> event) {
        DataTransferModel dataTransferModel = (DataTransferModel) event.getValue();
        log.info("ALERT, NEW DATA ADD TO CACHE {}", dataTransferModel);
        cacheEventHandleService.newEntryCacheHandle(dataTransferModel, Boolean.TRUE);
    }

    @Override
    public void entryUpdated(EntryEvent<String, Object> event) {
        DataTransferModel dataTransferModel = (DataTransferModel) event.getValue();
        log.info("ALERT, NEW DATA UPDATE TO CACHE {}", dataTransferModel);
        cacheEventHandleService.updateEntryCacheHandle(dataTransferModel);
    }

    @Override
    public void entryEvicted(EntryEvent<String, Object> event) {
        DataTransferModel dataTransferModel = (DataTransferModel) event.getValue();
        log.info("ALERT, DATA EVICTED TO CACHE {}", event);
        cacheEventHandleService.evictEntryCacheHandle(dataTransferModel);
    }
}
