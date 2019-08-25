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
import yota.homework.tariff.dto.UserDto;
import yota.homework.tariff.model.SimCard;
import yota.homework.tariff.repository.PackageRepository;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.repository.UserRepository;
import yota.homework.tariff.service.SimCardService;
import yota.homework.tariff.service.UserService;
import yota.homework.tariff.util.Merger;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    @Mock
    private SimRepository simRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Merger merger;
    @InjectMocks
    private SimCardService simCardService;

    private UserService userService;
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserService(userRepository, simCardService);
        this.userController = new UserController(userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldReturn400BadRequest_whenCreateUser_ifPhoneNumberAlreadyExists() throws Exception {
        UserDto dto = UserDto.randomUserDto();
        when(simRepository.findByPhoneNumber(any())).thenReturn(new SimCard());
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Phone number " + dto.getPhoneNumber() + " already exists")));
    }

    @Test
    void shouldReturn400BadRequest_whenCreateUser_ifUserDtoIsNotValid() throws Exception {
        UserDto dto = getInvalidUserDto();
        MvcResult result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResolvedException().getMessage().contains("Validation failed for argument"));
    }

    private UserDto getInvalidUserDto() {
        UserDto dto = UserDto.randomUserDto();
        dto.setFirstName(null);
        return dto;
    }
}
