
package loans;

import library.LibraryServices;
import org.junit.jupiter.api.Test;
import resources.Resource;
import subscribers.Student;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoanConcurrencyTest {

    @Test
    void onlyOneLibrarianCanBorrowSameBook() throws Exception {
        LibraryServices service = new LibraryServices(
                (category, action, detail) -> {
                }
        );

        Resource book = new Resource("Book", "Dune", "Author");
        service.addResource(book);

        ExecutorService executor = Executors.newFixedThreadPool(50);
        CountDownLatch ready = new CountDownLatch(50);
        CountDownLatch start = new CountDownLatch(1);

        try {
            List<Future<Boolean>> results = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                Student student = new Student("Student " + i, "Uni");
                service.addStudent(student);

                results.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();

                    try {
                        service.loanResourceToSubscriber(
                                book, student, new GregorianCalendar()
                        );
                        return true;
                    } catch (IllegalStateException e) {
                        return false;
                    }
                }));
            }

            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();

            int successes = 0;

            for (Future<Boolean> result : results) {
                if (result.get(10, TimeUnit.SECONDS)) {
                    successes++;
                }
            }

            assertEquals(1, successes);
        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }
}
