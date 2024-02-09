package fr.esgi.dvf;

import fr.esgi.dvf.exception.MissingParamException;
import fr.esgi.dvf.service.impl.LigneTransactionServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DvfApplication.class)

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:applicationTest.properties")
class LigneTransactionRestControllerIT {

    @Autowired
    private LigneTransactionServiceImpl ligneTransactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void shouldReturnPdfFileSuccessfully() throws Exception {
        assertNotNull(ligneTransactionService);
        ligneTransactionService.importer();
        String longitude = "4.848466";
        String latitude = "45.754752";
        String rayon = "100";

        String url = "/LigneTransaction/generatePdfByLocation?longitude=" + longitude + "&latitude=" + latitude + "&rayon=" + rayon;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("application/pdf", result.getResponse().getContentType()));
    }

    @Test
    @Order(2)
    void shouldReturnMissingParamException() throws Exception {
        String url = "/LigneTransaction/generatePdfByLocation";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(MissingParamException.class);
                });
    }
}
