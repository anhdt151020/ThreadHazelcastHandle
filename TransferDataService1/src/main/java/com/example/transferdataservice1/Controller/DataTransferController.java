package com.example.transferdataservice1.Controller;

import com.example.transferdataservice1.Domain.DataTransferModel;
import com.example.transferdataservice1.Service.DataTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
@Slf4j
public class DataTransferController {

    private final DataTransferService dataTransferService;
    private final String CLASS_NAME = DataTransferController.class.getSimpleName();


    @PostMapping("/make")
    public ResponseEntity<DataTransferModel> makeDataTransfer(@RequestBody DataTransferModel dataTransferModel){
        log.info("MAKE DATA FOR TRANSFER FROM {} - DATA {}", CLASS_NAME, dataTransferModel);
        return ResponseEntity.ok(dataTransferService.makeDataTransfer(dataTransferModel));
    }

    @PutMapping("/update")
    public ResponseEntity<DataTransferModel> updateDataTransfer(@RequestBody DataTransferModel dataTransferModel){
        log.info("MAKE DATA FOR TRANSFER FROM {} - DATA {}", CLASS_NAME, dataTransferModel);
        return ResponseEntity.ok(dataTransferService.updateDataTransfer(dataTransferModel));
    }

    @PostMapping("/clear-cache")
    public void clearCacheDataTransfer(@RequestBody DataTransferModel dataTransferModel){
        log.info("CLEAR CACHE REQUEST FROM {}", CLASS_NAME);
        dataTransferService.clearCache(dataTransferModel);
    }

    @PostMapping("/clear-all-cache")
    public void clearCacheDataTransfer(){
        log.info("CLEAR ALL CACHE REQUEST FROM {}", CLASS_NAME);
        dataTransferService.clearAllCache();
    }

}
