package io.sicredi.pautainfo.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.sicredi.pautainfo.api.build.BuildPauta;
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
        PautaDTO pautaDTO = BuildPauta.buildPautaDTO();
        PautaFormDTO pautaFormDTO = BuildPauta.buildPautaFormDTO();

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
        PautaFormDTO pautaFormDTO = BuildPauta.buildPautaFormDTOEmpty();
        mockMvc.perform(
                post(endpointAPI).content(mapper.writeValueAsString(pautaFormDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldUpdatePautaWithAValidRequestTest() throws Exception {
        PautaDTO pautaDTO = BuildPauta.buildPautaDTO();
        PautaFormDTO pautaFormDTO = BuildPauta.buildPautaFormDTO();
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
        PautaFormDTO pautaFormDTO = BuildPauta.buildPautaFormDTOEmpty();
        mockMvc.perform(put((String.format("%s/%s", endpointAPI, "123")))
                .content(mapper.writeValueAsString(pautaFormDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldGetPautaWithAValidIdTest() throws Exception {
        PautaDTO pautaDTO = BuildPauta.buildPautaDTO();
        when(pautaService.findById(anyString())).thenReturn(pautaDTO);

        mockMvc.perform(get((String.format("%s/%s", endpointAPI, "123"))))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(pautaDTO.getId()))
                .andExpect(jsonPath("$.name").value(pautaDTO.getName()))
                .andExpect(jsonPath("$.description").value(pautaDTO.getDescription()))
                .andExpect(jsonPath("$.createdAt").exists());

        verify(pautaService).findById("123");
    }

    @Test
    public void shouldGetPautaWithAnInvalidIdTest() throws Exception {
        when(pautaService.findById(eq(" "))).thenThrow(new BadRequestException("The id cannot be empty."));
        mockMvc.perform(get((String.format("%s/%s", endpointAPI, " "))))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details[0]").value("The id cannot be empty."));
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
        doThrow(new RecordNotFoundException("Cannot find any registry with this ID " + id))
                .when(pautaService).delete(eq(id));
        mockMvc.perform(delete((String.format("%s/%s", endpointAPI, id))))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("NOT_FOUND"))
                .andExpect(jsonPath("$.details[0]").value("Cannot find any registry with this ID " + id));
        verify(pautaService).delete(eq(id));
    }

    @Test
    public void shouldGetAllPauta() throws Exception {
        PautaDTO pautaDTO = BuildPauta.buildPautaDTO();

        when(pautaService.findAll()).thenReturn(Collections.singletonList(pautaDTO));
        mockMvc.perform(
                get(endpointAPI).content(mapper.writeValueAsString(pautaDTO)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(pautaService).findAll();
    }
}