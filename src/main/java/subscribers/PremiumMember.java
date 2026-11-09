package subscribers;

import loans.Loan;
import reporting.OutputServiceContributor;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class PremiumMember implements OutputServiceContributor {
    private String id;
    private final String name;
    private final Map loans = new Hashtable();
    private final double monthlySubscription;

    public PremiumMember(String id, String name, double monthlySubscription) {
        this.id = id;
        this.name = name;
        this.monthlySubscription = monthlySubscription;
    }

    public PremiumMember(String name, double monthlySubscription) {
        this.name = name;
        this.monthlySubscription = monthlySubscription;
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
        return 10;
    }

    public String getFormattedString() {
        return "Premium member: " + name + " - monthly subscription " + monthlySubscription;
    }
}
