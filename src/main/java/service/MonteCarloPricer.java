package service;

import model.HestonModel;
import utils.OptionParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.DoubleStream;
import java.util.Arrays;

public class MonteCarloPricer {
    private OptionParameters params;

    public MonteCarloPricer(OptionParameters params) {
        this.params = params;
    }

    public double call() {
        return calculatePrice(true);
    }

    public double put() {
        return calculatePrice(false);
    }

    public double calculatePrice(boolean isCallOption) {
        ExecutorService executor = Executors.newFixedThreadPool(params.getThreadPoolSize());
        List<Future<Double>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < params.getNumSimulations(); i++) {
                int simulationIndex = i;
                futures.add(executor.submit(() -> {
                    HestonModel model = new HestonModel(100, params.getTheta(), params.getInterestRate(),
                            params.getKappa(), params.getTheta(), params.getSigma(),
                            params.getRho(), 100, params.getMaturity());
                    double[] path = model.simulatePath();
                    double payoff = computePayoff(path, isCallOption);
                    System.out.println("Simulation " + simulationIndex + ": Path = " + Arrays.toString(path) + ", Payoff = " + payoff);
                    return payoff;
                }));
            }

            double sumPayoffs = 0;
            for (Future<Double> future : futures) {
                sumPayoffs += future.get();
            }

            System.out.println("Sum of all payoffs: " + sumPayoffs);
            double averagePayoff = sumPayoffs / params.getNumSimulations();
            double finalPrice = Math.exp(-params.getInterestRate() * params.getMaturity()) * averagePayoff;
            System.out.println("Final discounted price: " + finalPrice);
            return finalPrice;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        } finally {
            executor.shutdown();
        }
    }

    private double computePayoff(double[] path, boolean isCallOption) {
        double strike = params.getStrikePrice();
        double[] exerciseDates = params.getBermudanDates();

        if (isCallOption) {
            switch (params.getOptionType().toLowerCase()) {
                case "european":
                    return Math.max(path[path.length - 1] - strike, 0);
                case "american":
                    return longstaffSchwartz(path, strike);
                case "bermudan":
                    return bermudanPayoff(path, strike, exerciseDates);
                default:
                    throw new IllegalArgumentException("Unsupported option type: " + params.getOptionType());
            }
        } else {
            switch (params.getOptionType().toLowerCase()) {
                case "european":
                    return Math.max(strike - path[path.length - 1], 0);
                case "american":
                    return longstaffSchwartzPut(path, strike);
                case "bermudan":
                    return bermudanPayoffPut(path, strike, exerciseDates);
                default:
                    throw new IllegalArgumentException("Unsupported option type: " + params.getOptionType());
            }
        }
    }

    private double longstaffSchwartz(double[] path, double strike) {
        int n = path.length;
        double[] cashFlows = new double[n];
        cashFlows[n - 1] = Math.max(path[n - 1] - strike, 0);

        for (int t = n - 2; t >= 0; t--) {
            double discountedCashFlow = Math.exp(-params.getInterestRate() * (params.getMaturity() / n));
            double exerciseValue = Math.max(path[t] - strike, 0);

            if (exerciseValue > 0) {
                double continuationValue = discountedCashFlow * cashFlows[t + 1];
                cashFlows[t] = Math.max(exerciseValue, continuationValue);
            } else {
                cashFlows[t] = discountedCashFlow * cashFlows[t + 1];
            }
        }

        return cashFlows[0];
    }

    private double bermudanPayoff(double[] path, double strike, double[] exerciseDates) {
        int n = path.length;
        double[] cashFlows = new double[n];
        cashFlows[n - 1] = Math.max(path[n - 1] - strike, 0);

        for (int t = n - 2; t >= 0; t--) {
            double time = t * (params.getMaturity() / n);
            double discountedCashFlow = Math.exp(-params.getInterestRate() * (params.getMaturity() / n));
            double exerciseValue = Math.max(path[t] - strike, 0);

            if (exerciseValue > 0 && isExerciseDate(time, exerciseDates)) {
                double continuationValue = discountedCashFlow * cashFlows[t + 1];
                cashFlows[t] = Math.max(exerciseValue, continuationValue);
            } else {
                cashFlows[t] = discountedCashFlow * cashFlows[t + 1];
            }
        }

        return cashFlows[0];
    }

    private double longstaffSchwartzPut(double[] path, double strike) {
        int n = path.length;
        double[] cashFlows = new double[n];
        cashFlows[n - 1] = Math.max(strike - path[n - 1], 0);

        for (int t = n - 2; t >= 0; t--) {
            double discountedCashFlow = Math.exp(-params.getInterestRate() * (params.getMaturity() / n));
            double exerciseValue = Math.max(strike - path[t], 0);

            if (exerciseValue > 0) {
                double continuationValue = discountedCashFlow * cashFlows[t + 1];
                cashFlows[t] = Math.max(exerciseValue, continuationValue);
            } else {
                cashFlows[t] = discountedCashFlow * cashFlows[t + 1];
            }
        }

        return cashFlows[0];
    }

    private double bermudanPayoffPut(double[] path, double strike, double[] exerciseDates) {
        int n = path.length;
        double[] cashFlows = new double[n];
        cashFlows[n - 1] = Math.max(strike - path[n - 1], 0);

        for (int t = n - 2; t >= 0; t--) {
            double time = t * (params.getMaturity() / n);
            double discountedCashFlow = Math.exp(-params.getInterestRate() * (params.getMaturity() / n));
            double exerciseValue = Math.max(strike - path[t], 0);

            if (exerciseValue > 0 && isExerciseDate(time, exerciseDates)) {
                double continuationValue = discountedCashFlow * cashFlows[t + 1];
                cashFlows[t] = Math.max(exerciseValue, continuationValue);
            } else {
                cashFlows[t] = discountedCashFlow * cashFlows[t + 1];
            }
        }

        return cashFlows[0];
    }

    private boolean isExerciseDate(double time, double[] exerciseDates) {
        return DoubleStream.of(exerciseDates).anyMatch(date -> Math.abs(date - time) < 1e-4);
    }
}
