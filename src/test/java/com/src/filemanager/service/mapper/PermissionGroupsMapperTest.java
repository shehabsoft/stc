package com.src.filemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionGroupsMapperTest {

    private PermissionGroupsMapper permissionGroupsMapper;

    @BeforeEach
    public void setUp() {
        permissionGroupsMapper = new PermissionGroupsMapperImpl();
    }
}
