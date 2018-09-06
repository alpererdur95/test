package com.example.demo.models;

import java.util.List;
public class ArrivalPrivilege {
    private Integer uid;
    private List<String> privilegesLists;
    private String resourceName;

    public List<String> getPrivilegesLists() {
        return privilegesLists;
    }
    public void setPrivilegesLists(List<String> privilegesLists) {
        this.privilegesLists = privilegesLists;
    }

    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
