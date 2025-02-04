package com.jichijima.todang.service.address;

import com.jichijima.todang.model.dto.address.AddressDto;
import com.jichijima.todang.model.entity.address.Address;
import com.jichijima.todang.repository.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<AddressDto> getAddresses(Long userId, Boolean isPrimary) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수 입력값입니다.");
        }

        List<Address> addresses;

        if (isPrimary == null) {
            // isPrimary 값이 없으면 모든 주소 가져오기
            addresses = addressRepository.findByUserId(userId);
        } else {
            // isPrimary가 true이면 대표 주소만 가져오기, false이면 전체 주소 가져오기
            addresses = addressRepository.findByUserIdAndIsPrimary(userId, isPrimary);
        }

        return addresses.stream()
                .map(address -> AddressDto.builder()
                        .id(address.getId())
                        .userId(address.getUserId())
                        .name(address.getName())
                        .isPrimary(address.getIsPrimary())
                        .address(address.getAddress())
                        .detail(address.getDetail())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AddressDto> getAllAddresses(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수 입력값입니다.");
        }

        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(address -> AddressDto.builder()
                        .id(address.getId())
                        .userId(address.getUserId())
                        .name(address.getName())
                        .isPrimary(address.getIsPrimary())
                        .address(address.getAddress())
                        .detail(address.getDetail())
                        .build())
                .collect(Collectors.toList());
    }



    @Transactional
    public AddressDto addAddress(AddressDto dto) {
        Address address = Address.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .isPrimary(dto.getIsPrimary())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .build();

        Address savedAddress = addressRepository.save(address);
        return AddressDto.builder()
                .id(savedAddress.getId())
                .userId(savedAddress.getUserId())
                .name(savedAddress.getName())
                .isPrimary(savedAddress.getIsPrimary())
                .address(savedAddress.getAddress())
                .detail(savedAddress.getDetail())
                .build();
    }

    public AddressDto getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("주소 ID가 존재하지 않습니다."));

        return AddressDto.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .name(address.getName())
                .isPrimary(address.getIsPrimary())
                .address(address.getAddress())
                .detail(address.getDetail())
                .build();
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new RuntimeException("주소 ID가 존재하지 않습니다.");
        }
        addressRepository.deleteById(addressId);
    }
}
