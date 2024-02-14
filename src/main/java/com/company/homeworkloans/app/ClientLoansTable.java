package com.company.homeworkloans.app;

import com.company.homeworkloans.entity.Loan;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ClientLoansTable {

    @Autowired
    private DataManager dataManager;

    List<Loan> getClientLoans(UUID clientId) {
        return dataManager.load(Loan.class)
                .query("")
                .parameter("clientId", clientId)
                .list();
    }
}