package org.bank.fintech.model;

import java.util.*;

import jakarta.annotation.Generated;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name="usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String login;

    private String senha;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        if("admin".equals(this.role)){ 
            
            return List.of(new SimpleGrantedAuthority("ROLE_admin"), new SimpleGrantedAuthority("ROLE_user"));
        
        }else {
            return List.of(new SimpleGrantedAuthority("ROLE_user"));
        }
    }

    @Override
    public String getPassword(){
        return senha;
    }

    @Override
    public String getUsername(){
        return login;
    }
    
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
}
