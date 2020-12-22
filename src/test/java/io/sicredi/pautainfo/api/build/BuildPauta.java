package io.sicredi.pautainfo.api.build;

import io.sicredi.pautainfo.api.dto.PautaDTO;
import io.sicredi.pautainfo.api.dto.form.PautaFormDTO;

import java.time.LocalDateTime;

public interface BuildPauta {

    static PautaFormDTO buildPautaFormDTO() {
        return PautaFormDTO.builder()
                .name("Test Sicredi")
                .description("Description")
                .build();
    }

    static PautaFormDTO buildPautaFormDTOEmpty() {
        return PautaFormDTO.builder().build();
    }

    static PautaDTO buildPautaDTO() {
        return PautaDTO.builder()
                .name("Test Sicredi")
                .description("Description")
                .createdAt(LocalDateTime.now())
                .build();
    }
}