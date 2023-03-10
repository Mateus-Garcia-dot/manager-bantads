package com.bantads.manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerModel {
    @Id
    private String uuid = java.util.UUID.randomUUID().toString();

    private String name;
    private String cpf;
    private String telephone;
}
