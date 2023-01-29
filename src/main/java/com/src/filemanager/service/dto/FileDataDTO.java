package com.src.filemanager.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.src.filemanager.domain.FileData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileDataDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] binery;

    private String bineryContentType;
    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBinery() {
        return binery;
    }

    public void setBinery(byte[] binery) {
        this.binery = binery;
    }

    public String getBineryContentType() {
        return bineryContentType;
    }

    public void setBineryContentType(String bineryContentType) {
        this.bineryContentType = bineryContentType;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDataDTO)) {
            return false;
        }

        FileDataDTO fileDataDTO = (FileDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDataDTO{" +
            "id=" + getId() +
            ", binery='" + getBinery() + "'" +
            ", item=" + getItem() +
            "}";
    }
}
