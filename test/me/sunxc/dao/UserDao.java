package me.sunxc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import me.sunxc.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends JdbcDaoSupport {
    @Autowired
    @Qualifier("readWriteDataSource")
    public void setDS(DataSource ds) {
        setDataSource(ds);
    }
    
    
    private RowMapper<User> rowMapper = new RowMapper<User>() {
        
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            return user;
        }
    };
    
   
    
    
    public void save(final User user) {
        final String sql = "insert into user(name) values(?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder(); 
        getJdbcTemplate().update(new PreparedStatementCreator() {
            
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement psst = con.prepareStatement(sql, new String[]{"id"});
                psst.setString(1, user.getName());
                return psst;
            }
        }, generatedKeyHolder);
        
        user.setId(generatedKeyHolder.getKey().intValue());
    }
    
    public void update(User user) {
        String sql = "update user set name=? where id=?";
        getJdbcTemplate().update(sql, user.getName(), user.getId());
    }
    
    public void delete(int id) {
        String sql = "delete from user where id=?";
        getJdbcTemplate().update(sql, id);
    }
    
    public User findById(int id) {
        String sql = "select id, name from user where id=?";
        List<User> userList = getJdbcTemplate().query(sql, rowMapper, id);
        
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }
    
}
