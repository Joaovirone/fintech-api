package org.bank.fintech.repository;
import org.bank.fintech.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
    
    UserDetails findByLogin(String Login);
}
