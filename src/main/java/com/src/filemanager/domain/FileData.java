package com.src.filemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A FileData.
 */
@Entity
@Table(name = "file_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "binery")
    private byte[] binery;

    @Column(name = "binery_content_type")
    private String bineryContentType;

    @JsonIgnoreProperties(value = { "permissionUsersGroup" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBinery() {
        return this.binery;
    }

    public FileData binery(byte[] binery) {
        this.setBinery(binery);
        return this;
    }

    public void setBinery(byte[] binery) {
        this.binery = binery;
    }

    public String getBineryContentType() {
        return this.bineryContentType;
    }

    public FileData bineryContentType(String bineryContentType) {
        this.bineryContentType = bineryContentType;
        return this;
    }

    public void setBineryContentType(String bineryContentType) {
        this.bineryContentType = bineryContentType;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public FileData item(Item item) {
        this.setItem(item);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileData)) {
            return false;
        }
        return id != null && id.equals(((FileData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileData{" +
            "id=" + getId() +
            ", binery='" + getBinery() + "'" +
            ", bineryContentType='" + getBineryContentType() + "'" +
            "}";
    }
}
