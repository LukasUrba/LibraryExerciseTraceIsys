package resources;

import reporting.OutputServiceContributor;
import loans.*;

public class Resource implements OutputServiceContributor {
    private String id;
    private final String title;
    private String gameType;
    private final String type;
    private String author;

    private Loan loan;

    public Resource(String type, String title, String extra) {
        this.type = type;
        this.title = title;
        if (type.equals("Book")) {
            this.author = extra;
        } else {
            this.gameType = extra;
        }
    }

    public Resource(String type, String id, String title, String extra) {
        this.type = type;
        this.id = id;
        this.title = title;
        if (type.equals("Book")) {
            this.author = extra;
        } else {
            this.gameType = extra;
        }
    }


    void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    /**
     * Records the loan against this resource. Games may be loaned to Students
     * and Premium members; books may be loaned to any subscriber.
     */
    public void setLoan(Loan loan) throws Exception {
        if (this.loan != null) {
            throw new Exception("This resource is already on loan: " + this.loan.getFormattedString());
        }
        this.loan = loan;
    }

    public void removeLoan() {
        loan = null;
    }

    public Loan getLoan() {
        return loan;
    }

    public int getMaxLoanPeriod() {
        if (type.equals("Book")) {
            return 21;
        }
        return switch (gameType) {
            case "X-Box" -> 7;
            case "PS2" -> 7;
            case "Amstrad" -> 14;
            default -> 28;
        };
    }


    public double getSurcharge() {
        if (type.equals("Book")) {
            return 0;
        }
        return switch (gameType) {
            case "X-Box" -> 1.0;
            case "PS2" -> 1.5;
            case "Amstrad" -> .5;
            default -> 0;
        };
    }

    public String getFormattedString() {
        if (type.equals("Book"))
            return "Book - " + title + " by " + author;
        else
            return "Game - " + title + " for the " + gameType;
    }
}
