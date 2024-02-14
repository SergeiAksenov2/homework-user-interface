package com.company.homeworkloans.screen.requestloan;

import com.company.homeworkloans.entity.Client;
import com.company.homeworkloans.entity.Loan;
import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;


@DialogMode(width = "AUTO", height = "AUTO", resizable = true, forceDialog = true)
@UiController("RequestLoan")
@UiDescriptor("request_loan.xml")
public class RequestLoan extends Screen {

    @Autowired
    private Notifications notifications;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private EntityComboBox<Client> clientField;

    @Autowired
    private TextField<BigDecimal> amountField;

    @Subscribe("windowClose")
    public void onWindowClose(final Action.ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }

    public RequestLoan withClient(@Nullable Client client) {
        clientField.setValue(client);
        return this;
    }

    @Subscribe("windowCommitAndClose")
    public void onWindowCommitAndClose(final Action.ActionPerformedEvent event) throws NullPointerException {
        if (clientField != null && amountField != null) {
            BigDecimal amount = amountField.getValue();
            if (amount != null) {
                if (amount.signum() == 1) {
                    Client client = clientField.getValue();
                    LocalDate localdate = LocalDate.now();
                    Loan loan = dataManager.create(Loan.class);
                    loan.setClient(client);
                    loan.setAmount(amount);
                    loan.setRequestDate(localdate);
                    loan.setStatus(LoanStatus.REQUESTED);
                    dataManager.save(loan);
                    close(StandardOutcome.CLOSE);
                } else {
                    notifications.create()
                            .withType(Notifications.NotificationType.TRAY)
                            .withCaption("Please enter correct positive value")
                            .show();
                }
            }
        }
    }
}
