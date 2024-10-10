package model;

import java.util.Random;

public class HestonModel {
    private double S0;  
    private double V0;  
    private double r;   
    private double kappa;  
    private double theta;  
    private double sigma;  
    private double rho;    
    private int steps;     
    private double T;      

    private Random random = new Random();

    public HestonModel(double S0, double V0, double r, double kappa, double theta, double sigma, double rho, int steps, double T) {
        this.S0 = S0;
        this.V0 = V0;
        this.r = r;
        this.kappa = kappa;
        this.theta = theta;
        this.sigma = sigma;
        this.rho = rho;
        this.steps = steps;
        this.T = T;
    }

    public double[] simulatePath() {
        double[] path = new double[steps + 1];
        path[0] = S0;
        double dt = T / steps;
        double S = S0;
        double V = V0;

        for (int i = 1; i <= steps; i++) {
            double dW1 = Math.sqrt(dt) * random.nextGaussian();
            double dW2 = rho * dW1 + Math.sqrt(1 - rho * rho) * Math.sqrt(dt) * random.nextGaussian();

            V = V + kappa * (theta - V) * dt + sigma * Math.sqrt(Math.max(V, 0)) * dW2;
            V = Math.max(V, 0);  

            S = S * Math.exp((r - 0.5 * Math.max(V, 0)) * dt + Math.sqrt(Math.max(V, 0)) * dW1);
            path[i] = S;
        }

        return path;
    }

    public double[][] simulatePaths(int numSimulations) {
        double[][] paths = new double[numSimulations][steps + 1];
        for (int i = 0; i < numSimulations; i++) {
            paths[i] = simulatePath();
        }
        return paths;
    }
}
