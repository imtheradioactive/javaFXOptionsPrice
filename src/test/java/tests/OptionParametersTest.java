package tests;

import org.junit.jupiter.api.Test;

import utils.OptionParameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OptionParametersTest {

    @Test
    public void testOptionParametersForEuropeanOption() {
        OptionParameters params = new OptionParameters(
                "European", 
                100.0, 
                1.0, 
                0.03, 
                1.5768, 
                0.0398, 
                0.3, 
                -0.5711, 
                1000000, 
                10
        );

        assertEquals("European", params.getOptionType());
        assertEquals(100.0, params.getStrikePrice());
        assertEquals(1.0, params.getMaturity());
        assertEquals(0.03, params.getInterestRate());
        assertEquals(1.5768, params.getKappa());
        assertEquals(0.0398, params.getTheta());
        assertEquals(0.3, params.getSigma());
        assertEquals(-0.5711, params.getRho());
        assertEquals(1000000, params.getNumSimulations());
        assertEquals(10, params.getThreadPoolSize());
        assertNull(params.getBermudanDates());
    }

    @Test
    public void testOptionParametersForBermudanOption() {
        double[] exerciseDates = {0.5, 1.0};

        OptionParameters params = new OptionParameters(
                "Bermudan", 
                100.0, 
                1.0, 
                0.03, 
                1.5768, 
                0.0398, 
                0.3, 
                -0.5711, 
                1000000, 
                10, 
                exerciseDates
        );

        assertEquals("Bermudan", params.getOptionType());
        assertEquals(100.0, params.getStrikePrice());
        assertEquals(1.0, params.getMaturity());
        assertEquals(0.03, params.getInterestRate());
        assertEquals(1.5768, params.getKappa());
        assertEquals(0.0398, params.getTheta());
        assertEquals(0.3, params.getSigma());
        assertEquals(-0.5711, params.getRho());
        assertEquals(1000000, params.getNumSimulations());
        assertEquals(10, params.getThreadPoolSize());
        assertArrayEquals(exerciseDates, params.getBermudanDates());
    }

    @Test
    public void testOptionParametersForAmericanOption() {
        OptionParameters params = new OptionParameters(
                "American", 
                100.0, 
                1.0, 
                0.03, 
                1.5768, 
                0.0398, 
                0.3, 
                -0.5711, 
                1000000, 
                10
        );

        assertEquals("American", params.getOptionType());
        assertEquals(100.0, params.getStrikePrice());
        assertEquals(1.0, params.getMaturity());
        assertEquals(0.03, params.getInterestRate());
        assertEquals(1.5768, params.getKappa());
        assertEquals(0.0398, params.getTheta());
        assertEquals(0.3, params.getSigma());
        assertEquals(-0.5711, params.getRho());
        assertEquals(1000000, params.getNumSimulations());
        assertEquals(10, params.getThreadPoolSize());
        assertNull(params.getBermudanDates());
    }
}
