package library;

import audit.AuditLogger;
import loans.Loan;
import org.junit.jupiter.api.Test;
import resources.Resource;
import subscribers.Pensioner;
import subscribers.PremiumMember;
import subscribers.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServicesTest {

    @Test
    void bookHasCorrectLoanPeriod() {
        Resource book = new Resource("Book", "1984", "George Orwell");

        assertEquals(21, book.getMaxLoanPeriod());
    }

    @Test
    void pensionerCannotBorrowGame() {
        LibraryServices service = new LibraryServices();

        Resource game = new Resource("Game", "Halo", "X-Box");
        Pensioner pensioner = new Pensioner("John");

        service.addResource(game);
        service.addPensioner(pensioner);

        assertThrows(IllegalArgumentException.class, () -> service.loanResourceToSubscriber(game, pensioner, new GregorianCalendar()));

        assertNull(game.getLoan());
    }

    @Test
    void studentCannotBorrowSameBookTwice() throws Exception {
        LibraryServices service = new LibraryServices();

        Student student = new Student("Alex", "University");
        Resource book = new Resource("Book", "The Name of the Wind", "Patrick Rothfuss");

        service.addStudent(student);
        service.addResource(book);

        GregorianCalendar date = new GregorianCalendar();

        service.loanResourceToSubscriber(book, student, date);

        assertThrows(IllegalStateException.class, () -> service.loanResourceToSubscriber(book, student, date));

        assertEquals(1, student.getLoans().size());
    }

    @Test
    void rejectedLoanIsAudited() {
        List<String> actions = new ArrayList<>();

        AuditLogger logger = (category, action, detail) -> actions.add(action);

        LibraryServices service = new LibraryServices(logger);

        Resource game = new Resource("Game", "Halo", "X-Box");
        Pensioner pensioner = new Pensioner("John");

        service.addResource(game);
        service.addPensioner(pensioner);

        assertThrows(IllegalArgumentException.class, () -> service.loanResourceToSubscriber(game, pensioner, new GregorianCalendar()));

        assertTrue(actions.contains("REJECTED"));
    }

    @Test
    void pensionerCannotExceedBorrowingLimit() throws Exception {
        LibraryServices service = new LibraryServices((category, action, detail) -> {
        });

        Pensioner pensioner = new Pensioner("John");
        service.addPensioner(pensioner);

        for (int i = 0; i < pensioner.getMaxBorrowing(); i++) {
            Resource book = new Resource("Book", "Book " + i, "Author");

            service.addResource(book);
            service.loanResourceToSubscriber(book, pensioner, new GregorianCalendar());
        }

        Resource extraBook = new Resource("Book", "Extra Book", "Author");
        service.addResource(extraBook);

        assertThrows(IllegalStateException.class, () -> service.loanResourceToSubscriber(extraBook, pensioner, new GregorianCalendar()));

        assertNull(extraBook.getLoan());
        assertEquals(pensioner.getMaxBorrowing(), pensioner.getLoans().size());
    }


    @Test
    void returningBookReleasesResource() throws Exception {
        LibraryServices service = new LibraryServices((category, action, detail) -> {
        });

        Student student = new Student("Alex", "University");
        Resource book = new Resource("Book", "Dune", "Frank Herbert");

        service.addStudent(student);
        service.addResource(book);

        GregorianCalendar loanDate = new GregorianCalendar(2026, Calendar.JANUARY, 1);

        service.loanResourceToSubscriber(book, student, loanDate);

        Loan loan = book.getLoan();
        assertNotNull(loan);

        double surcharge = service.returnedResource(loan, new GregorianCalendar(2026, Calendar.JANUARY, 5));

        assertNull(book.getLoan());
        assertTrue(student.getLoans().isEmpty());
        assertNotNull(loan.getReturnDate());
        assertEquals(0.0, surcharge);
    }


    @Test
    void premiumMemberCannotExceedBorrowingLimit() throws Exception {
        LibraryServices service = new LibraryServices();

        PremiumMember premium = new PremiumMember("John", 19.99);
        service.addPremiumMember(premium);

        // Borrow the maximum 10 books
        for (int i = 0; i < 10; i++) {
            Resource book = new Resource("Book", "Book " + i, "Author");

            service.addResource(book);
            service.loanResourceToSubscriber(book, premium, new GregorianCalendar());
        }

        assertEquals(10, premium.getLoans().size());

        // Attempt to borrow an 11th book
        Resource extraBook = new Resource("Book", "Extra Book", "Author");

        service.addResource(extraBook);

        assertThrows(IllegalStateException.class, () -> service.loanResourceToSubscriber(extraBook, premium, new GregorianCalendar()));

        // The rejected book should still be available
        assertNull(extraBook.getLoan());
        assertEquals(10, premium.getLoans().size());
    }


    @Test
    void xboxGameHasCorrectReturnSurcharge() throws Exception {
        LibraryServices service = new LibraryServices((category, action, detail) -> {
        });

        Student student = new Student("Alex", "University");
        Resource game = new Resource("Game", "Halo", "X-Box");

        service.addStudent(student);
        service.addResource(game);

        service.loanResourceToSubscriber(game, student, new GregorianCalendar(2026, Calendar.JANUARY, 1));

        double surcharge = service.returnedResource(game.getLoan(), new GregorianCalendar(2026, Calendar.JANUARY, 4));

        assertEquals(3.0, surcharge, 0.001);
    }


}
