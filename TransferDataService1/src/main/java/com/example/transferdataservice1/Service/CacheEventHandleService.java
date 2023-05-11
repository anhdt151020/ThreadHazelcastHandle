package com.example.transferdataservice1.Service;

import com.example.transferdataservice1.Domain.DataTransferModel;

public interface CacheEventHandleService {
    void newEntryCacheHandle(DataTransferModel dataTransferModel);

    void updateEntryCacheHandle(DataTransferModel dataTransferModel);
}
