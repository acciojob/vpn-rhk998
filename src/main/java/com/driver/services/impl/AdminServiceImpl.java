package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        List<ServiceProvider> serviceProviderList = new ArrayList<>();
        admin.setServiceProviders(serviceProviderList);
        return adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Optional<Admin> adminOptional = adminRepository1.findById(adminId);
        if(adminOptional.isPresent()){
            Admin admin= adminOptional.get();
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(providerName);
            List<Country> countryList = new ArrayList<>();
            serviceProvider.setCountryList(countryList);
            serviceProvider.setAdmin(admin);
            admin.getServiceProviders().add(serviceProvider);
            return adminRepository1.save(admin);
        }
        return null;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
         boolean isCountryPresent = false;

        String str = countryName.toUpperCase();
        if(!str.equals("IND") && !str.equals("JPN") && !str.equals("AUS") && !str.equals("CHI") && !str.equals("USA")){
            throw  new Exception("Country not found");
        }
        Optional<ServiceProvider> serviceProviderOptional  = serviceProviderRepository1.findById(serviceProviderId);
        if (!serviceProviderOptional.isPresent()) {
            throw new Exception("Service Provider not found"); // Or a custom exception
        }
        ServiceProvider serviceProvider = serviceProviderOptional.get();
        Country country = new Country(CountryName.valueOf(str), CountryName.valueOf(str).toCode());


        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);

        serviceProviderRepository1.save(serviceProvider);

        return serviceProvider;
    }
}
