package com.nixsolutions.laboratoryfourteen.dao;

import com.nixsolutions.laboratoryfourteen.entity.Role;

public interface RoleDao {

	public void create(Role role);

	public void update(Role role);

	public void remove(Role role);

	public Role findByName(String name);

}
