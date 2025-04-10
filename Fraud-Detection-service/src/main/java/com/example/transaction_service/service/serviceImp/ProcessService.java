package com.example.transaction_service.service.serviceImp;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessService {


    public void  startProcess(Map<String, Object> data) {
        System.out.println("🚀 Processus déclenché avec données : " + data);
        // Exemple : envoyer un mail, appel REST, écrire en base, etc.
    }
}
