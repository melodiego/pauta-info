package io.sicredi.pautainfo.api.dto;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaDTO implements Serializable {
    private static final long serialVersionUID = 5204707300913807812L;

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}