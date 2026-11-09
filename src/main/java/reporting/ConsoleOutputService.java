package reporting;

import java.util.*;

public class ConsoleOutputService implements OutputService {
    public ConsoleOutputService() {
    }

    public void outputExtent(String title, Collection subscribers) {
        System.out.println("\n" + title + "\n-----------");
        OutputServiceContributor subscriber;
        for (Object o : subscribers) {
            subscriber = (OutputServiceContributor) o;
            System.out.println(subscriber.getFormattedString());
        }
    }

    public String getName() {
        return "Console output service";
    }
}
