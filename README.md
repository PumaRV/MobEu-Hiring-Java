# Coding Challenge for Digital Consultancy Company
This project was created as part of a hiring process for a Digital Consultancy Company. It calculates the optimal 
combination of items to put in a package, maximizing the value of the contents and minimizing the weight. This is 
a variation on the 0-1 Knapsack problem (https://en.wikipedia.org/wiki/Knapsack_problem)

## Prerequisites
- Java 8
- Maven 3.6.1 

## Configuration
Application properties are listed in Application.properties.

- input.file.path:      Absolute path to the input File
- weight.max = 100:     Maximum weight allowed for any single item
- capacity.max = 100:   Maximum capacity allowed for any single package
- value.max = 100:      Maximum value allowed for any single item 
- items.max = 15:       Maximum amount of items allowed for any single package

## Executing
You can follow these steps: 

- Navigate to the root of the project 
run
```
$ mvn exec:java -Dexec.mainClass="com.mobiquityinc.PackageApplication"
```

- Results will be printed out to console. 


 
