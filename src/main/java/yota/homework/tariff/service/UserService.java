package yota.homework.tariff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yota.homework.tariff.dto.UserDto;
import yota.homework.tariff.exception.ErrorCode;
import yota.homework.tariff.exception.NiceException;
import yota.homework.tariff.model.SimCard;
import yota.homework.tariff.model.User;
import yota.homework.tariff.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SimCardService simCardService;

    @Transactional
    public UserDto createUser(UserDto dto) {
        simCardService.checkIfPhoneNumberAlreadyExists(dto.getPhoneNumber());
        User created = findOrCreate(dto);
        return UserDto.builder()
                .firstName(created.getFirstName())
                .lastName(created.getLastName())
                .passport(created.getPassport())
                .phoneNumber(simCardService.getLatestRegisteredSimCard(created).getPhoneNumber())
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String errorMsg = "User with " + userId + " id not found";
                    return new NiceException(ErrorCode.ENTITY_NOT_FOUND, HttpStatus.NOT_FOUND, errorMsg);
                });
    }

    private User findOrCreate(UserDto dto) {
        User user = userRepository.findOneByPassport(dto.getPassport());
        if (user != null) {
            user.getSimCards().add(new SimCard(dto.getPhoneNumber(), user));
        } else user = new User(dto);
        return userRepository.save(user);
    }
}
