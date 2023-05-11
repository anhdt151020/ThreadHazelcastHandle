package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Repository.DataTransferModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheEventHandleServiceImpl implements CacheEventHandleService{
    private final DataTransferModelRepository dataTransferModelRepository;
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    DataTransferModel res;
    @Override
    public void newEntryCacheHandle(DataTransferModel dataTransferModel) {
        res = dataTransferModel;
        log.info("HANDLING ENTRY CACHE");
        MyRunnable myRunnable = new MyRunnable();
        executorService.execute(myRunnable);
    }

    @Override
    public void updateEntryCacheHandle(DataTransferModel dataTransferModel) {
        log.info("HANDLING UPDATE CACHE");
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {

        }
    }
}
