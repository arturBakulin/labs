package com.nixsolutions.laboratorythirteen;

import java.sql.Date;

import com.nixsolutions.laboratorythirteen.entity.User;
import com.nixsolutions.laboratorythirteen.interfaces.UserDao;
import com.nixsolutions.laboratorythirteen.jdbc.JdbcUserDaoImpl;

public class Main {

	public static void main(String[] args) {
	
	User userFirst = new User("Greg", "Greg", "denbars@mail.comjnj", "dfdfb", "dbrdrv", Date.valueOf("1990-01-01"), 2);
//	ServiceManager serviceManager = ServiceManager.getInstance();
//	serviceManager.setRepository(H2ServerRepository.getInstance());
	UserDao userDao = new JdbcUserDaoImpl();
	userDao.create(userFirst);
//	roleDao.update(roleFirst);
//	roleDao.create(roleFirst);
//	System.out.println(roleDao.findByName("Admin").getId());
//	userDao.update(userFirst);
//	System.out.println(userDao.findAll().get(3).getRole().getName());
	}

}
