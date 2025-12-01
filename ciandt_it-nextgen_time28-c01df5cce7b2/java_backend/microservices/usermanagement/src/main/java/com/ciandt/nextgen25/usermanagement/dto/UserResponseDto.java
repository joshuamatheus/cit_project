package com.ciandt.nextgen25.usermanagement.dto;

import com.ciandt.nextgen25.usermanagement.entity.PositionMap;
import com.ciandt.nextgen25.usermanagement.entity.Role;
import com.ciandt.nextgen25.usermanagement.entity.UserType;

public record UserResponseDto (
    
    Long id,

    String name,
    
    String email,

    UserType type,

    PositionMap positionMap,

    Role role,
    
    String pdmEmail
    
) {}
