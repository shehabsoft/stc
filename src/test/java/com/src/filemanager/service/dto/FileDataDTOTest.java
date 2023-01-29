package com.src.filemanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.src.filemanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDataDTO.class);
        FileDataDTO fileDataDTO1 = new FileDataDTO();
        fileDataDTO1.setId(1L);
        FileDataDTO fileDataDTO2 = new FileDataDTO();
        assertThat(fileDataDTO1).isNotEqualTo(fileDataDTO2);
        fileDataDTO2.setId(fileDataDTO1.getId());
        assertThat(fileDataDTO1).isEqualTo(fileDataDTO2);
        fileDataDTO2.setId(2L);
        assertThat(fileDataDTO1).isNotEqualTo(fileDataDTO2);
        fileDataDTO1.setId(null);
        assertThat(fileDataDTO1).isNotEqualTo(fileDataDTO2);
    }
}
