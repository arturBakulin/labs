package com.nixsolutions.laboratorythirteen.dao;

import com.nixsolutions.laboratorythirteen.entity.Role;

public interface RoleDao {

	public void create(Role role);

	public void update(Role role);

	public void remove(Role role);

	public Role findByName(String name);

}
