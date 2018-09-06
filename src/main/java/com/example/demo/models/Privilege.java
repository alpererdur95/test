package com.example.demo.models;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="chimneyprivilege")
public class Privilege implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "privilegename",unique = true)
    private String privilegename;
    @Column(name = "resourcename")
  private String resourcename;
    public Privilege(){
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getResourcename() {
        return resourcename;
    }
    public void setResourcename(String rescourcename) {
        this.resourcename = rescourcename;
    }

    public String getPrivilegename() {
        return privilegename;
    }
    public void setPrivilegename(String privilegename) {
        this.privilegename = privilegename;
    }


}
