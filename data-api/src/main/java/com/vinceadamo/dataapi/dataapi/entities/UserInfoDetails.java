package com.vinceadamo.dataapi.dataapi.entities;

import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 
  
import java.util.Collection;
import java.util.HashSet;
  
public class UserInfoDetails implements UserDetails { 
  
    private String name; 
    private String password; 
  
    public UserInfoDetails(User userInfo) { 
        name = userInfo.getEmail(); 
        password = userInfo.getPassword();
    } 
  
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }
  
    @Override
    public String getPassword() { 
        return password; 
    } 
  
    @Override
    public String getUsername() { 
        return name; 
    } 
  
    @Override
    public boolean isAccountNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isAccountNonLocked() { 
        return true; 
    } 
  
    @Override
    public boolean isCredentialsNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isEnabled() { 
        return true; 
    } 
} 