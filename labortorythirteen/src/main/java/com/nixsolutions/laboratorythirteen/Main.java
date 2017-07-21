package com.nixsolutions.laboratorythirteen;

import java.sql.Date;

import com.nixsolutions.laboratorythirteen.dao.UserDao;
import com.nixsolutions.laboratorythirteen.dao.jdbc.JdbcUserDaoImpl;
import com.nixsolutions.laboratorythirteen.entity.User;

public class Main {

	public static void main(String[] args) {

		User user = new User();
		user.setFirstName("dfdfb");
		user.setLastName("dbrdrv");
		user.setEmail("denbars@mail.com");
		user.setLogin("Doe");
		user.setPassword("maxbars");
		user.setBirhday(Date.valueOf("1970-12-01"));
		user.setId_role(1);
		UserDao userDao = new JdbcUserDaoImpl();
		userDao.create(user);
	}

}
