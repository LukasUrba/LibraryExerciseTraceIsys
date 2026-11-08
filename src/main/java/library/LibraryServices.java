package library;

import loans.Loan;
import loans.LoanExtent;
import resources.Resource;
import resources.ResourceExtent;
import subscribers.Pensioner;
import subscribers.PremiumMember;
import subscribers.Student;
import subscribers.SubscriberExtent;

import java.util.GregorianCalendar;

public class LibraryServices {
	
	public LibraryServices() {
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
	
	public void loanResourceToSubscriber(Resource resource, Object subscriber, GregorianCalendar gregorianCalendar) throws Exception {
		LoanExtent.INSTANCE.createLoan(resource, subscriber, gregorianCalendar);
	}
	public double returnedResource(Loan loan, GregorianCalendar date) {
		return LoanExtent.INSTANCE.returnLoan(loan, date);
	}

}
