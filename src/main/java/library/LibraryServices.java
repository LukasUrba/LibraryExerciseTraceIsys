package library;

import loans.Loan;
import loans.LoanExtent;
import resources.Resource;
import resources.ResourceExtent;
import subscribers.Pensioner;
import subscribers.PremiumMember;
import subscribers.Student;
import subscribers.SubscriberExtent;
import audit.AuditLogger;
import audit.ConsoleAuditLogger;

import java.util.GregorianCalendar;

public class LibraryServices {

	private final AuditLogger audit;

	public LibraryServices() {
		this(new ConsoleAuditLogger());
	}
	public LibraryServices(AuditLogger audit) {
		this.audit = audit;
	}
	public void addResource(Resource resource) {
		ResourceExtent.INSTANCE.createResource(resource);
	}
	public void addPensioner(Pensioner pensioner) {
		SubscriberExtent.INSTANCE.createPensioner(pensioner);
	}
	public void addStudent(Student student) {
		SubscriberExtent.INSTANCE.createStudent(student);
	}
	public void addPremiumMember(PremiumMember premiumMember) {
		SubscriberExtent.INSTANCE.createPremiumMember(premiumMember);
	}

	
	public void loanResourceToSubscriber(Resource resource, Object subscriber, GregorianCalendar date) throws Exception {

		Loan loan;

		try {
			loan = LoanExtent.INSTANCE.createLoan(resource, subscriber, date);
		} catch (Exception e) {
			audit.log("LOAN", "REJECTED", e.getMessage());
			throw e;
		}

		audit.log("LOAN", "CREATED", "Loan " + loan.getId() + " created");
	}
	public double returnedResource(Loan loan, GregorianCalendar date) {

		double surcharge;

		try {
			surcharge = LoanExtent.INSTANCE.returnLoan(loan, date);
		} catch (RuntimeException e) {
			audit.log("LOAN", "RETURN_REJECTED", e.getMessage());
			throw e;
		}

		audit.log("LOAN", "RETURNED", "Loan " + loan.getId() + " returned. Surcharge: £ " + surcharge);

		return surcharge;
	}

}
