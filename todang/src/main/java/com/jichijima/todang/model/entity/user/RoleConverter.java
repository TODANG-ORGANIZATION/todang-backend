package com.jichijima.todang.model.entity.user;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<User.Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(User.Role role) {
        if (role == null) return null;
        switch (role) {
            case CUSTOMER:
                return 0;
            case OWNER:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    @Override
    public User.Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        switch (dbData) {
            case 0:
                return User.Role.CUSTOMER;
            case 1:
                return User.Role.OWNER;
            default:
                throw new IllegalArgumentException("Unknown role id: " + dbData);
        }
    }
}
