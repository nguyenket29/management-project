package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "files")
@Data
public class Files extends BaseEntity {
    @Column(name = "content_type")
    private String contentType;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "extention")
    private String extention;

    @Column(name = "type")
    private String type;
}
