package yota.homework.tariff.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yota.homework.tariff.dto.PackageDto;
import yota.homework.tariff.service.SimCardService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/sim/{phoneNumber}")
@RequiredArgsConstructor
public class PackageController {
    private final SimCardService simCardService;

    @PostMapping(path = "/package")
    @ApiOperation(httpMethod = "POST", value = "updates tariff plan's package (either internet or minutes)",
            notes = "Given phone number, may change number of available minutes or gigabytes as well as " +
                    "their expiration date")
    public ResponseEntity changePackage(@PathVariable Long phoneNumber, @Valid @RequestBody PackageDto update) {
        return ResponseEntity.ok(simCardService.updatePackage(phoneNumber, update));
    }

    @GetMapping(path = "/package")
    @ApiOperation(httpMethod = "GET", value = "gets available internet traffic and minutes")
    public ResponseEntity<PackageDto> createActualMaterialsInParticipant(@PathVariable Long phoneNumber) {
        return ResponseEntity.ok(simCardService.getGigsAndMinutesLeft(phoneNumber));
    }

    @PutMapping
    @ApiOperation(httpMethod = "PUT", value = "activates/deactivates SIM card")
    public ResponseEntity activateOrDeactivateSimCard(@PathVariable Long phoneNumber, @RequestParam boolean active) {
        simCardService.activateOrBlockSim(phoneNumber, active);
        return ResponseEntity.noContent().build();
    }
}
