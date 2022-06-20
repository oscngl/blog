package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.dtos.RoleDto;

import java.util.List;

public interface RoleService {

    Result save(RoleDto roleDto);
    DataResult<Role> getById(int id);
    DataResult<List<Role>> getAll();
    DataResult<Role> getByName(String name);

}
