package cn.cnyaoshun.form.common.util;


import cn.cnyaoshun.form.common.DatabaseDriverType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Component
public class DynamicDataSourceUtil {

    /**
     *  获取数据源
     * @param databaseType mysql/oracle
     * @param url jdbc:oracle:thin:@192.168.1.9:1521:xe
     * @param username root
     * @param password root
     * @return
     */
    private DriverManagerDataSource getDataSource(String databaseType,String url,String username,String password){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url,username,password);
        dataSource.setDriverClassName(DatabaseDriverType.getDatabaseDriver(databaseType));
        return dataSource;
    }

    /**
     * 获取事务模版
     * @param dataSource
     * @return
     */
    private TransactionTemplate getTransactionTemplate(String databaseType,String url,String username,String password){
        DriverManagerDataSource dataSource = getDataSource(databaseType, url, username, password);
        return getTransactionTemplate(dataSource);
    }

    /**
     * 获取事务模版
     * @param dataSource
     * @return
     */
    private TransactionTemplate getTransactionTemplate(DataSource dataSource){
       PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
       TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
       return transactionTemplate;
    }

    /**
     * 获取jdbc操作模版
     * @param dataSource
     * @return
     */
    private JdbcTemplate getJdbcTemplate(DataSource dataSource){
       return new JdbcTemplate(dataSource);
    }

    /**
     * 获取jdbc操作模版
     * @param dataSource
     * @return
     */
    public JdbcTemplate getJdbcTemplate(String databaseType,String url,String username,String password){
        DriverManagerDataSource dataSource = getDataSource(databaseType, url, username, password);
        return new JdbcTemplate(dataSource);
    }


    /**
     *
     *  获取数据源
     * @param databaseType mysql/oracle
     * @param url jdbc:oracle:thin:@192.168.1.9:1521:xe
     * @param username root
     * @param password root
     * @return
     */
    public DynamicTemplate getDynamicTemplate(String databaseType,String url,String username,String password){
        DriverManagerDataSource dataSource = getDataSource(databaseType, url, username, password);
        TransactionTemplate transactionTemplate = getTransactionTemplate(dataSource);
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dataSource);
        return new DynamicTemplate(jdbcTemplate,transactionTemplate);
    }
}
