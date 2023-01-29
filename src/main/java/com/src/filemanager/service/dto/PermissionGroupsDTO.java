package com.src.filemanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.src.filemanager.domain.PermissionGroups} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionGroupsDTO implements Serializable {

    private Long id;

    private String groupName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionGroupsDTO)) {
            return false;
        }

        PermissionGroupsDTO permissionGroupsDTO = (PermissionGroupsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionGroupsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionGroupsDTO{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
