package com.nixsolutions.laboratorythirteen.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nixsolutions.laboratorythirteen.abstractclass.AbstractJdbcDao;
import com.nixsolutions.laboratorythirteen.entity.Role;
import com.nixsolutions.laboratorythirteen.interfaces.RoleDao;

public class JdbcRoleDaoImpl extends AbstractJdbcDao implements RoleDao {

	private Role role;

	@Override
	public void create(Role role) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = insertRole(role, connection);
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
	public void update(Role role) {
		if (!isRoleExist(role)) {
			log.error("IllegalArgumentException.There is no such role to update!");
			throw new IllegalArgumentException();
		}
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = updateRoleName(role, connection);
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
	public void remove(Role role) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = deleteRoleById(role, connection);
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
	public Role findByName(String name) {
		if (name == null) {
			log.error("NullPointerException. There is no such role name!");
			throw new NullPointerException();
		}
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = selectRoleByName(name, connection);
			ResultSet result = prepStatement.executeQuery();
			if (!result.next()) {
				throw new IllegalArgumentException();
			}
			role = roleResultToObject(result);
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
				throw new RuntimeException("SQLException while closing connection");
			}
		}
		return role;
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

	private Role roleResultToObject(final ResultSet currentPosition) {
		Role role = new Role();
		try {
			role.setId(currentPosition.getLong("id_role"));
			role.setName(currentPosition.getString("name"));
		} catch (SQLException e) {
			log.error("getting SQLException", e);
			throw new RuntimeException(e);
		}
		return role;
	}

	private boolean isRoleExist(Role role) {
		PreparedStatement prepStatement = null;
		Connection connection = null;
		try {
			connection = createConnection();
			prepStatement = selectRoleById(role, connection);
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

	private PreparedStatement selectRoleById(Role role, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("SELECT * FROM role WHERE id_role = ?");
		prepStatement.setLong(1, role.getId());
		return prepStatement;
	}
	
	private PreparedStatement updateRoleName(Role role, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("UPDATE ROLE SET NAME = ? WHERE ID_ROLE = ?;");
		prepStatement.setString(1, role.getName());
		prepStatement.setLong(2, role.getId());
		prepStatement.executeUpdate();
		return prepStatement;
	}
	
	private PreparedStatement insertRole(Role role, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("INSERT INTO ROLE (NAME) VALUES (?)");
		prepStatement.setString(1, role.getName());
		prepStatement.executeUpdate();
		return prepStatement;
	}
	
	private PreparedStatement deleteRoleById(Role role, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("DELETE FROM ROLE WHERE ID_ROLE = ?;");
		prepStatement.setLong(1, role.getId());
		prepStatement.executeUpdate();
		return prepStatement;
	}
	
	private PreparedStatement selectRoleByName(String name, Connection connection) throws SQLException {
		PreparedStatement prepStatement;
		prepStatement = connection.prepareStatement("SELECT * FROM ROLE WHERE NAME = ?;");
		prepStatement.setString(1, name);
		return prepStatement;
	}
}
