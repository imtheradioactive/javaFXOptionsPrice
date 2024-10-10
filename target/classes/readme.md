# Option Pricing Application

## Overview
The Option Pricing Application is designed for calculating the prices of European, American, and Bermudan options using Monte Carlo simulations. It allows users to input various parameters like strike price, maturity, interest rate, and more to simulate the price of financial options. The app provides an intuitive interface using JavaFX, making it easy to use for anyone familiar with options trading.

## Key Features
- **Supports Multiple Option Types**: Users can choose between European, American, or Bermudan options. Each type is priced using its respective model, including handling exercise dates for Bermudan options.
- **Monte Carlo Simulations**: The core of the pricing model uses Monte Carlo methods to simulate the price paths of the underlying asset. This approach allows for a flexible and accurate estimation of option prices, even for complex payoffs.
- **Heston Model Integration**: Uses the Heston Model for simulating paths of stock prices, considering both stochastic volatility and the correlation between asset price and variance. This model is more advanced compared to simpler models like Black-Scholes.
- **Multithreading**: The application is built to handle a large number of simulations efficiently by using multithreading. A thread pool is used to run simulations concurrently, significantly reducing the time needed for computations. The number of threads can be adjusted based on the user's system capabilities.

## Code Structure
- **Main**: The entry point that launches the JavaFX application and sets up the UI.
- **OptionPricingApp**: Manages the user interface, takes input parameters, and triggers the pricing calculation.
- **MonteCarloPricer**: Handles the core logic for simulating option prices. It submits each simulation as a task to a thread pool, using concurrent processing for faster results.
- **HestonModel**: Simulates the path of stock prices based on the Heston Model, capturing both price and variance over time.
- **OptionParameters**: Stores user inputs like strike price, maturity, volatility, and other parameters for the pricing calculations.

## Usage
1. **Input Parameters**: Users enter details like the strike price, option type, maturity, and other relevant data.
2. **Select Option Type**: Choose between “Call” or “Put” and the option style (European, American, or Bermudan).
3. **Calculate Price**: Click the calculate button, and the app uses Monte Carlo simulations to generate the price.
4. **View Results**: The calculated price appears in the interface, giving users an estimate of the option's fair value.

## Notes
- **Concurrency**: By using `ExecutorService` for managing threads, the app can run thousands of simulations in parallel, making it suitable for more intensive calculations.
- **JavaFX**: The app’s user interface is built with JavaFX, providing a clean and responsive user experience.
- **Flexibility**: Users can customize the number of simulations and the thread pool size, allowing them to balance between speed and accuracy based on their needs.

## Start the application
 **Prerequisites**: 
   - Java SDK.
   -Any Java Supporting IDE
 
 
 **Instructions to start app**:
   - Run Maven build with goal as run to start project
   - Run Maven Test to run all the tests
	
   

