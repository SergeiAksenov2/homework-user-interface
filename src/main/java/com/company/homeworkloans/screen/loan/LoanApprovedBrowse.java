package com.company.homeworkloans.screen.loan;

import com.company.homeworkloans.entity.Client;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.Label;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@UiController("LoanApproved.browse")
@UiDescriptor("loan-approved-browse.xml")
@LookupComponent("loansTable")
public class LoanApprovedBrowse extends StandardLookup<Loan> {

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private DataManager dataManager;

    @Install(to = "loansTable.clientAge", subject = "columnGenerator")
    private Component loansTableClientAgeColumnGenerator(final Loan loan) {
        Client client = loan.getClient();
        LocalDate birthDay = client.getBirthDate();
        Label<Integer> age = uiComponents.create(Label.TYPE_INTEGER);
        age.setValue(LocalDate.now().getYear() - birthDay.getYear());
        return age;
    }


    ////   dataLoadCoordinator выбор selected строки

    @Subscribe("loansTable")
    public void onLoansTableSelection(final Table.SelectionEvent<Loan> event) {
        Loan selectedLoan = event.getSource().getSingleSelected();
        if (selectedLoan != null) {
            Client selectedClient = selectedLoan.getClient();
        }
    }

    @Install(to = "clientLoansDl", target = Target.DATA_LOADER)
    private List<Loan> loansDlLoadDelegate(final LoadContext<Loan> loadContext) {
        Loan selectedLoan = (Loan) loadContext.getId();
        if (selectedLoan != null) {
            UUID clientId = selectedLoan.getClient().getId();
            return dataManager.load(Loan.class)
                    .query("select e from Loan e where  e.client.id =: clientId")
                    .parameter("clientId", clientId)
                    .list();
        }
        return Collections.emptyList();
    }
}