package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.RoleService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.RoleDao;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.dtos.RoleDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleManager implements RoleService {

    private final RoleDao roleDao;
    private final ModelMapper modelMapper;

    @Override
    public Result save(RoleDto roleDto) {
        Role exists = roleDao.findByName(roleDto.getName());
        if(exists != null) {
            return new ErrorResult("Role already exists!");
        }
        Role role = modelMapper.map(roleDto, Role.class);
        roleDao.save(role);
        return new SuccessResult("Role saved.");
    }

    @Override
    public DataResult<Role> getById(int id) {
        Role role = roleDao.findById(id).orElse(null);
        if(role == null) {
            return new ErrorDataResult<>(null, "Role not found!");
        }
        return new SuccessDataResult<>(role);
    }

    @Override
    public DataResult<List<Role>> getAll() {
        return new SuccessDataResult<>(roleDao.findAll());
    }

    @Override
    public DataResult<Role> getByName(String name) {
        Role role = roleDao.findByName(name);
        if(role == null) {
            return new ErrorDataResult<>(null, "Role not found!");
        }
        return new SuccessDataResult<>(role);
    }

}
