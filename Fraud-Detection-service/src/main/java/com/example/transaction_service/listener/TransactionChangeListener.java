package com.example.transaction_service.listener;

import com.example.transaction_service.service.serviceImp.ProcessService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionChangeListener {

    @Autowired
    private ProcessService processService;


    @KafkaListener(topics = "dbserver1.public.fds004t_transaction")
    public void ecouter(String message) {
        System.out.println("🧾 Événement reçu : " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(message);

            log.info("listener içi ....... !");
            log.info(" json  object  içi ....... !" , json.toString());

//            String operation = json.get("op").asText(); // c, u, d
//            JsonNode data = json.get("after"); // pour insert/update
//
//            if ((operation.equals("c") || operation.equals("u")) && data != null) {
//                Map<String, Object> utilisateurData = mapper.convertValue(data, new TypeReference<>() {});
//                processService. startProcess(utilisateurData);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
