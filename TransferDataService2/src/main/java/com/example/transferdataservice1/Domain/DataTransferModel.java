package com.example.transferdataservice1.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTransferModel implements Serializable {

    @Id
    private Long id;

    private String username;

    private String data;

    private String threadId;

    private String status;
}
