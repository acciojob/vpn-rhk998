package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository1;
    private final ServiceProviderRepository serviceProviderRepository1;
    private final CountryRepository countryRepository1;
    private final AdminRepository adminRepository1;

    // ✅ Constructor-based injection
    public AdminServiceImpl(UserRepository userRepository3,
                           ServiceProviderRepository serviceProviderRepository3,
                           CountryRepository countryRepository3,
                            AdminRepository adminRepository1) {
        this.userRepository1 = userRepository3;
        this.serviceProviderRepository1 = serviceProviderRepository3;
        this.countryRepository1 = countryRepository3;
        this.adminRepository1 = adminRepository1;
    }

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
            admin.getServiceProviders().add(serviceProvider);
            return adminRepository1.save(admin);
        }
        return null;
    }

//
@Override
public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
    //add a country under the serviceProvider and return respective service provider

    // You should create a new Country object based on the given country name and add it to the country list of the service provider.
    // Note that the user attribute of the country in this case would be null.
    //In case country name is not amongst the above mentioned strings, throw "Country not found" exception
    boolean isCountryPresent = false;
    String str = countryName.toUpperCase();
    if(!str.equals("IND") && !str.equals("JPN") && !str.equals("AUS") && !str.equals("CHI") && !str.equals("USA")){
        throw  new Exception("Country not found");
    }
    ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();

    Country country = new Country(CountryName.valueOf(str), CountryName.valueOf(str).toCode());


    country.setServiceProvider(serviceProvider);
    serviceProvider.getCountryList().add(country);

    serviceProviderRepository1.save(serviceProvider);

    return serviceProvider;
    }
}
