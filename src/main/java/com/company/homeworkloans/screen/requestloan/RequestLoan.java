package com.company.homeworkloans.screen.requestloan;

import com.company.homeworkloans.entity.Client;
import com.company.homeworkloans.entity.Loan;
import com.company.homeworkloans.entity.LoanStatus;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import io.jmix.core.DataManager;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.component.validation.NotBlankValidator;
import io.jmix.ui.component.validation.PositiveValidator;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;


@DialogMode(width = "AUTO", height = "AUTO", resizable = true, forceDialog = true)
@UiController("RequestLoan")
@UiDescriptor("request_loan.xml")
public class RequestLoan extends Screen {

//    @Autowired
//    private CollectionContainer<Client> clientsDc;

//    @Autowired
//    private DataContext dataContext;


    @Autowired
    private Notifications notifications;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private EntityComboBox<Client> clientField;

    @Autowired
    private TextField<BigDecimal> amountField;

    @Autowired
    protected ApplicationContext applicationContext;

//    @Autowired
//    private Dialogs dialogs;


//    public RequestLoan withClient(@Nullable Client client) {
//        //clientsDc..load();
//        return this;
//    }

    @Subscribe("windowClose")
    public void onWindowClose(final Action.ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }

//    @Subscribe("closeBtn")
//    public void onCloseBtnClick(final Button.ClickEvent event) {
//        close(StandardOutcome.CLOSE);
//    }


//    @Subscribe("commitAndCloseBtn")
//    protected void onCommitAndCloseBtnClick(Button.ClickEvent event) {
//
//        PositiveValidator positiveValidator = applicationContext.getBean(PositiveValidator.class);
//        amountField.addValidator(positiveValidator);
//
////        NotBlankValidator notBlankValidator = applicationContext.getBean(NotBlankValidator.class);
////        amountField.addValidator(notBlankValidator);
//    }


    @Subscribe("windowCommitAndClose")
    public void onWindowCommitAndClose(final Action.ActionPerformedEvent event) throws NullPointerException {
        if (clientField == null || amountField == null


        ) {
            int i = 0;
        } else {

            BigDecimal amount = amountField.getValue();

            if (amount.signum() == 1) {
                int e=0;

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
                int r=0;

                notifications.create()
                        .withType(Notifications.NotificationType.TRAY)
                        .withCaption("Please, enter correct values")
                        .withDescription("Description!")
                        .show();
            }

        }
    }
}
