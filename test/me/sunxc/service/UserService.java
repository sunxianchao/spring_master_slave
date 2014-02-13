package me.sunxc.service;

import me.sunxc.dao.UserDao;
import me.sunxc.model.Address;
import me.sunxc.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AddressService addressService;
    
    public void save(User user) {
        userDao.save(user);
    }
    
    public void save(User user, Address address) {
        userDao.save(user);
        address.setUserId(user.getId());
        addressService.save(address);
    }
    
    public void update(User user) {
        userDao.update(user);
    }
    
    
    public void delete(int id) {
        userDao.delete(id);
    }
    
    public User findById(int id) {
        return userDao.findById(id);
    }

    public void testFirstWriteNextRead(User user) { //1 current determine write datasource  进入该方法开启事务
    		System.out.println("testfirst....");
        save(user); //2  same 1
        //必须获取代理本身 否则目标对象内的自我调用会有问题
//        UserService userService = (UserService)AopContext.currentProxy();
        
        //3  when forceChoiceReadOnWrite=false    same 1  choice write datasource (not read datasource)
        //3  when forceChoiceReadOnWrite=true     current determine read datasource : readDataSource×××
        user = findById(user.getId()); 
        
        delete(user.getId()); // same 1
        
    }
}
