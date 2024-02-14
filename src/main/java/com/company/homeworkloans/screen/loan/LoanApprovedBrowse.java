package com.company.homeworkloans.screen.loan;

import com.company.homeworkloans.entity.Client;
import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.Label;
import io.jmix.ui.model.CollectionLoader;
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

    @Autowired
    private GroupTable<Loan> loansTable;

    @Autowired
    private Notifications notifications;

    @Autowired
    private CollectionLoader<Loan> loansDl;

    @Install(to = "loansTable.clientAge", subject = "columnGenerator")
    private Component loansTableClientAgeColumnGenerator(final Loan loan) {
        Client client = loan.getClient();
        LocalDate birthDay = client.getBirthDate();
        Label<Integer> age = uiComponents.create(Label.TYPE_INTEGER);
        age.setValue(LocalDate.now().getYear() - birthDay.getYear());
        return age;
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

    @Subscribe("loansTable.approveAction")
    public void onLoansTableApproveAction(final Action.ActionPerformedEvent event) {
        Loan slectedLoan = loansTable.getSingleSelected();
        if (slectedLoan != null) {
            slectedLoan.setStatus(LoanStatus.APPROVED);
            dataManager.save(slectedLoan);
            loansDl.load();
            notifications.create()
                    .withDescription("Approved")
                    .show();
        }
    }

    @Subscribe("loansTable.rejectAction")
    public void onLoansTableRejectAction(final Action.ActionPerformedEvent event) {
        Loan slectedLoan = loansTable.getSingleSelected();
        if (slectedLoan != null) {
            slectedLoan.setStatus(LoanStatus.REJECTED);
            dataManager.save(slectedLoan);
            loansDl.load();
            notifications.create()
                    .withDescription("Rejected")
                    .show();
        }
    }
}