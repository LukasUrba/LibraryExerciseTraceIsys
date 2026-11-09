/*
 * Created on 02-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package library;

import loans.Loan;
import loans.LoanExtent;
import reporting.OutputService;
import resources.ResourceExtent;
import subscribers.SubscriberExtent;

import java.util.Collection;
import java.util.Vector;

/**
 * @author Simon
 * <p>
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LibraryReportingService {
    Collection outputServices = new Vector();

    public void outputSubscribersReport() {
        OutputService outputService;
        for (Object service : outputServices) {
            outputService = (OutputService) service;
            try {
                outputService.outputExtent("Subscribers", SubscriberExtent.INSTANCE.getSubscribers());
            } catch (Exception e) {
                System.out.println("Exception while outputing subscriber to service " + outputService.getName() + " - Original message: " + e.getMessage());
            }
        }
    }

    /**
     * Generates a report on resources using the registered output services
     */
    public void outputResourcesReport() {
        OutputService outputService;
        for (Object service : outputServices) {
            outputService = (OutputService) service;
            try {
                outputService.outputExtent("Resources", ResourceExtent.INSTANCE.getResources());
            } catch (Exception e) {
                System.out.println("Exception while outputing resource to service " + outputService.getName() + " - Original message: " + e.getMessage());
            }
        }
    }

    public void outputOutstandingLoansReport() {
        OutputService outputService;
        Collection loans = LoanExtent.INSTANCE.getLoans();
        Collection outstandingLoans = new Vector();
        for (Object loan : loans) {
            Loan element = (Loan) loan;
            if (element.getReturnDate() == null) {
                outstandingLoans.add(element);
            }
        }
        for (Object service : outputServices) {
            outputService = (OutputService) service;
            try {
                outputService.outputExtent("Loans", outstandingLoans);
            } catch (Exception e) {
                System.out.println("Exception while outputing loan to service " + outputService.getName() + " - Original message: " + e.getMessage());
            }
        }
    }

    public void addOutputService(OutputService outputService) {
        outputServices.add(outputService);
    }

    public void removeOutputService(OutputService outputService) {
        outputServices.remove(outputService);
    }


}
