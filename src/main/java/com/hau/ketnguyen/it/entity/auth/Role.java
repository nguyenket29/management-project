package com.hau.ketnguyen.it.entity.auth;

import com.hau.ketnguyen.it.common.util.JsonUtil;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "type")
  private String type;

  public Role() {}

  public Role(Integer id) {
    this.id = id;
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  @Override
  public String toString() {
    return JsonUtil.toJson(this);
  }
}
