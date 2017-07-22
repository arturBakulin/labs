package com.nixsolutions.laboratoryfourteen.jdbc;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import org.dbunit.*;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.*;

import com.nixsolutions.laboratoryfourteen.dao.RoleDao;
import com.nixsolutions.laboratoryfourteen.dao.jdbc.JdbcRoleDaoImpl;
import com.nixsolutions.laboratoryfourteen.entity.Role;

public class JdbcRoleDaoImplTest {

	private static final Charset UTF8 = null;
	private IDatabaseTester databaseTester;
	private RoleDao roleDao = null;
	private Role role = null;

	@BeforeClass
	public static void createSchema() throws Exception {
		RunScript.execute(setupProperties().getProperty("dburl"), setupProperties().getProperty("dbusername"),
				setupProperties().getProperty("dbpassword"), "src/test/resources/role.sql", UTF8, false);
	}

	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = readDataSet();
		roleDao = new JdbcRoleDaoImpl();
		role = new Role("Registred");
		cleanlyInsert(dataSet);
	}

	private IDataSet readDataSet() throws Exception {
		InputStream input = JdbcRoleDaoImplTest.class.getClassLoader().getResourceAsStream("roledataset.xml");
		return new FlatXmlDataSetBuilder().build(input);
	}

	@Test(expected = NullPointerException.class)
	public void findRoleByNameNullTest() throws Exception {
		roleDao.findByName(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void findRoleByNameNoSuchRoleTest() throws Exception {
		roleDao.findByName(role.getName());
	}

	@Test
	public void findRoleByNameTest() throws Exception {
		Role role = roleDao.findByName("Doe");
		assertEquals(role.getName(), "Doe");
	}

	@Test(expected = NullPointerException.class)
	public void createRoleNullTest() throws Exception {
		roleDao.create(null);
	}

	@Test
	public void createRoleTest() throws Exception {
		InputStream input = JdbcRoleDaoImplTest.class.getClassLoader().getResourceAsStream("createroleexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		roleDao.create(role);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "ROLE", ignore);
	}

	@Test(expected = NullPointerException.class)
	public void updateRoleNullTest() throws Exception {
		roleDao.update(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateRoleNoSuchRoleTest() throws Exception {
		roleDao.update(role);
	}

	@Test
	public void updateRoleTest() throws Exception {
		Role roleForUpdate = new Role(2, "Anonimus");
		InputStream input = JdbcRoleDaoImplTest.class.getClassLoader().getResourceAsStream("updateroleexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		roleDao.update(roleForUpdate);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "ROLE", ignore);
	}

	@Test(expected = NullPointerException.class)
	public void deleteNullTest() throws Exception {
		roleDao.remove(null);
	}

	@Test
	public void deleteRoleTest() throws Exception {
		Role roleFromDB = roleDao.findByName("Doe");
		InputStream input = JdbcRoleDaoImplTest.class.getClassLoader().getResourceAsStream("removeroleexpected.xml");
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(input);
		roleDao.remove(roleFromDB);
		IDataSet actualData = databaseTester.getConnection().createDataSet();
		String[] ignore = { "id" };
		Assertion.assertEqualsIgnoreCols(expectedData, actualData, "ROLE", ignore);
	}

	private void cleanlyInsert(IDataSet dataSet) throws Exception {
		databaseTester = new JdbcDatabaseTester(setupProperties().getProperty("dbdriver"),
				setupProperties().getProperty("dburl"), setupProperties().getProperty("dbusername"),
				setupProperties().getProperty("dbpassword"));
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}

	private static Properties setupProperties() throws IOException {
		Properties prop = new Properties();
		InputStream input = JdbcRoleDaoImplTest.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);
		return prop;
	}
}
