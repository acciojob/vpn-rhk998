package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository3;
    private final ServiceProviderRepository serviceProviderRepository3;
    private final CountryRepository countryRepository3;

    // ✅ Constructor-based injection
    public UserServiceImpl(UserRepository userRepository3,
                           ServiceProviderRepository serviceProviderRepository3,
                           CountryRepository countryRepository3) {
        this.userRepository3 = userRepository3;
        this.serviceProviderRepository3 = serviceProviderRepository3;
        this.countryRepository3 = countryRepository3;
    }

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        CountryName countryEnum;
        try {
            countryEnum = CountryName.valueOf(countryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Country not found");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false);

        Country country = new Country();
        country.setCountryName(countryEnum);
        country.setCode(countryEnum.toCode());
        country.setUser(user);

        user.setOriginalCountry(country);

        User savedUser = userRepository3.save(user);
        savedUser.setOriginalIp(country.getCode() + "." + savedUser.getId());

        return userRepository3.save(savedUser);
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
         User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

        List<User> users = serviceProvider.getUsers();
        users.add(user);
        serviceProvider.setUsers(users);
        serviceProviderRepository3.save(serviceProvider);

        List<ServiceProvider> providers = user.getServiceProviderList();
        providers.add(serviceProvider);
        user.setServiceProviderList(providers);

        return userRepository3.save(user);
    }
}
