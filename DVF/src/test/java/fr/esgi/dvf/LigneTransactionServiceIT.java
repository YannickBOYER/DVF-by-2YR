package fr.esgi.dvf;

import fr.esgi.dvf.repository.LigneTransactionRepository;
import fr.esgi.dvf.service.impl.LigneTransactionServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.scheduling.enabled=false")
class LigneTransactionServiceIT {

    @Mock
    private LigneTransactionRepository ligneTransactionRepository;

    @InjectMocks
    private LigneTransactionServiceImpl ligneTransactionService;

    String csvTestFilePath = "doc/csv/testCsv.csv";

    @Test
    @Order(1)
    void shouldImportFileSuccessfully() throws Exception{
        assertEquals(1000, ligneTransactionService.getNombreDeLignes());
    }
}
