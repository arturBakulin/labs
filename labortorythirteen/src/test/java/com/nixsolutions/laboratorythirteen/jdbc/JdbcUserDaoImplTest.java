package com.nixsolutions.laboratorythirteen.jdbc;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.*;
import com.nixsolutions.laboratorythirteen.dao.UserDao;
import com.nixsolutions.laboratorythirteen.dao.jdbc.JdbcUserDaoImpl;
import com.nixsolutions.laboratorythirteen.entity.User;

public class JdbcUserDaoImplTest {

	private static final Charset UTF8 = null;
	private IDatabaseTester databaseTester;
	private UserDao userDao = null;
	private User user = null;
	private User userExpected = null;

	@BeforeClass
	public static void createSchema() throws Exception {
		RunScript.execute(setupProperties().getProperty("dburl"), setupProperties().getProperty("dbusername"),
				setupProperties().getProperty("dbpassword"), "src/test/resources/userrole.sql", UTF8, false);
	}

	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = readDataSet();
		userDao = new JdbcUserDaoImpl();
		user = new User();
		user.setFirstName("dfdfb");
		user.setLastName("dbrdrv");
		user.setEmail("denbars@mail.com");
		user.setLogin("Doe");
		user.setPassword("maxbars");
		user.setBirhday(Date.valueOf("1970-12-01"));
		user.setId_role(1);
		userExpected = new User();
		userExpected.setId(1);
		userExpected.setFirstName("Doe");
		userExpected.setLastName("DoeSecondName");
		userExpected.setEmail("DoeUser@gmail.com");
		userExpected.setLogin("DoeUser");
		userExpected.setPassword("12345");
		userExpected.setBirhday(Date.valueOf("1992-05-02"));
		userExpected.setId_role(1);

		cleanlyInsert(dataSet);
	}

	@Test(expected = NullPointerException.class)
	public void createUserNullTest() throws Exception {
		userDao.create(null);
	}

	@Test
	public void createUserTest() throws Exception {
		InputStream input = JdbcUserDaoImplTest.class.getClassLoader().getResourceAsStream("createuserexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		userDao.create(user);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "USER", ignore);
	}

	@Test(expected = NullPointerException.class)
	public void updateUserNullTest() throws Exception {
		userDao.update(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateUserNoSuchUserTest() throws Exception {
		user.setId(5);
		userDao.update(user);
	}

	@Test
	public void updateUserTest() throws Exception {
		user.setId(2);
		InputStream input = JdbcUserDaoImplTest.class.getClassLoader().getResourceAsStream("updateuserexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		userDao.update(user);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "USER", ignore);
	}

	@Test(expected = NullPointerException.class)
	public void getUserByLoginNullTest() throws Exception {
		userDao.findByLogin(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUserByLoginNoSuchLoginTest() throws Exception {
		userDao.findByLogin(user.getLogin());
	}

	@Test
	public void getUserByLoginTest() throws Exception {
		User userFromBd = userDao.findByLogin(userExpected.getLogin());
		assertEquals(userExpected, userFromBd);
	}

	@Test(expected = NullPointerException.class)
	public void getUserByEmailNullTest() throws Exception {
		userDao.findByEmail(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getUserByEmailNoSuchEmailTest() throws Exception {
		userDao.findByEmail(user.getEmail());
	}

	@Test
	public void getUserByEmailTest() throws Exception {
		User userFromBd = userDao.findByEmail(userExpected.getEmail());
		assertEquals(userExpected, userFromBd);
	}

	@Test(expected = NullPointerException.class)
	public void deleteUserNullTest() throws Exception {
		userDao.remove(null);
	}

	@Test
	public void deleteUserTest() throws Exception {
		InputStream input = JdbcUserDaoImplTest.class.getClassLoader().getResourceAsStream("removeuserexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		userDao.remove(userExpected);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "USER", ignore);
	}

	@Test
	public void getAllUsersTest() throws Exception {
		ITable expeted = databaseTester.getConnection().createDataSet().getTable("USER");
		List<User> users = userDao.findAll();
		assertEquals(expeted.getRowCount(), users.size());
	}

	private void cleanlyInsert(IDataSet dataSet) throws Exception {
		databaseTester = new JdbcDatabaseTester(setupProperties().getProperty("dbdriver"),
				setupProperties().getProperty("dburl"), setupProperties().getProperty("dbusername"),
				setupProperties().getProperty("dbpassword"));
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}

	private IDataSet readDataSet() throws Exception {
		InputStream input = JdbcUserDaoImplTest.class.getClassLoader().getResourceAsStream("userroledataset.xml");
		return new FlatXmlDataSetBuilder().build(input);
	}

	private static Properties setupProperties() throws IOException {
		Properties prop = new Properties();
		InputStream input = JdbcUserDaoImplTest.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);
		return prop;
	}
}