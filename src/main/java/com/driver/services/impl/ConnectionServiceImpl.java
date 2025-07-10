package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final UserRepository userRepository2;
    private final ServiceProviderRepository serviceProviderRepository2;
    private final ConnectionRepository connectionRepository2;

    // ✅ Constructor-based injection
    public ConnectionServiceImpl(UserRepository userRepository3,
                           ServiceProviderRepository serviceProviderRepository2,
                           ConnectionRepository connectionRepository2) {
        this.userRepository2 = userRepository3;
        this.serviceProviderRepository2 = serviceProviderRepository2;
        this.connectionRepository2 = connectionRepository2;
    }

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user  = userRepository2.findById(userId).get();
        if(user.getConnected()) throw new Exception("already connected");
        String country = user.getOriginalCountry().getCountryName().toString();
        if(countryName.equals(country)) return user;

        List<ServiceProvider> providers = user.getServiceProviderList();
        ServiceProvider selectedProvider = null;
        Country targetCountry = null;

        for(ServiceProvider provider : providers){
            for(Country c : provider.getCountryList()){
                if(c.getCountryName().toString().equalsIgnoreCase(countryName)){
                    if (selectedProvider == null || provider.getId() < selectedProvider.getId()) {
                        selectedProvider = provider;
                        targetCountry = c;
                    }
                }
            }
        }
        if(selectedProvider == null) throw new Exception("Unable to connect");

        Connection connection = new Connection();
        connection.setUser(user);
        connection.setServiceProvider(selectedProvider);
        connectionRepository2.save(connection);

        String maskedId = targetCountry.getCode() + "." + selectedProvider.getId() + "." + user.getId();
        user.setMaskedIp(maskedId);
        user.setConnected(true);
        user.getConnectionList().add(connection);
        return userRepository2.save(user);
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).get();
        if(!user.getConnected()) throw new Exception("already disconnected");
        user.setConnected(false);
        user.setMaskedIp(null);
        return userRepository2.save(user);
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User receiver = userRepository2.findById(receiverId).get();
        User sender = userRepository2.findById(senderId).get();

        String receiverCounrty = receiver.getConnected() ?
                receiver.getMaskedIp().split("\\.")[0] : receiver.getOriginalCountry().getCode();

        String senderCountry = sender.getOriginalCountry().getCode();

        if(senderCountry.equals(receiverCounrty)) return sender;
        ServiceProvider selectedProvider = null;
        for(ServiceProvider p : sender.getServiceProviderList()){
            for(Country c : p.getCountryList()){
                if (c.getCode().equals(receiverCounrty)){
                    if( selectedProvider == null || p.getId() < selectedProvider.getId()){
                        selectedProvider = p;
                        sender.setConnected(true);
                        sender.setMaskedIp(c.getCode() + "." + p.getId() + "." + sender.getId());
                        Connection connection = new Connection();
                        connection.setUser(sender);
                        connection.setServiceProvider(p);
                        connectionRepository2.save(connection);
                        return userRepository2.save(sender);
                    }
                }
            }
        }
        throw new Exception("Cannot establish communication");
    }
}
