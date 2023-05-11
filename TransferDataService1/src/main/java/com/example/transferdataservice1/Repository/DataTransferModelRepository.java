package com.example.transferdataservice1.Repository;

import com.example.transferdataservice1.Domain.DataTransferModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTransferModelRepository extends JpaRepository<DataTransferModel, Long> {
}
