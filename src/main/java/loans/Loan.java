package loans;

import reporting.OutputServiceContributor;
import resources.Resource;
import subscribers.Pensioner;
import subscribers.PremiumMember;
import subscribers.Student;

import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;

public class Loan implements OutputServiceContributor {
    private Resource resource;
    private Object subscriber;
    private GregorianCalendar loanDate;
    private GregorianCalendar returnDate;
    private final String id;
    private double returnTotal;

    private static int idCounter = 0;

    public String getFormattedString() {
        String formattedString = (resource == null ? "Unknown" : resource.getFormattedString()) + " loaned to ";
        if (subscriber == null) {
            formattedString += "Nobody";
        } else if (subscriber instanceof Student) {
            formattedString += ((Student) subscriber).getFormattedString();
        } else if (subscriber instanceof Pensioner) {
            formattedString += ((Pensioner) subscriber).getFormattedString();
        } else if (subscriber instanceof PremiumMember) {
            formattedString += ((PremiumMember) subscriber).getFormattedString();
        } else {
            formattedString += "Unknown subscriber type.";
        }
        return formattedString;
    }

    public GregorianCalendar getReturnDate() {
        return returnDate;
    }

    void setReturnDate(GregorianCalendar date) {
        if (returnDate != null) {
            throw new IllegalStateException("Loan has already been returned");
        }

        if (date == null || date.before(loanDate)) {
            throw new IllegalArgumentException("Invalid return date");
        }

        long days = ChronoUnit.DAYS.between(
                loanDate.toZonedDateTime().toLocalDate(),
                date.toZonedDateTime().toLocalDate()
        );

        returnTotal = resource.getSurcharge() *
                Math.max(1, days);

        returnDate = (GregorianCalendar) date.clone();
    }

    public double getReturnSurcharge() {
        return returnTotal;
    }

    public void setLoanDate(GregorianCalendar loanDate) {
        this.loanDate = loanDate;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }

    public Loan() {
        this.id = "L" + idCounter++;
    }

    public Loan(String id) {
        this.id = id;
        idCounter++;
    }

    public String getId() {
        return id;
    }

    public Resource getResource() {
        return resource;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    /**
     * A loan is overdue when the elapsed days exceed the resource's maximum loan
     * period. Handles month and year boundaries correctly.
     */
    public boolean isOverdue(GregorianCalendar today) {

        if (today == null || loanDate == null || resource == null || returnDate != null) {
            return false;
        }

        long days = ChronoUnit.DAYS.between(
                loanDate.toZonedDateTime().toLocalDate(),
                today.toZonedDateTime().toLocalDate()
        );

        return days > resource.getMaxLoanPeriod();
    }
}
