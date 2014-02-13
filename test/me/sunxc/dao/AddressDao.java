package me.sunxc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import me.sunxc.model.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao extends JdbcDaoSupport {
    @Autowired
    @Qualifier("readWriteDataSource")
    public void setDS(DataSource ds) {
        setDataSource(ds);
    }
    
    
    private RowMapper<Address> rowMapper = new RowMapper<Address>() {
        
        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
            Address address = new Address();
            address.setId(rs.getInt("id"));
            address.setUserId(rs.getInt("addressId"));
            address.setCity(rs.getString("city"));
            return address;
        }
    };
    
   
    
    
    public void save(final Address address) {
        final String sql = "insert into address(userId, city) values(?,?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder(); 
        getJdbcTemplate().update(new PreparedStatementCreator() {
            
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement psst = con.prepareStatement(sql, new String[]{"id"});
                psst.setInt(1, address.getUserId());
                psst.setString(2, address.getCity());
                return psst;
            }
        }, generatedKeyHolder);
        
        address.setId(generatedKeyHolder.getKey().intValue());
    }
    
    public Address findById(int id) {
        String sql = "select id, userId, city from address where id=?";
        List<Address> addressList = getJdbcTemplate().query(sql, rowMapper, id);
        
        if(addressList.size() == 0) {
            return null;
        }
        return addressList.get(0);
    }
    
}
