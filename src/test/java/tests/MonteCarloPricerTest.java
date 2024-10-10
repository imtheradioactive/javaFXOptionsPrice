package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import service.MonteCarloPricer;
import utils.OptionParameters;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonteCarloPricerTest {
    private MonteCarloPricer pricer;

    @BeforeEach
    public void setup() {
        String optionType = "European"; 
        double strikePrice = 100.0;
        double maturity = 1.0; 
        double interestRate = 0.025; 
        double kappa = 2.0; 
        double theta = 0.04; 
        double sigma = 0.3; 
        double rho = -0.7; 
        int numSimulations = 10000; 
        int threadPoolSize = 8; 

        OptionParameters params = new OptionParameters(optionType, strikePrice, maturity, interestRate,
                kappa, theta, sigma, rho, numSimulations, threadPoolSize, null);
        pricer = new MonteCarloPricer(params);
    }

    @Test
    public void testCallOptionPrice() {
        double expectedCallPrice = 3.52;
        double calculatedPrice = pricer.calculatePrice(true);
        assertEquals(expectedCallPrice, calculatedPrice, 0.5, 
            "The calculated call option price is incorrect. Adjust expected value or parameters.");
    }

    @Test
    public void testPutOptionPrice() {
        OptionParameters putParams = new OptionParameters("European", 100.0, 1.0, 0.025,
                2.0, 0.04, 0.3, -0.7, 10000, 8, null);

        MonteCarloPricer putPricer = new MonteCarloPricer(putParams);
        double expectedPutPrice = 11.13;
        double calculatedPrice = putPricer.calculatePrice(false);
        assertEquals(expectedPutPrice, calculatedPrice, 0.5, 
            "The calculated put option price is incorrect. Adjust expected value or parameters.");
    }

    @Test
    public void testComplexScenario() {
        double[] bermudanDates = {0.25, 0.5, 0.75}; 
        OptionParameters bermudanParams = new OptionParameters("Bermudan", 100.0, 1.0, 0.025,
                2.0, 0.04, 0.3, -0.7, 10000, 8, bermudanDates);

        MonteCarloPricer bermudanPricer = new MonteCarloPricer(bermudanParams);
        double expectedBermudanPrice = 3.4;
        double calculatedPrice = bermudanPricer.calculatePrice(true);
        assertEquals(expectedBermudanPrice, calculatedPrice, 0.5, 
            "The calculated Bermudan option price is incorrect. Adjust expected value or parameters.");
    }
}
