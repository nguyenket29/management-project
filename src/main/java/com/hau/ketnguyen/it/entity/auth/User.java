
package com.hau.ketnguyen.it.entity.auth;

import com.hau.ketnguyen.it.common.util.JsonUtil;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    public static final class Gender {
        public static final short MALE = 0;
        public static final short FEMALE = 1;
        public static final short OTHER = 2;
    }

    public static final class Status {
        public static final short ACTIVE = 1;
        public static final short WAITING = 0;
        public static final short LOCK = -1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    private Short gender;

    @Column(name = "status")
    private Short status;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<Role>(0);

    @Transient
    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
