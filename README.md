# Mendota_Ice

### About
Ice is a program that performs calculations on a data set containing information about winters from 1855-2017.

### Input
The command line arguments for Ice should be formatted as: FLAG [arg1 arg2]
FLAG can be an integer value ranging from 1-900. Arg1 and arg2 are optional and will only be used for certain FLAGs.

## Definitions
### FLAG == 100:
#### Format
  -java Ice 100
#### Output
  -Prints the year and resulting number of days with ice for each winter in the data set.
  
### FLAG == 200:
  #### Format
  -java Ice 200
  #### Output
  -Prints n (number of winters in data set), the mean number of days with ice, and the standard deviation.
  
### FLAG == 300:
  #### Format
  -java Ice 300 B0 B1
  #### Input
  -B0: Beta 0 value.
  -B1: Beta 1 value.
  #### Output
  -Prints the mean squared error (MSE) for given Beta0 and Beta1 values.
  
### FLAG == 400:
  #### Format
  -java Ice 400 B0 B1
  #### Input
  -B0: Beta 0 value.
  -B1: Beta 1 value.
  #### Output
  -Prints corresponding gradient to given Beta0 and Beta1 values.
  
### FLAG == 500:
  #### Format
  -java Ice 500 N T
  #### Input
  -N: N scalar value used in formula.
  -T: Number of iterations to calculate.
  #### Output
  -Prints corresponding gradient with calculated Beta values over T iterations.
  
### FLAG == 600:
  #### Format
  -java Ice 600
  #### Output
  -Prints Beta0, Beta1, and MSE values from a closed-form solution for ordinary least squared in one dimmension.
  
### FLAG == 700:
  #### Format
  -java 700 y
  #### Input
  -y: Year to estimate number of days of ice with.
  #### Output
  -Prints estimated number of days of ice for a given year based on data set.
  
### FLAG == 800:
  #### Format
  -java 800 N T
  #### Input
  -N: N scalar value used in formula.
  -T: Number of iterations to calculate.
  #### Output
  -Prints B0, B1, and MSE with Xi substitution using stdx over T iterations.
  
### FLAG == 900:
  #### Format
  -java 900 N T
  #### Input
  -N: N scalar value used in formula.
  -T: Number of iterations to calculate.
  #### Output
  -Prints B0, B1, and MSE based on estimate from random entry in data set.
