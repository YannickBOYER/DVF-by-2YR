package fr.esgi.dvf.repository;

import fr.esgi.dvf.business.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PdfRepository extends JpaRepository<Pdf, Long> {

}
