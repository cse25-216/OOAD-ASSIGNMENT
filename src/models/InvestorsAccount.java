package models;

public class InvestorsAccount extends Account implements Withdrawable, InterestBearing {

    private static final double INTEREST_RATE = 0.05;

    public InvestorsAccount(int id, int userId, double balance) {
        super(id, userId, "Investors", balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            return false;
        }

        if (amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}
