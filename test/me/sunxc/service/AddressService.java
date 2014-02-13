package me.sunxc.service;

import me.sunxc.dao.AddressDao;
import me.sunxc.model.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;
    
    public void save(Address address) {
        addressDao.save(address);
    }
    
    public Address findById(int id) {
        return addressDao.findById(id);
    }
}
