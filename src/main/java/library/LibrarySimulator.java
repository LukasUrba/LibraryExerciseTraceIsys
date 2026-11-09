package library;

import reporting.ConsoleOutputService;
import resources.Resource;
import resources.ResourceExtent;
import subscribers.SubscriberExtent;
import reporting.FileOutputService;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class LibrarySimulator {
    LibraryServices library;
    LibraryReportingService reporting;

    public LibrarySimulator() throws IOException {
        library = new LibraryServices();
        reporting = new LibraryReportingService();
        reporting.addOutputService(new ConsoleOutputService());
        reporting.addOutputService(new FileOutputService("library-report.txt"));
    }

    public static void main(String[] args) throws IOException {
        LibrarySimulator librarySimulator = new LibrarySimulator();
        librarySimulator.runSimulator();
    }

    public void runReports() {
        reporting.outputResourcesReport();
        reporting.outputSubscribersReport();
        reporting.outputOutstandingLoansReport();
    }

    public void runSimulator() {
        reporting.outputOutstandingLoansReport();
        Resource resource = ResourceExtent.INSTANCE.findByPrimaryKey("R1");
        Object subscriber = SubscriberExtent.INSTANCE.findByPrimaryKey("S1");
        try {
            library.loanResourceToSubscriber(resource, subscriber, new GregorianCalendar(2004, Calendar.JANUARY, 20));

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Loan rejected: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        reporting.outputOutstandingLoansReport();
        double surcharge = library.returnedResource(resource.getLoan(), new GregorianCalendar(2004, Calendar.JANUARY, 20));
        System.out.println("Return surcharge: £" + surcharge);
        reporting.outputOutstandingLoansReport();
        try {
            library.loanResourceToSubscriber(resource, subscriber, new GregorianCalendar(2004, Calendar.JANUARY, 20));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        reporting.outputOutstandingLoansReport();
    }
}
