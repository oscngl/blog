package com.osc.blog.api.controllers;

import com.osc.blog.business.abstracts.RoleService;
import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.Role;
import com.osc.blog.entities.dtos.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RolesController {

    private final RoleService roleService;

    @PostMapping("/save")
    public Result save(@RequestBody @Valid RoleDto roleDto) {
        return roleService.save(roleDto);
    }

    @GetMapping("/getById")
    public DataResult<Role> getById(int id) {
        return roleService.getById(id);
    }

    @GetMapping("/getAll")
    public DataResult<List<Role>> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/getByName")
    public DataResult<Role> getByName(String name) {
        return roleService.getByName(name);
    }

}
