package audit;

public interface AuditLogger {
    void log(String category, String action, String detail);
}
