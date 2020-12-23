package io.sicredi.pautainfo.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.sicredi.pautainfo.api.build.BuildPautaDTO;
import io.sicredi.pautainfo.api.dto.PautaDTO;
import io.sicredi.pautainfo.api.dto.form.PautaFormDTO;
import io.sicredi.pautainfo.api.exception.BadRequestException;
import io.sicredi.pautainfo.api.exception.RecordNotFoundException;
import io.sicredi.pautainfo.api.service.PautaService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static io.sicredi.pautainfo.api.util.Constants.*;
import static io.sicredi.pautainfo.api.util.Constants.PautaConstants.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PautaController.class)
public class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String endpointAPI = "/pauta-info";

    @Test
    public void shouldCreatePautaWithAValidRequestTest() throws Exception {
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTO();

        when(pautaService.save(any())).thenReturn(pautaDTO);

        mockMvc.perform(
                post(endpointAPI).content(mapper.writeValueAsString(pautaDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(pautaDTO.getId()))
                .andExpect(jsonPath("$.name").value(pautaDTO.getName()))
                .andExpect(jsonPath("$.description").value(pautaDTO.getDescription()))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(pautaService).save(eq(pautaFormDTO));
    }

    @Test
    public void shouldCreatePautaWithAnInvalidRequestTest() throws Exception {
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTOEmpty();
        mockMvc.perform(
                post(endpointAPI).content(mapper.writeValueAsString(pautaFormDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUpdatePautaWithAValidRequestTest() throws Exception {
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTO();
        when(pautaService.update(anyString(), eq(pautaFormDTO))).thenReturn(pautaDTO);
        mockMvc.perform(put((String.format("%s/%s", endpointAPI, "123")))
                .content(mapper.writeValueAsString(pautaDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(pautaDTO.getId()))
                .andExpect(jsonPath("$.name").value(pautaDTO.getName()))
                .andExpect(jsonPath("$.description").value(pautaDTO.getDescription()))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(pautaService).update(anyString(), eq(pautaFormDTO));
    }

    @Test
    public void shouldUpdatePautaWithAnInvalidRequestTest() throws Exception {
        PautaFormDTO pautaFormDTO = BuildPautaDTO.buildPautaFormDTOEmpty();
        mockMvc.perform(put((String.format("%s/%s", endpointAPI, "123")))
                .content(mapper.writeValueAsString(pautaFormDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldGetPautaWithAValidIdTest() throws Exception {
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();
        when(pautaService.findById(anyString())).thenReturn(pautaDTO);
        String id = "123";

        mockMvc.perform(get((String.format("%s/%s", endpointAPI, id))))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(pautaDTO.getId()))
                .andExpect(jsonPath("$.name").value(pautaDTO.getName()))
                .andExpect(jsonPath("$.description").value(pautaDTO.getDescription()))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(pautaService).findById(id);
    }

    @Test
    public void shouldGetPautaWithAnInvalidIdTest() throws Exception {
        when(pautaService.findById(eq(" "))).thenThrow(new BadRequestException(THE_ID_CANNOT_BE_EMPTY));
        mockMvc.perform(get((String.format("%s/%s", endpointAPI, " "))))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value(KEY_BAD_REQUEST))
                .andExpect(jsonPath("$.details[0]").value(THE_ID_CANNOT_BE_EMPTY));
        verify(pautaService).findById(eq(" "));
    }

    @Test
    public void shouldDeletePautaWithAValidIdTest() throws Exception {
        mockMvc.perform(delete((String.format("%s/%s", endpointAPI, "123"))))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldDeletePautaWithAnInvalidIdTest() throws Exception {
        String id = "123";
        doThrow(new RecordNotFoundException(CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID + id))
                .when(pautaService).delete(eq(id));
        mockMvc.perform(delete((String.format("%s/%s", endpointAPI, id))))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value(KEY_NOT_FOUND))
                .andExpect(jsonPath("$.details[0]").value(CANNOT_FIND_ANY_REGISTRY_WITH_THIS_ID + id));
        verify(pautaService).delete(eq(id));
    }

    @Test
    public void shouldGetAllPauta() throws Exception {
        PautaDTO pautaDTO = BuildPautaDTO.buildPautaDTO();

        when(pautaService.findAll()).thenReturn(Collections.singletonList(pautaDTO));
        mockMvc.perform(
                get(endpointAPI).content(mapper.writeValueAsString(pautaDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(pautaService).findAll();
    }
}