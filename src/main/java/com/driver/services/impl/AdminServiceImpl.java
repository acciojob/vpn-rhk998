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

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        Admin admin = serviceProvider.getAdmin();
        String cname = countryName.toUpperCase();
        CountryName countryEnum ;
        try{
            countryEnum = CountryName.valueOf(countryName);
        }catch (IllegalArgumentException e){
            throw new Exception("Country not found");
        }
        Country country = new Country();
        country.setCountryName(countryEnum);
        country.setCode(countryEnum.toCode());
        country.setUser(null);
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);

        return serviceProviderRepository1.save(serviceProvider);
    }
}
