package com.driver.model;// Note: Do not write @Enumerated annotation above CountryName in this model.
import com.driver.model.CountryName;
import com.driver.model.User;

import javax.persistence.*;
import java.util.List;

public class Country{
    private int id;
    private String name;
    private CountryName countryName;
    private String code;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}