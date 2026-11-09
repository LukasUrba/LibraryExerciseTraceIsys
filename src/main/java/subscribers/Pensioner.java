package subscribers;

import loans.Loan;
import reporting.OutputServiceContributor;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class Pensioner implements OutputServiceContributor {
    private String id;
    private final String name;
    private final Map loans = new Hashtable();

    public Pensioner(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pensioner(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addLoan(Loan loan) throws Exception {
        int projectedLoanCount = loans.size();
        if (projectedLoanCount >= getMaxBorrowing()) {
            throw new Exception(name + " has reached their borrowing limit.");
        }
        loans.put(loan.getId(), loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan.getId());
    }

    public Collection getLoans() {
        return loans.values();
    }

    public int getMaxBorrowing() {
        return 5;
    }

    public String getFormattedString() {
        return "Pensioner: " + name;
    }
}
