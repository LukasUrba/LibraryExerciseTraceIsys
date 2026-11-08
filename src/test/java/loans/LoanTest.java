package loans;

import org.junit.jupiter.api.Test;
import resources.Resource;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoanTest {

    @Test
    void overdueWorksAcrossMonths() {

        Resource book = new Resource("Book", "1984", "George Orwell");

        Loan loan = new Loan();
        loan.setResource(book);
        loan.setLoanDate(new GregorianCalendar(2026, Calendar.JANUARY, 20));

        assertFalse(loan.isOverdue(new GregorianCalendar(2026, Calendar.FEBRUARY, 10)));
        assertTrue(loan.isOverdue(new GregorianCalendar(2026, Calendar.FEBRUARY, 11)));
    }
}