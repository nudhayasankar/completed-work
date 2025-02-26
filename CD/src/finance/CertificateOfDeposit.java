package finance;

import java.text.DecimalFormat;

public class CertificateOfDeposit {
    private double faceValue;
    private double rate;
    private int term;
    // Compounding period - default compounding annually
    private int cp  = 1;
    private DecimalFormat df = new DecimalFormat("0.00");

    public CertificateOfDeposit(double faceValue, double rate, int term) {
        this.faceValue = faceValue;
        this.rate = rate;
        this.term = term;
    }

    // Constructor chaining to avoid duplicated code
    public CertificateOfDeposit(double faceValue, double rate, int term, int cp) {
        this(faceValue, rate, term);
        this.cp = cp;
    }

    // Added getters and setters for all state elements
    public double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(double faceValue) {
        this.faceValue = faceValue;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    /** Calculated using formula A = P(1 + r/n)^(nt)
     *  where A is the amount at maturity
     *  P is the face value
     *  r is the rate
     *  t is the term
     *  n is the compounding period
     * @return The amount of the CD at maturity as a String
     * Would have preferred to return in double for further usage in other calculation
     * But using a string to aid in printing the output
     */
    public String calculateCDValue(){
        double amount = faceValue * Math.pow((1 + (rate/cp)), (cp * term));
        return df.format(amount);
    }
}
