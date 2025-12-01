package com.ciandt.nextgen25.security.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface UserInterface 
{
    public Long getId();
    
    public String getName();
    
    public String getEmail();
    
    public String getType();
    
    default public String getPassword() {
        return null;
    }

    public Long getPDM();

    public Collection<? extends GrantedAuthority> getAuthorities();
}
