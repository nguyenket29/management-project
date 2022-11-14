package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "training_system")
@Data
public class TrainingSystem extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
}
