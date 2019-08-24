package yota.homework.tariff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yota.homework.tariff.dto.InternetPackageDto;
import yota.homework.tariff.dto.MinutesPackageDto;
import yota.homework.tariff.dto.PackageDto;
import yota.homework.tariff.exception.ErrorCode;
import yota.homework.tariff.exception.NiceException;
import yota.homework.tariff.model.*;
import yota.homework.tariff.repository.PackageRepository;
import yota.homework.tariff.repository.SimRepository;
import yota.homework.tariff.util.Merger;

import java.time.Instant;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class SimCardService {
    private Comparator<SimCard> comparator = Comparator.comparing(SimCard::getRegistrationDate);
    private final SimRepository simRepository;
    private final PackageRepository packageRepository;
    private final Merger merger;

    SimCard getLatestRegisteredSimCard(User user) {
        return user.getSimCards().stream().max(comparator).orElseThrow(() -> {
            String errorMsg = "Internal server error. Probably, sim cards list is empty";
            return new NiceException(ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, errorMsg);
        });
    }

    void checkIfPhoneNumberAlreadyExists(Long phoneNumber) {
        SimCard simCard = simRepository.findByPhoneNumber(phoneNumber);
        if (simCard != null) {
            String errorMsg = "Phone number " + phoneNumber + " already exists";
            throw new NiceException(ErrorCode.DATA_FORMAT_ERROR, HttpStatus.BAD_REQUEST, errorMsg);
        }
    }

    @Transactional
    public PackageDto getGigsAndMinutesLeft(Long phoneNumber) {
        Plan tariffPlan = getSimCardByPhone(phoneNumber).getPlan();
        return tariffPlan != null ? convertToPackageDto(tariffPlan) : new PackageDto();
    }

    @Transactional
    public PackageDto updatePackage(Long phoneNumber, PackageDto dto) {
        SimCard simCard = getSimCardByPhone(phoneNumber);
        Plan tariffPlan = simCard.getPlan();
        if (tariffPlan == null) tariffPlan = new Plan();
        if (dto.getMinutesPackage() != null) updatePlanMinutesPackage(tariffPlan, dto.getMinutesPackage());
        if (dto.getInternetPackage() != null) updatePlanInternetPackage(tariffPlan, dto.getInternetPackage());
        simCard.setPlan(tariffPlan);
        SimCard updated = simRepository.save(simCard);
        return convertToPackageDto(updated.getPlan());
    }

    @Transactional
    public void activateOrBlockSim(Long phoneNumber, boolean active) {
        SimCard simCard = getSimCardByPhone(phoneNumber);
        simCard.setActive(active);
        simRepository.save(simCard);
    }

    private SimCard getSimCardByPhone(Long phoneNumber) {
        SimCard simCard = simRepository.findByPhoneNumber(phoneNumber);
        if (simCard == null) {
            String errorMsg = "SIM card with " + phoneNumber + " phone number not found";
            throw new NiceException(ErrorCode.ENTITY_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR, errorMsg);
        }
        return simCard;
    }

    private void updatePlanInternetPackage(Plan tariffPlan, InternetPackageDto dto) {
        //todo: fix null instant daysLeft when gigabytes and minutes are not null
        InternetPackage update = new InternetPackage(dto);
        if (tariffPlan.getInternetPackage() != null) {
            InternetPackage merged = merger.merge(update, tariffPlan.getInternetPackage(), Instant.class);
            tariffPlan.setInternetPackage(packageRepository.save(merged));
        } else {
            tariffPlan.setInternetPackage(packageRepository.save(update));
        }

    }

    private void updatePlanMinutesPackage(Plan tariffPlan, MinutesPackageDto dto) {
        MinutesPackage update = new MinutesPackage(dto);
        if (tariffPlan.getMinutesPackage() != null) {
            MinutesPackage merged = merger.merge(update, tariffPlan.getMinutesPackage(), Instant.class);
            tariffPlan.setMinutesPackage(packageRepository.save(merged));
        } else {
            tariffPlan.setMinutesPackage(packageRepository.save(update));
        }
    }

    private PackageDto convertToPackageDto(Plan plan) {
        PackageDto packageDto = new PackageDto();
        if (plan.getInternetPackage() != null) packageDto.setInternetPackage(plan.getInternetPackage().toDto());
        if (plan.getMinutesPackage() != null) packageDto.setMinutesPackage(plan.getMinutesPackage().toDto());
        return packageDto;
    }

}
