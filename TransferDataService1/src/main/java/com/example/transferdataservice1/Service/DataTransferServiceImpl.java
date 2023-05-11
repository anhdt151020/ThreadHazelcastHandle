package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Config.HazelcastInstanceConfiguration;
import com.example.transferdataservice1.Controller.DataTransferController;
import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Repository.DataTransferModelRepository;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataTransferServiceImpl implements DataTransferService{
    private final String CLASS_NAME = DataTransferServiceImpl.class.getSimpleName();
    private final DataTransferModelRepository dataTransferModelRepository;
    private final HazelcastInstanceConfiguration hazelcastInstanceConfiguration;
    @Override
    public DataTransferModel makeDataTransfer(DataTransferModel dataTransferModel) {
        IMap<String, Object> dataMap = hazelcastInstanceConfiguration.dataMap();
        String key = "data_key_" + dataTransferModel.getUsername();
        log.info("CALLING MAKE DATA Class {} data key {}", CLASS_NAME, key);
        DataTransferModel temp = (DataTransferModel) dataMap.get(key);
        if (temp != null){
            log.info("Class {} data is AVAILABLE with key {}, data {}, CANCEL PUSH CACHE", CLASS_NAME, key, temp);
            return temp;
        } else {
            log.info("Class {} data is NULL with key {}, PUSHING CACHE", CLASS_NAME, key);
            dataMap.put(key, dataTransferModel);
            log.info("PUSHED CACHE with key {}", key);
        }
        return null;
    }

    @Override
    public DataTransferModel updateDataTransfer(DataTransferModel dataTransferModel) {
        IMap<String, Object> dataMap = hazelcastInstanceConfiguration.dataMap();
        String key = "data_key_" + dataTransferModel.getUsername();

        log.info("CALLING UPDATE DATA Class {} data key {}", CLASS_NAME, key);
        log.info("CHECK DATA AVAILABLE IN CACHE ?");

        DataTransferModel temp = (DataTransferModel) dataMap.get(key);

        if (temp != null){
            log.info("Class {} data is AVAILABLE with key {}, data {}, UPDATE DATA IN CACHE", CLASS_NAME, key, temp);
            dataMap.put(key, dataTransferModel);

            log.info("REQUEST SAVE DATA TO DATABASE");
            DataTransferModel res = dataTransferModelRepository.save(dataTransferModel);
            log.info("SAVED DATA TO DATABASE");
            return res;
        } else {
            log.info("INSERT DATA FIRST !!!");
        }
        return null;
    }

    @Override
    public void clearCache(DataTransferModel dataTransferModel) {
        IMap<String, Object> dataMap = hazelcastInstanceConfiguration.dataMap();
        String key = "data_key_" + dataTransferModel.getUsername();
        DataTransferModel temp = (DataTransferModel) dataMap.get(key);

        if (temp != null) {
            log.info("CLEAR CACHE WITH KEY: {}", key);
            dataMap.evict(key);
            log.info("CACHE IS CLEARED !!!");
        }
    }

    @Override
    public void clearAllCache() {
        IMap<String, Object> dataMap = hazelcastInstanceConfiguration.dataMap();
        log.info("REQUEST CLEAR ALL CACHE !!!");
        dataMap.evictAll();
        log.info("CLEARED ALL CACHE !!!");
    }
}
