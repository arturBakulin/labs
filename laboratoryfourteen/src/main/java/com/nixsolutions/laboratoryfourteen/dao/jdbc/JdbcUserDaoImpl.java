package com.nixsolutions.laboratoryfourteen.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.nixsolutions.laboratoryfourteen.dao.UserDao;
import com.nixsolutions.laboratoryfourteen.entity.User;

public class JdbcUserDaoImpl extends AbstractJdbcDao implements UserDao {

	private List<User> userList;
	private User user;

	@Override
	public void create(User user) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = createConnection();
			prepStatement = insertUser(user, connection);
			connection.commit();
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.error("SQLException while rolling back", e1);
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("SQLException while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
	}

	@Override
	public void update(User user) {
		if (!isUserById(user)) {
			log.error("IllegalArgumentException.There is no such role to update!");
			throw new IllegalArgumentException();
		}
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = createConnection();
			prepStatement = updateUserFields(user, connection);
			connection.commit();
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.error("SQLException while rolling back", e1);
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("SQLException while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
	}

	@Override
	public void remove(User user) {
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = createConnection();
			prepStatement = removeUserById(user, connection);
			connection.commit();
		} catch (SQLException e) {
			log.error("SQLException while getting connection");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.error("SQLException while rolling back", e1);
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("Exception while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
	}

	@Override
	public List<User> findAll() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = createConnection();
			userList = new ArrayList<User>();
			ResultSet result = connection.createStatement()
					.executeQuery("SELECT ID, LOGIN, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, BIRTHDAY,ID_ROLE FROM USER");
			while (result.next()) {
				user = new User(result.getLong("ID"), result.getString("LOGIN"), result.getString("PASSWORD"),
						result.getString("EMAIL"), result.getString("FIRSTNAME"), result.getString("LASTNAME"),
						result.getDate("BIRTHDAY"), result.getLong("ID_ROLE"));
				userList.add(user);
			}
		} catch (SQLException e) {
			log.error("SQLException while finding all users", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null && !statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing statement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("SQLException while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return userList;
	}

	@Override
	public User findByLogin(String login) {
		if (login == null) {
			log.error("NullPointerException. There is no such role name!");
			throw new NullPointerException();
		}
		if (!isUserByLogin(login)) {
			throw new IllegalArgumentException();
		}
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = createConnection();
			prepStatement = selectUserByLogin(login, connection);
			ResultSet result = prepStatement.executeQuery();
			result.next();
			user = new User(result.getLong("ID"), result.getString("LOGIN"), result.getString("PASSWORD"),
					result.getString("EMAIL"), result.getString("FIRSTNAME"), result.getString("LASTNAME"),
					result.getDate("BIRTHDAY"), result.getLong("ID_ROLE"));
		} catch (SQLException e) {
			log.error("SQLException while finding by login", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("Exception while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return user;
	}

	@Override
	public User findByEmail(String email) {
		if (email == null) {
			log.error("NullPointerException. There is no such role name!");
			throw new NullPointerException();
		}
		if (!isUserByEmail(email)) {
			throw new IllegalArgumentException();
		}
		Connection connection = null;
		PreparedStatement prepStatement = null;
		try {
			connection = createConnection();
			prepStatement = selectUserByEmail(email, connection);
			ResultSet result = prepStatement.executeQuery();
			result.next();
			user = new User(result.getLong("ID"), result.getString("LOGIN"), result.getString("PASSWORD"),
					result.getString("EMAIL"), result.getString("FIRSTNAME"), result.getString("LASTNAME"),
					result.getDate("BIRTHDAY"), result.getLong("ID_ROLE"));
		} catch (SQLException e) {
			log.error("SQLException while finding by email", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("SQLException while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return user;
	}

	@Override
	public Connection createConnection() {
		Connection connection = null;
		try {
			connection = this.ds.getConnection();
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			throw new RuntimeException(e);
		}
		return connection;
	}

	private boolean isUserById(User user) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = selectUserById(user, connection);
			ResultSet res = prepStatement.executeQuery();
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("Exception while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return false;
	}

	private boolean isUserByLogin(String login) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = selectUserByLogin(login, connection);
			ResultSet res = prepStatement.executeQuery();
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("Exception while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return false;
	}

	private boolean isUserByEmail(String email) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = selectUserByEmail(email, connection);
			ResultSet res = prepStatement.executeQuery();
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			log.error("SQLException while getting connection", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (SQLException e) {
				log.error("SQLException while closing prepStatement", e);
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e1) {
					log.error("SQLException while closing connection", e1);
					throw new RuntimeException("SQLException while closing connection", e1);
				}
				throw new RuntimeException("SQLException while closing prepStatement", e);
			}
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e1) {
				log.error("SQLException while closing connection", e1);
				throw new RuntimeException("SQLException while closing connection", e1);
			}
		}
		return false;
	}

	private PreparedStatement insertUser(User user, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement(
				"INSERT INTO USER (login, password, email, firstName, lastName, birthday, id_role) VALUES (?,?,?,?,?,?,?)");
		prepStatement.setString(1, user.getLogin());
		prepStatement.setString(2, user.getPassword());
		prepStatement.setString(3, user.getEmail());
		prepStatement.setString(4, user.getFirstName());
		prepStatement.setString(5, user.getLastName());
		prepStatement.setDate(6, user.getBirhday());
		prepStatement.setLong(7, user.getId_role());
		prepStatement.executeUpdate();
		return prepStatement;
	}

	private PreparedStatement updateUserFields(User user, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement(
				"UPDATE USER SET login = ?, password = ?, email = ?, firstName = ?, lastName = ?, birthday = ?, id_role = ? WHERE ID = ?;");
		prepStatement.setString(1, user.getLogin());
		prepStatement.setString(2, user.getPassword());
		prepStatement.setString(3, user.getEmail());
		prepStatement.setString(4, user.getFirstName());
		prepStatement.setString(5, user.getLastName());
		prepStatement.setDate(6, user.getBirhday());
		prepStatement.setLong(7, user.getId_role());
		prepStatement.setLong(8, user.getId());
		prepStatement.executeUpdate();
		return prepStatement;
	}

	private PreparedStatement removeUserById(User user, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("DELETE FROM USER WHERE ID = ?;");
		prepStatement.setLong(1, user.getId());
		prepStatement.executeUpdate();
		return prepStatement;
	}

	private PreparedStatement selectUserByLogin(String login, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("SELECT * FROM USER WHERE LOGIN = ? ;");
		prepStatement.setString(1, login);
		return prepStatement;
	}

	private PreparedStatement selectUserByEmail(String email, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("SELECT * FROM USER WHERE EMAIL = ? ;");
		prepStatement.setString(1, email);
		return prepStatement;
	}

	private PreparedStatement selectUserById(User user, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("SELECT * FROM USER WHERE id = ?");
		prepStatement.setLong(1, user.getId());
		return prepStatement;
	}
}
