package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Domain.DataTransferModel;
import org.springframework.http.ResponseEntity;

public interface DataTransferService {
    DataTransferModel makeDataTransfer(DataTransferModel dataTransferModel) throws InterruptedException;

    DataTransferModel updateDataTransfer(DataTransferModel dataTransferModel);

    void clearCache(DataTransferModel dataTransferModel);

    void clearAllCache();
}
