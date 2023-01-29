package com.src.filemanager.service.dto;

import com.src.filemanager.domain.enumeration.ItemType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.src.filemanager.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private Long id;

    private ItemType type;

    private String name;

    private PermissionUsersGroupDTO permissionUsersGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionUsersGroupDTO getPermissionUsersGroup() {
        return permissionUsersGroup;
    }

    public void setPermissionUsersGroup(PermissionUsersGroupDTO permissionUsersGroup) {
        this.permissionUsersGroup = permissionUsersGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", permissionUsersGroup=" + getPermissionUsersGroup() +
            "}";
    }
}
