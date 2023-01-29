package com.src.filemanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionUsersGroupMapperTest {

    private PermissionUsersGroupMapper permissionUsersGroupMapper;

    @BeforeEach
    public void setUp() {
        permissionUsersGroupMapper = new PermissionUsersGroupMapperImpl();
    }
}
