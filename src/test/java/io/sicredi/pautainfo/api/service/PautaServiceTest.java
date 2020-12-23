package io.sicredi.pautainfo.api.service;

import io.sicredi.pautainfo.api.build.BuildPauta;
import io.sicredi.pautainfo.api.build.BuildPautaDTO;
import io.sicredi.pautainfo.api.dto.PautaDTO;
import io.sicredi.pautainfo.api.dto.form.PautaFormDTO;
import io.sicredi.pautainfo.api.exception.RecordNotFoundException;
import io.sicredi.pautainfo.api.model.Pauta;
import io.sicredi.pautainfo.api.repository.PautaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PautaServiceTest {
    private static final String id = "1231";
    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PautaService pautaService;

    @Test
    public void shouldSavePautaDTOValidTest() {
        Pauta pautaIn = BuildPauta.buildPautaGeneric();
        Pauta pautaOut = BuildPauta.buildPautaGeneric();
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTO();
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();
        when(pautaRepository.save(pautaIn)).thenReturn(pautaOut);
        when(mapper.map(any(), eq(Pauta.class))).thenReturn(pautaIn);
        when(mapper.map(any(), eq(PautaDTO.class))).thenReturn(pautaDTO);

        PautaDTO pautaDTOSaved = pautaService.save(pautaFormDTO);
        assertEquals(pautaDTOSaved, pautaDTO);

        verify(pautaRepository).save(eq(pautaIn));
    }

    @Test
    public void shouldUpdatePautaDTOValidTest() {
        Pauta pautaIn = BuildPauta.buildPautaGeneric();
        Pauta pautaOut = BuildPauta.buildPautaGeneric();
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTO();
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();
        when(pautaRepository.findById(id)).thenReturn(Optional.of(pautaIn));
        when(pautaRepository.save(pautaIn)).thenReturn(pautaOut);
        when(mapper.map(any(), eq(Pauta.class))).thenReturn(pautaIn);
        when(mapper.map(any(), eq(PautaDTO.class))).thenReturn(pautaDTO);
        PautaDTO pautaDTOSaved = pautaService.update(id, pautaFormDTO);
        assertEquals(pautaDTOSaved, pautaDTO);
        verify(pautaRepository).save(eq(pautaIn));
    }

    @Test(expected = RecordNotFoundException.class)
    public void shouldUpdatePautaInvalidThrowRecordNotFoundExceptionTest() {
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTO();
        when(pautaRepository.findById(id)).thenReturn(Optional.empty());
        pautaService.update(id, pautaFormDTO);
    }

    @Test
    public void shouldFindByIdValidTest() {
        Optional<Pauta> pauta = Optional.of(BuildPauta.buildPautaGeneric());
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();

        when(pautaRepository.findById(id)).thenReturn(pauta);
        when(mapper.map(any(), eq(PautaDTO.class))).thenReturn(pautaDTO);

        PautaDTO pautaDTOSaved = pautaService.findById(id);
        assertNotNull(pautaDTOSaved);
        assertEquals(pautaDTO, pautaDTOSaved);
        assertNotNull(pautaDTO.getId());
        verify(pautaRepository).findById(id);
    }

    @Test(expected = RecordNotFoundException.class)
    public void shouldFindByIdInvalidTest() {
        Optional<Pauta> entity = Optional.empty();
        when(pautaRepository.findById(id)).thenReturn(entity);
        pautaService.findById(id);
        verify(pautaRepository).findById(id);
    }

    @Test(expected = RecordNotFoundException.class)
    public void shouldDeleteInvalidTest() {
        Optional<Pauta> entity = Optional.empty();
        when(pautaRepository.findById(id)).thenReturn(entity);
        pautaService.delete(id);
    }

    @Test
    public void shouldFindAllPautaTest() {
        when(pautaRepository.findAll()).thenReturn(Collections.singletonList(BuildPauta.buildPautaGeneric()));
        List<PautaDTO> pautas = pautaService.findAll();
        assertEquals(pautas.size(), 1);
        verify(pautaRepository).findAll();
    }
}