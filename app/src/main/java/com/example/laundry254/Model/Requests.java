package com.example.laundry254.Model;

import java.util.Date;

public class Requests {

    String name, location, description, status, provider_id, customer_id;
    Date requestDate, pickDate;

    public Requests() {

    }

    public Requests(String name, String location, String description, String status, String provider_id, String customer_id, Date requestDate, Date pickDate) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.status = status;
        this.provider_id = provider_id;
        this.customer_id = customer_id;
        this.requestDate = requestDate;
        this.pickDate = pickDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getPickDate() {
        return pickDate;
    }

    public void setPickDate(Date pickDate) {
        this.pickDate = pickDate;
    }
}
