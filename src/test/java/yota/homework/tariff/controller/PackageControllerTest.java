package yota.homework.tariff.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import yota.homework.tariff.repository.PackageRepository;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.service.SimCardService;
import yota.homework.tariff.util.Merger;

import static io.qala.datagen.RandomShortApi.Long;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yota.homework.tariff.dto.UserDto.PHONE_LOWER_BOUNDARY;
import static yota.homework.tariff.dto.UserDto.PHONE_UPPER_BOUNDARY;

@ExtendWith(SpringExtension.class)
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

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.packageController = new PackageController(simCardService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(packageController).build();

    }

    @Test
    void shoulReturnNiceException() throws Exception {
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        when(simRepository.findByPhoneNumber(randomPhoneNumber)).thenReturn(null);
        mockMvc.perform(put("/sim/{phone-number}", randomPhoneNumber)
                .param("active", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("SIM card with " + randomPhoneNumber + " phone number not found")));
    }
}
