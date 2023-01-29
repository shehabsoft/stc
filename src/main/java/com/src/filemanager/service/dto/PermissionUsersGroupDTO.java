package com.src.filemanager.service.dto;

import com.src.filemanager.domain.enumeration.PermissionLevel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.src.filemanager.domain.PermissionUsersGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionUsersGroupDTO implements Serializable {

    private Long id;

    private String userEmail;

    private PermissionLevel permissionLevel;

    private PermissionGroupsDTO permissionGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public PermissionGroupsDTO getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroupsDTO permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionUsersGroupDTO)) {
            return false;
        }

        PermissionUsersGroupDTO permissionUsersGroupDTO = (PermissionUsersGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionUsersGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionUsersGroupDTO{" +
            "id=" + getId() +
            ", userEmail='" + getUserEmail() + "'" +
            ", permissionLevel='" + getPermissionLevel() + "'" +
            ", permissionGroup=" + getPermissionGroup() +
            "}";
    }
}
