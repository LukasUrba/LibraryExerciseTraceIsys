package library;

import org.junit.jupiter.api.Test;
import resources.Resource;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryServicesTest {

    @Test
    void bookHasCorrectLoanPeriod() {
        Resource book = new Resource("Book", "1984", "George Orwell");

        assertEquals(21, book.getMaxLoanPeriod());
    }
}
