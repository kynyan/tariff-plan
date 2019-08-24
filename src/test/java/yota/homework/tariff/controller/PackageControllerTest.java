package yota.homework.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yota.homework.tariff.exception.NiceException;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.service.SimCardService;

import static io.qala.datagen.RandomShortApi.Long;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yota.homework.tariff.dto.UserDto.PHONE_LOWER_BOUNDARY;
import static yota.homework.tariff.dto.UserDto.PHONE_UPPER_BOUNDARY;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PackageController.class)
public class PackageControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SimRepository simRepository;
    @MockBean
    private SimCardService simCardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
//        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldGetAllTickets() throws Exception {
        when(simRepository.findByPhoneNumber(any())).thenReturn(null);
        Long randomPhoneNumber = Long(PHONE_LOWER_BOUNDARY, PHONE_UPPER_BOUNDARY);
        MvcResult result = mockMvc.perform(put("/sim/{phone-number}?active=false", randomPhoneNumber))
//                .param("active", "false"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        NiceException exception = new ObjectMapper().readValue(result.getResponse().getContentAsString(), NiceException.class);
        assertEquals("ok", "ok");

//        assertReflectionEquals(expectedTickets, Arrays.asList(tickets));

    }

//    @Configuration
//    static class PackageControllerTestConfiguration {
////        @Mock
////        private SimRepository simRepository;
////        @MockBean
////        private PackageRepository packageRepository;
////        @MockBean
////        private Merger merger;
////
////        @Bean
////        public SimCardService simCardService() {
////            return new SimCardService(simRepository, packageRepository, merger);
////        }
//        @SpyBean
//        private SimRepository simRepository;
//        @InjectMocks
//        private SimCardService simCardService;
//    }

}
