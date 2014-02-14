package me.sunxc.readwrite;

import me.sunxc.model.Address;
import me.sunxc.model.User;
import me.sunxc.service.UserService;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 测试forceChoiceReadOnWrite=false 场景
 * @author xianchao.sun@yunyoyo.cn
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-config.xml"})
@TransactionConfiguration(transactionManager="txManager", defaultRollback=false)
public class DynamicDatasourceChoiceTest {
    
    private static final Logger log = Logger.getLogger("cn.javass.common.datasource.test");
    
    @Autowired
    private UserService userService;
    
    @Test
    public void testOnlyRead() {
        log.debug("test only read============begin");
        userService.findById(1);
        log.debug("test only read============end");
    }
    
    @Test
    public void testOnlyWrite() {
        log.debug("test only write============begin");
        User user = genUser();
        userService.save(user); //1  choice write datasource
//        userService.delete(user.getId()); //2  choice write datasource
        
        User user2 = genUser();
        Address address2 = genAddress();
        userService.save(user2, address2);//3  choice write datasource  此处内部会传播事务
     
//        userService.delete(user2.getId());//4  choice write datasource
        
        log.debug("test only write============end");
    }
    

    @Test
    public void testFirstReadNextWrite() {
        log.debug("test first read next write============begin");
        User user = genUser();
        userService.save(user); //1  choice write datasource
        
        user = userService.findById(user.getId()); //2  choice read datasource
        
        userService.delete(user.getId());//3  choice write datasource
        
        log.debug("test first read next write============end");
    }
    
    @Test
    public void testFirstWriteNextRead() {
        log.debug("test first write next read============begin");
        
        userService.testFirstWriteNextRead(genUser());
        
        log.debug("test first write next read============end");
    }
    
    
    
    private User genUser() {
        return new User("zhang" + System.currentTimeMillis());
    }
    

    private Address genAddress() {
        return new Address("city"+System.currentTimeMillis());
    }

    

}
