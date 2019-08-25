package yota.homework.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yota.homework.tariff.dto.InternetPackageDto;
import yota.homework.tariff.dto.PackageDto;
import yota.homework.tariff.repository.PackageRepository;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.service.SimCardService;
import yota.homework.tariff.util.Merger;

import java.lang.Double;
import java.lang.Long;

import static io.qala.datagen.RandomShortApi.Long;
import static io.qala.datagen.RandomShortApi.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yota.homework.tariff.dto.UserDto.PHONE_LOWER_BOUNDARY;
import static yota.homework.tariff.dto.UserDto.PHONE_UPPER_BOUNDARY;

class PackageControllerTest {

    @Mock
    private SimRepository simRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private Merger merger;
    @InjectMocks
    private SimCardService simCardService;

    private PackageController packageController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.packageController = new PackageController(simCardService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(packageController).build();
    }

    @Test
    void shouldReturn400BadRequest_whenDeactivateSim_ifPhoneNumberIsNotFound() throws Exception {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        when(simRepository.findByPhoneNumber(randomPhoneNumber)).thenReturn(null);
        mockMvc.perform(put("/sim/{phone-number}", randomPhoneNumber)
                .param("active", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("SIM card with " + randomPhoneNumber + " phone number not found")));
    }

    @Test
    void shouldReturn400BadRequest_whenUpdatePackage_ifPhoneNumberIsNotFound() throws Exception {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        when(simRepository.findByPhoneNumber(randomPhoneNumber)).thenReturn(null);
        mockMvc.perform(post("/sim/{phone-number}/package", randomPhoneNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PackageDto())))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("SIM card with " + randomPhoneNumber + " phone number not found")));
    }

    @Test
    void shouldReturn400BadRequest_whenUpdatePackage_ifPackageDtoNotValid() throws Exception {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        PackageDto dto = getInvalidPackageDto();
        MvcResult result = mockMvc.perform(post("/sim/{phone-number}/package", randomPhoneNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResolvedException().getMessage().contains("Validation failed for argument"));
    }

    private PackageDto getInvalidPackageDto() {
        Double wrongGigabytes = negativeDouble();
        Integer wrongDays = integer(Integer.MIN_VALUE, -1);
        return new PackageDto(InternetPackageDto.builder().gigaBytes(wrongGigabytes).daysLeft(wrongDays).build(), null);
    }
}
