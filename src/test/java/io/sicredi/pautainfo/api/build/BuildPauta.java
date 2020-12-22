package io.sicredi.pautainfo.api.build;

import io.sicredi.pautainfo.api.model.Pauta;

import java.time.LocalDateTime;

public interface BuildPauta {

    static Pauta buildPautaGeneric() {
        return Pauta.builder()
                .id("123")
                .name("Test")
                .description("sicredi")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
