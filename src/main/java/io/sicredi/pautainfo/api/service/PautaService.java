package io.sicredi.pautainfo.api.service;

import io.sicredi.pautainfo.api.dto.PautaDTO;
import io.sicredi.pautainfo.api.dto.form.PautaFormDTO;
import io.sicredi.pautainfo.api.exception.DuplicatedIDException;
import io.sicredi.pautainfo.api.exception.RecordNotFoundException;
import io.sicredi.pautainfo.api.model.Pauta;
import io.sicredi.pautainfo.api.repository.PautaRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PautaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PautaService.class);

    public static final String CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID = "Cannot find any registry with this ID ";

    private final PautaRepository pautaRepository;
    private final ModelMapper mapper;

    public PautaService(PautaRepository pautaRepository, ModelMapper mapper) {
        this.pautaRepository = pautaRepository;
        this.mapper = mapper;
    }

    public List<PautaDTO> findAll() {
        Collection<Pauta> pautas = pautaRepository.findAll();
        return pautas.stream().map(pauta -> mapper.map(pauta, PautaDTO.class)).collect(Collectors.toList());
    }

    public PautaDTO findById(String id) {
        Pauta entity = pautaRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID + id));
        return mapper.map(entity, PautaDTO.class);
    }

    public PautaDTO save(PautaFormDTO pautaFormDTO) {
        Pauta pauta = pautaRepository.save(mapper.map(pautaFormDTO, Pauta.class));
        return mapper.map(pauta, PautaDTO.class);
    }

    public PautaDTO update(String id, PautaFormDTO pautaFormDTO) {
        pautaRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID + id));
        Pauta pauta = mapper.map(pautaFormDTO, Pauta.class);
        pauta.setUpdatedAt(LocalDateTime.now());

        return mapper.map(pautaRepository.save(pauta), PautaDTO.class);
    }

    public void delete(String id) {
        Pauta pauta = pautaRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID + id));
        pautaRepository.delete(pauta);
    }

    public boolean existsPautaById(String id) {
        return pautaRepository.findById(id).isPresent();
    }
}