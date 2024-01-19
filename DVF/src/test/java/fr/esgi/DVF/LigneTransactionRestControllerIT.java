package fr.esgi.DVF;

import fr.esgi.DVF.exception.ImportNotCompletedException;
import fr.esgi.DVF.exception.MissingParamException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LigneTransactionRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void generatePdfByLocationParam() throws Exception{
        String longitude = "4.848466";
        String latitude = "45.754752";
        String rayon = "100";

        String url = "/LigneTransaction/generatePdfByLocationParam?longitude=" + longitude + "&latitude=" + latitude + "&rayon=" + rayon;

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(reqResult -> {
                    if (reqResult.getResolvedException() instanceof MissingParamException){
                        System.out.println(reqResult.getResolvedException().getMessage());
                    } else if (reqResult.getResolvedException() instanceof ImportNotCompletedException) {
                        System.out.println(reqResult.getResolvedException().getMessage());
                    }else{
                        MockMvcResultMatchers.status().isOk().match(reqResult);
                    }
                });
    }


}
