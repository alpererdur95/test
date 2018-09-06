package com.example.demo.models;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "chimneyprivilegelist")
public class PrivilegesList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "privilegename")
    private String privilegename;

    public PrivilegesList() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegename() {
        return privilegename;
    }
    public void setPrivilegename(String privilegename) {
        this.privilegename = privilegename;
    }
}
