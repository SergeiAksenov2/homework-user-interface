package com.company.homeworkloans.screen.client;

import com.company.homeworkloans.screen.requestloan.RequestLoan;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("clientsTable")
public class ClientBrowse extends StandardLookup<Client> {

    @Autowired
    private GroupTable<Client> clientsTable;

    @Autowired
    private ScreenBuilders screenBuilders;

    @Subscribe("clientsTable.showRequestLoan")
    public void onClientsTableShowRequestLoan(final Action.ActionPerformedEvent event) {
        Client selected = clientsTable.getSingleSelected();
        if (selected == null) {
            return;
        }
        screenBuilders.screen(this)
                .withScreenClass(RequestLoan.class)
                .withOpenMode(OpenMode.DIALOG)
                .build()
                .withClient(selected)
                .show();
    }
}