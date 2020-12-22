package io.sicredi.pautainfo.api;

import io.sicredi.pautainfo.api.dto.PautaDTO;
import io.sicredi.pautainfo.api.dto.form.PautaFormDTO;
import io.sicredi.pautainfo.api.service.PautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/pauta-info")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping
    public ResponseEntity<Collection<PautaDTO>> getAll() {
        return new ResponseEntity<>(pautaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaDTO> getById(@PathVariable(value = "id") @Valid String id) {
        return new ResponseEntity<>(pautaService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PautaDTO> save(@RequestBody @Valid PautaFormDTO pautaFormDTO) {
        return new ResponseEntity<>(pautaService.save(pautaFormDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PautaDTO> update(@PathVariable("id") String id, @RequestBody @Valid PautaFormDTO pautaFormDTO) {
        return new ResponseEntity<>(pautaService.update(id, pautaFormDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        String codeEscape = HtmlUtils.htmlEscape(id);
        pautaService.delete(codeEscape);
    }
}