package fr.esgi.dvf.repository;

import fr.esgi.dvf.business.LigneTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface LigneTransactionRepository extends JpaRepository<LigneTransaction, Long> {

}
