# Branch Lending Oracle Application

## Pre-requisites

 1. Java JDK 1.8
 2. Apache Maven 3.6.3
 3. Gson 2.8.6

## Steps to build and run
1. Go to the project directory
2.  Compile and create a jar file
`mvn clean compile assembly:single`
3. Run the jar file with input arguments
  `java -jar target/lending-oracle-1.0-jar-with-dependencies.jar <InputfilePath> <OutuptFilePath> <N> <K>`

 ## Input Format
 **Arg[0]** - Input filePath - String - Absoute path of input file
 **Arg[1]** - Output FilePath - String - Absoute path of input file
 **Arg[2]** - N - Numeric - Starting Cash
 **Arg[3]** - K - Numeric - Max Active Loan Count

**Sample Command:**

    java -jar target/lending-oracle-1.0-jar-with-dependencies.jar /Users/shaziyasyeda/Projects/assignment/branch/lending-oracle/src/main/resources/applications.json /Users/shaziyasyeda/Projects/assignment/branch/lending-oracle/src/main/output/output.txt 50000 1000


If output file doesn't exist in the given directory the program will create one. Or if it already exists, it will delete the existing and create new.

If input arguments are empty or if input file is not found or if json is invalid, program will fail with an exception.

