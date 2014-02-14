package me.sunxc.common.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.CollectionUtils;

/**
 * 
 * <pre>
 * 读/写动态选择数据库实现
 * 目前实现功能
 *   一写库多读库选择功能，请参考
 *      @see me.sunxc.common.datasource.ReadWriteDataSourceChoice
        @see me.sunxc.common.datasource.ReadWriteDataSourceChoice.DataSourceType
 *   
 *   默认按顺序轮询使用读库
 *   默认选择写库
 *   
 *   已实现：一写多读、当写时默认读操作到写库、当写时强制读操作到读库
 *   TODO 读库负载均衡、读库故障转移
 * </pre>  
 * @author xianchao.sun@yunyoyo.cn
 *
 */
public class DynamicDataSource extends AbstractDataSource implements InitializingBean {
    private static final Logger log = Logger.getLogger(DynamicDataSource.class);
    
    private DataSource writeDataSource;
    private Map<String, DataSource> readDataSourceMap;
    
    
    private String[] readDataSourceNames;
    private DataSource[] readDataSources;
    private int readDataSourceCount;

    private AtomicInteger counter = new AtomicInteger(1);

    
    /**
     * 设置读库
     * @param readDataSourceMap
     */
    public void setReadDataSourceMap(Map<String, DataSource> readDataSourceMap) {
        this.readDataSourceMap = readDataSourceMap;
    }
    
    /**
     * 设置写库
     * @param writeDataSource
     */
    public void setWriteDataSource(DataSource writeDataSource) {
        this.writeDataSource = writeDataSource;
    }
    
    /**
     * 是InitializingBean的方法，如果属性类的属性都被设置后则调用
     * 可以在这个方法中检查属性设置
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(writeDataSource == null) {
            throw new IllegalArgumentException("property 'writeDataSource' is required");
        }
        if(CollectionUtils.isEmpty(readDataSourceMap)) {
            throw new IllegalArgumentException("property 'readDataSourceMap' is required");
        }
        readDataSourceCount = readDataSourceMap.size();
        
        readDataSources = new DataSource[readDataSourceCount];
        readDataSourceNames = new String[readDataSourceCount];
        
        int i = 0;
        for(Entry<String, DataSource> e : readDataSourceMap.entrySet()) {
            readDataSources[i] = e.getValue();
            readDataSourceNames[i] = e.getKey();
            i++;
        }
        
        
    }
    
    /**
     * 这里进行判断是读还是写库，默认是写库
     * @return DataSource
     */
    private DataSource choiceDataSource() {
        if(ReadWriteDataSourceChoice.isChoiceWrite()) {
            log.debug("current determine write datasource");
            return writeDataSource;
        }
        
        if(ReadWriteDataSourceChoice.isChoiceNone()) {
            log.debug("no choice read/write, default determine write datasource");
            return writeDataSource;
        } 
        return choiceReadDataSource();
    }
    
    /**
     * 对读库进行负载均衡，算法就是轮询,可以在这里扩展其他的算法
     * @return DataSource
     */
    private DataSource choiceReadDataSource() {
        //按照顺序选择读库 
        //TODO 算法改进 
        int index = counter.incrementAndGet() % readDataSourceCount;
        index = Math.abs(index);
            
        String dataSourceName = readDataSourceNames[index];
        
        log.debug(String.format("current choice read datasource : %s", dataSourceName));
        return readDataSources[index];
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return choiceDataSource().getConnection();
    }
    
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return choiceDataSource().getConnection(username, password);
    }

}
