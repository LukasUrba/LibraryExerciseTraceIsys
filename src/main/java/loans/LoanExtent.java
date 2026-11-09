/*
 * Created on 02-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package loans;

import resources.Resource;
import resources.ResourceExtent;
import subscribers.Pensioner;
import subscribers.PremiumMember;
import subscribers.Student;
import subscribers.SubscriberExtent;

import java.util.*;

public class LoanExtent {
    private static Map loans = new Hashtable();
    private static int idCounter = 0;
    public static LoanExtent INSTANCE = new LoanExtent();

    private LoanExtent() {
        try {
            //Initialise and register resources currently on loan
            createLoan(ResourceExtent.INSTANCE.findByPrimaryKey("R1"), (Student) SubscriberExtent.INSTANCE.findByPrimaryKey("S3"), new GregorianCalendar(2004, 00, 20));
            createLoan(ResourceExtent.INSTANCE.findByPrimaryKey("R5"), (Student) SubscriberExtent.INSTANCE.findByPrimaryKey("S3"), new GregorianCalendar(2004, 00, 01));
            createLoan(ResourceExtent.INSTANCE.findByPrimaryKey("R4"), (Pensioner) SubscriberExtent.INSTANCE.findByPrimaryKey("S1"), new GregorianCalendar(2004, 00, 01));
        } catch (Exception e) {
            System.out.println("Loan rejected: " + e.getMessage());
        }
    }

    public synchronized Loan createLoan(Resource resource,
                                        Object subscriber,
                                        GregorianCalendar date) throws Exception {

        Objects.requireNonNull(resource, "Resource cannot be null");
        Objects.requireNonNull(subscriber, "Subscriber cannot be null");
        Objects.requireNonNull(date, "Date cannot be null");

        if (resource.getLoan() != null) {
            throw new IllegalStateException("This resource is already on loan");
        }
        if (!(subscriber instanceof Student) &&
                !(subscriber instanceof Pensioner) &&
                !(subscriber instanceof PremiumMember)) {
            throw new IllegalArgumentException(
                    "Invalid subscriber type"
            );
        }

        if ("Game".equals(resource.getType()) &&
                !(subscriber instanceof Student)) {
            throw new IllegalArgumentException(
                    "Only students can borrow games"
            );
        }

        if (getLoanCount(subscriber) >= getBorrowingLimit(subscriber)) {
            throw new IllegalStateException(
                    "Borrowing limit reached"
            );
        }

        Loan loan = new Loan();
        loan.setResource(resource);
        loan.setSubscriber(subscriber);
        loan.setLoanDate(date);

        resource.setLoan(loan);

        try {
            addSubscriberLoan(subscriber, loan);
        } catch (Exception e) {
            resource.removeLoan();
            throw e;
        }

        loans.put(loan.getId(), loan);
        return loan;
    }

    private int getLoanCount(Object subscriber) {
        if (subscriber instanceof Student s)
            return s.getLoans().size();

        if (subscriber instanceof Pensioner p)
            return p.getLoans().size();

        return ((PremiumMember) subscriber).getLoans().size();
    }

    private int getBorrowingLimit(Object subscriber) {
        if (subscriber instanceof Student s)
            return s.getMaxBorrowing();

        if (subscriber instanceof Pensioner p)
            return p.getMaxBorrowing();

        return ((PremiumMember) subscriber).getMaxBorrowing();
    }

    private void addSubscriberLoan(Object subscriber, Loan loan)
            throws Exception {

        if (subscriber instanceof Student s)
            s.addLoan(loan);
        else if (subscriber instanceof Pensioner p)
            p.addLoan(loan);
        else
            ((PremiumMember) subscriber).addLoan(loan);
    }

    public Loan findByPrimaryKey(String id) {
        return (Loan) loans.get(id);
    }

    public synchronized Collection getLoans() {
        return new ArrayList(loans.values());
    }

    public synchronized double returnLoan(Loan loan, GregorianCalendar date) {

        if (loan == null || date == null || loans.get(loan.getId()) != loan) {
            throw new IllegalArgumentException("Invalid loan or return date");
        }

        loan.setReturnDate(date);

        Object subscriber = loan.getSubscriber();

        if (subscriber instanceof Student s)
            s.removeLoan(loan);
        else if (subscriber instanceof Pensioner p)
            p.removeLoan(loan);
        else ((PremiumMember) subscriber).removeLoan(loan);

        loan.getResource().removeLoan();

        return loan.getReturnSurcharge();
    }

    public String getNextId() {
        return "L" + (idCounter++);
    }
}
