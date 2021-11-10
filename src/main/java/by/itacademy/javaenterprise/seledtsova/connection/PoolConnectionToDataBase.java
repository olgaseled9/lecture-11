package by.itacademy.javaenterprise.seledtsova.connection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ResourceBundle;

public class PoolConnectionToDataBase {

    private static final Logger logger = LoggerFactory.getLogger(PoolConnectionToDataBase.class);

    private static BasicDataSource basicDataSource;

    static {
        try {
            basicDataSource = new BasicDataSource();
            ResourceBundle resource = ResourceBundle.getBundle("database");
            String driver = resource.getString("database.driver");
            String url = resource.getString("database.url");
            String user = resource.getString("database.user");
            String password = resource.getString("database.password");
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(user);
            basicDataSource.setPassword(password);
            basicDataSource.setDriverClassName(driver);
            basicDataSource.setMinIdle(15);
            basicDataSource.setMaxIdle(25);
            basicDataSource.setMaxOpenPreparedStatements(250);
        } catch (Exception e) {
            logger.error("Connection is not available", e);
        }
    }

    public static DataSource getDataSource() {
        return basicDataSource;
    }
}
