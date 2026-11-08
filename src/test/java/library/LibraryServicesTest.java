package library;

import org.junit.jupiter.api.Test;
import resources.Resource;
import subscribers.Pensioner;
import subscribers.Student;

import java.util.GregorianCalendar;

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

        assertThrows(IllegalArgumentException.class, () ->
                service.loanResourceToSubscriber(game, pensioner, new GregorianCalendar())
        );

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


}
