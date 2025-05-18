package org.evilincorporated.pineapple.repository;

import org.evilincorporated.pineapple.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String username);
    Boolean existsByUsername(String username);
}
