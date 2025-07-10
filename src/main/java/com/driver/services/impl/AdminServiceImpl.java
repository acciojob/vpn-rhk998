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
        return  adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository1.findById(adminId).get();
        ServiceProvider serviceProvider = serviceProviderRepository1.findByName(providerName);
        List<ServiceProvider> providers = admin.getServiceProviders();
        if(serviceProvider == null){
            serviceProvider = new ServiceProvider();
            serviceProvider.setName(providerName);
        }
        serviceProvider.setAdmin(admin);
        serviceProviderRepository1.save(serviceProvider);

        providers.add(serviceProvider);
        admin.setServiceProviders(providers);
        return adminRepository1.save(admin);
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

        List<Country> countryList = serviceProvider.getCountryList();
        if (countryList == null) countryList = new ArrayList<>();
        countryList.add(country);
        serviceProvider.setCountryList(countryList);

        return serviceProviderRepository1.save(serviceProvider);
    }
}
