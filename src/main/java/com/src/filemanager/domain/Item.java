package com.src.filemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.src.filemanager.domain.enumeration.ItemType;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ItemType type;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "permissionGroup" }, allowSetters = true)
    private PermissionUsersGroup permissionUsersGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemType getType() {
        return this.type;
    }

    public Item type(ItemType type) {
        this.setType(type);
        return this;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionUsersGroup getPermissionUsersGroup() {
        return this.permissionUsersGroup;
    }

    public void setPermissionUsersGroup(PermissionUsersGroup permissionUsersGroup) {
        this.permissionUsersGroup = permissionUsersGroup;
    }

    public Item permissionUsersGroup(PermissionUsersGroup permissionUsersGroup) {
        this.setPermissionUsersGroup(permissionUsersGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
