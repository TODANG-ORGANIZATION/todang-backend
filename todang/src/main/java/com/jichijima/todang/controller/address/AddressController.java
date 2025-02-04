package com.jichijima.todang.controller.address;

import com.jichijima.todang.model.dto.address.AddressDto;
import com.jichijima.todang.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "isPrimary", required = false) Optional<Boolean> isPrimary) {
        List<AddressDto> addresses;
        if (isPrimary.isPresent()) {
            addresses = addressService.getAddresses(userId, isPrimary.get());
        } else {
            addresses = addressService.getAllAddresses(userId);  // 모든 주소 반환
        }
        return ResponseEntity.ok(addresses);
    }



    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto addressDto) {
        return ResponseEntity.status(201).body(addressService.addAddress(addressDto));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable(name = "addressId") Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable(name = "addressId") Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
