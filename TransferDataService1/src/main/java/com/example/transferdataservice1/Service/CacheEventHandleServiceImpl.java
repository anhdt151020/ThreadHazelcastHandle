package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Repository.DataTransferModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class CacheEventHandleServiceImpl implements CacheEventHandleService{
    private final DataTransferModelRepository dataTransferModelRepository;
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    DataTransferModel res;


    @Override
    @Async
    public void newEntryCacheHandle(DataTransferModel dataTransferModel, boolean flag) {
        res = dataTransferModel;
        log.info("HANDLING ENTRY CACHE");
        MyRunnable myRunnable = new MyRunnable(dataTransferModel);
        executorService.execute(myRunnable);
    }

    @Override
    @Async
    public void updateEntryCacheHandle(DataTransferModel dataTransferModel) {
        log.info("HANDLING UPDATE CACHE");
        Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
        setOfThread.stream()
                .filter(o->o.getId() == Long.parseLong(res.getThreadId()))
                .findAny()
                .ifPresent(Thread::interrupt);

    }

    static class MyRunnable implements Runnable {
        final DataTransferModel dataTransferModel;
        public MyRunnable(DataTransferModel dataTransferModel) {
            this.dataTransferModel = dataTransferModel;
        }

        @Override
        public void run() {
            for (long i = 0; i < 10; i++){
                this.dataTransferModel.setThreadId(String.valueOf(Thread.currentThread().getId()));
                System.out.println("Hello Đại " + this.dataTransferModel.toString() + i);
            }

        }
    }
}
