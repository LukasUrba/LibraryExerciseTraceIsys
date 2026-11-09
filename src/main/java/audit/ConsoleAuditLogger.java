package audit;

public class ConsoleAuditLogger implements AuditLogger{
    @Override
    public void log(
            String category, String action, String detail) {

        System.out.println(
                "AUDIT: " + category + " | " +
                        action + " | " + detail
        );
    }
}
