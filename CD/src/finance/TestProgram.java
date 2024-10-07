package finance;

/**
 * Test program for the CertificateOfDeposit class.
 * 
 * @author Will Provost
 */
public class TestProgram {
	/**
	 * Quick test: runs four scenarios, each using a different instance.
	 */
	public static void main (String[] args) {
		
		int faceValue = 1000;
		double rate = 0.05;
		int term = 10;
		//TODO create a CD and get its value at maturity
		CertificateOfDeposit cd = new CertificateOfDeposit(faceValue, rate, term);
		System.out.println("$ " + cd.calculateCDValue());
		
		faceValue = 800;
		rate = 0.045;
		term = 8;
		//TODO create a CD and get its value at maturity
		cd = new CertificateOfDeposit(faceValue, rate, term);
		System.out.println("$ " + cd.calculateCDValue());
		
		faceValue = 1000;
		rate = 0.05;
		term = 10;
		int numberOfPeriods = 2;
		//TODO create a CD and get its value at maturity
		cd = new CertificateOfDeposit(faceValue, rate, term, numberOfPeriods);
		System.out.println("$ " + cd.calculateCDValue());
		
		faceValue = 800;
		rate = 0.045;
		term = 8;
		numberOfPeriods = 12;
		//TODO create a CD and get its value at maturity
		cd = new CertificateOfDeposit(faceValue, rate, term, numberOfPeriods);
		System.out.println("$ " + cd.calculateCDValue());
	}
}
