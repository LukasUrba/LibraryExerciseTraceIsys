package reporting;

import java.util.*;

public interface OutputService {
    void outputExtent(String title, Collection subscribers) throws Exception;

    String getName();
}
