package utils;

public class OptionParameters {
    private String optionType;
    private double strikePrice;
    private double maturity;
    private double interestRate;
    private double kappa;
    private double theta;
    private double sigma;
    private double rho;
    private int numSimulations;
    private int threadPoolSize;
    private double[] bermudanDates;

    public OptionParameters(String optionType, double strikePrice, double maturity, double interestRate,
                            double kappa, double theta, double sigma, double rho,
                            int numSimulations, int threadPoolSize) {
        this(optionType, strikePrice, maturity, interestRate, kappa, theta, sigma, rho, numSimulations, threadPoolSize, null);
    }

    public OptionParameters(String optionType, double strikePrice, double maturity, double interestRate,
                            double kappa, double theta, double sigma, double rho,
                            int numSimulations, int threadPoolSize, double[] bermudanDates) {
        if (strikePrice <= 0) throw new IllegalArgumentException("Strike price must be positive.");
        if (maturity <= 0) throw new IllegalArgumentException("Maturity must be positive.");
        if (interestRate < 0) throw new IllegalArgumentException("Interest rate cannot be negative.");
        if (numSimulations <= 0) throw new IllegalArgumentException("Number of simulations must be positive.");
        if (threadPoolSize <= 0) throw new IllegalArgumentException("Thread pool size must be positive.");
        if (bermudanDates != null && bermudanDates.length == 0) throw new IllegalArgumentException("Bermudan dates array cannot be empty.");
        if (!"European".equalsIgnoreCase(optionType) && !"American".equalsIgnoreCase(optionType) && !"Bermudan".equalsIgnoreCase(optionType))
            throw new IllegalArgumentException("Invalid option type. Valid types are 'European', 'American', or 'Bermudan'.");

        this.optionType = optionType;
        this.strikePrice = strikePrice;
        this.maturity = maturity;
        this.interestRate = interestRate;
        this.kappa = kappa;
        this.theta = theta;
        this.sigma = sigma;
        this.rho = rho;
        this.numSimulations = numSimulations;
        this.threadPoolSize = threadPoolSize;
        this.bermudanDates = bermudanDates;
    }

    public String getOptionType() { return optionType; }
    public double getStrikePrice() { return strikePrice; }
    public double getMaturity() { return maturity; }
    public double getInterestRate() { return interestRate; }
    public double getKappa() { return kappa; }
    public double getTheta() { return theta; }
    public double getSigma() { return sigma; }
    public double getRho() { return rho; }
    public int getNumSimulations() { return numSimulations; }
    public int getThreadPoolSize() { return threadPoolSize; }
    public double[] getBermudanDates() { return bermudanDates; }
}
