package com.src.filemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.src.filemanager.domain.enumeration.PermissionLevel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PermissionUsersGroup.
 */
@Entity
@Table(name = "permission_users_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionUsersGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level")
    private PermissionLevel permissionLevel;

    @OneToMany(mappedBy = "permissionUsersGroup")
    @JsonIgnoreProperties(value = { "permissionUsersGroup" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "permissionUsersGroups" }, allowSetters = true)
    private PermissionGroups permissionGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PermissionUsersGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public PermissionUsersGroup userEmail(String userEmail) {
        this.setUserEmail(userEmail);
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public PermissionLevel getPermissionLevel() {
        return this.permissionLevel;
    }

    public PermissionUsersGroup permissionLevel(PermissionLevel permissionLevel) {
        this.setPermissionLevel(permissionLevel);
        return this;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setPermissionUsersGroup(null));
        }
        if (items != null) {
            items.forEach(i -> i.setPermissionUsersGroup(this));
        }
        this.items = items;
    }

    public PermissionUsersGroup items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public PermissionUsersGroup addItem(Item item) {
        this.items.add(item);
        item.setPermissionUsersGroup(this);
        return this;
    }

    public PermissionUsersGroup removeItem(Item item) {
        this.items.remove(item);
        item.setPermissionUsersGroup(null);
        return this;
    }

    public PermissionGroups getPermissionGroup() {
        return this.permissionGroup;
    }

    public void setPermissionGroup(PermissionGroups permissionGroups) {
        this.permissionGroup = permissionGroups;
    }

    public PermissionUsersGroup permissionGroup(PermissionGroups permissionGroups) {
        this.setPermissionGroup(permissionGroups);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionUsersGroup)) {
            return false;
        }
        return id != null && id.equals(((PermissionUsersGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionUsersGroup{" +
            "id=" + getId() +
            ", userEmail='" + getUserEmail() + "'" +
            ", permissionLevel='" + getPermissionLevel() + "'" +
            "}";
    }
}
