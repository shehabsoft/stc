package com.src.filemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PermissionGroups.
 */
@Entity
@Table(name = "permission_groups")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @OneToMany(mappedBy = "permissionGroup")
    @JsonIgnoreProperties(value = { "items", "permissionGroup" }, allowSetters = true)
    private Set<PermissionUsersGroup> permissionUsersGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PermissionGroups id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public PermissionGroups groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<PermissionUsersGroup> getPermissionUsersGroups() {
        return this.permissionUsersGroups;
    }

    public void setPermissionUsersGroups(Set<PermissionUsersGroup> permissionUsersGroups) {
        if (this.permissionUsersGroups != null) {
            this.permissionUsersGroups.forEach(i -> i.setPermissionGroup(null));
        }
        if (permissionUsersGroups != null) {
            permissionUsersGroups.forEach(i -> i.setPermissionGroup(this));
        }
        this.permissionUsersGroups = permissionUsersGroups;
    }

    public PermissionGroups permissionUsersGroups(Set<PermissionUsersGroup> permissionUsersGroups) {
        this.setPermissionUsersGroups(permissionUsersGroups);
        return this;
    }

    public PermissionGroups addPermissionUsersGroup(PermissionUsersGroup permissionUsersGroup) {
        this.permissionUsersGroups.add(permissionUsersGroup);
        permissionUsersGroup.setPermissionGroup(this);
        return this;
    }

    public PermissionGroups removePermissionUsersGroup(PermissionUsersGroup permissionUsersGroup) {
        this.permissionUsersGroups.remove(permissionUsersGroup);
        permissionUsersGroup.setPermissionGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionGroups)) {
            return false;
        }
        return id != null && id.equals(((PermissionGroups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionGroups{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            "}";
    }
}
