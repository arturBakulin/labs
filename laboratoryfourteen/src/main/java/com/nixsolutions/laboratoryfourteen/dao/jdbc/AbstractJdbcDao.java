package com.nixsolutions.laboratoryfourteen.dao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJdbcDao {
	
	protected BasicDataSource ds;
	protected static final Logger log = LoggerFactory.getLogger(AbstractJdbcDao.class);
	
	public AbstractJdbcDao() {
		ds = new BasicDataSource();
		ds.setDriverClassName(setupProperties().getProperty("dbdriver"));
		ds.setUsername(setupProperties().getProperty("dbusername"));
		ds.setPassword(setupProperties().getProperty("dbpassword"));
		ds.setMinIdle(5);
		ds.setMaxIdle(10);
		ds.setUrl(setupProperties().getProperty("dburl"));
	}

	protected Properties setupProperties() {
		Properties prop = new Properties();
		InputStream input = AbstractJdbcDao.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(input);
		} catch (IOException e) {
			log.error("Can't read property file");
			throw new RuntimeException("Can't read property file");
		}
		return prop;
	}

	public abstract Connection createConnection();
}