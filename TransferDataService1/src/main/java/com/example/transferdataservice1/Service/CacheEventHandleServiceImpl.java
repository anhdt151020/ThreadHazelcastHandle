package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Config.HazelcastInstanceConfiguration;
import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Repository.DataTransferModelRepository;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class CacheEventHandleServiceImpl implements CacheEventHandleService{
    private final DataTransferModelRepository dataTransferModelRepository;
    private final HazelcastInstanceConfiguration hazelcastInstanceConfiguration;
    final String SERVICE_NAME = "TRANSFER_1";
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    DataTransferModel res;


    @Override
    @Async
    public void newEntryCacheHandle(DataTransferModel dataTransferModel, boolean flag) throws InterruptedException {
        log.info("HANDLING ENTRY CACHE");
        res = hazelcastInstanceConfiguration.dataQueue().take();
        log.info("res = {}", res);
        hazelcastInstanceConfiguration
                .keyMap().put(SERVICE_NAME+"_"+res.getId()+"_"+res.getUsername(), "Running", 32000, TimeUnit.MILLISECONDS);
        executorService.execute(new MyRunnable(res));
    }

    @Override
    public void updateEntryCacheHandle(DataTransferModel dataTransferModel) {
        log.info("HANDLING UPDATE CACHE");
        checkStateAndInterrupt(dataTransferModel);
    }

    @Override
    @Async
    public void evictEntryCacheHandle(DataTransferModel dataTransferModel) {
        log.info("HANDLING EVICT CACHE");
        checkStateAndInterrupt(dataTransferModel);
    }

    private void checkStateAndInterrupt(DataTransferModel dataTransferModel) {
        String state = (String) hazelcastInstanceConfiguration
                .keyMap().get(SERVICE_NAME+"_"+dataTransferModel.getId()+"_"+dataTransferModel.getUsername());
        if (state != null) {
            Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
            setOfThread.stream()
                    .filter(o ->
                            //log.info("Thread id = {}", res.getThreadId());
                            String.valueOf(o.getId()).equals(res.getThreadId())
                    )
                    .findAny()
                    .ifPresent(Thread::interrupt);
        }
    }

    class MyRunnable implements Runnable {
        final DataTransferModel dataTransferModel;
        public MyRunnable(DataTransferModel dataTransferModel) {
            this.dataTransferModel = dataTransferModel;
        }

        @Override
        public void run() {
            try {
                this.dataTransferModel.setThreadId(String.valueOf(Thread.currentThread().getId()));
                log.info("Running Thread id = {}", res.getThreadId());
                log.info("sleeping");
                Thread.sleep(32000);
                log.info("wakeup HERE !!!!");

                String key = "data_key_" + this.dataTransferModel.getUsername();

                log.info("PROCESS DATA FAILED, SAVING DATA TO DATABASE");
                dataTransferModelRepository.save(dataTransferModel);
                log.info("SAVED {}", dataTransferModel);

                hazelcastInstanceConfiguration.dataMap().evict(key);

            } catch (InterruptedException e) {
                log.info("interrupted");
            }

        }
    }
}
