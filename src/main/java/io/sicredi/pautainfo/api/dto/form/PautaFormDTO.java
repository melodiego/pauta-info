package io.sicredi.pautainfo.api.dto.form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaFormDTO implements Serializable {
    private static final long serialVersionUID = -7521883227245124330L;

    @NotBlank(message = "The name could not be null or empty.")
    private String name;

    private String description;
}