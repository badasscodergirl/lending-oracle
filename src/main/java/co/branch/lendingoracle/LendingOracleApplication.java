package co.branch.lendingoracle;

import co.branch.lendingoracle.service.LendingService;

/**
 * Hello world!
 *
 */
public class LendingOracleApplication
{
    LendingService lendingService;

    public static void main(String[] args ) {

        System.out.println( "Processing loan applications.. " );

        try {
            if(args.length < 4 || args.length > 4) {
                System.out.println("Invalid arguments.");
                return;
            }

            String inputFilePath = getString("inputFilePath", args[0]);
            String outputPath = getString("outputPath", args[1]);
            int startingCash = getInteger("startingCash", args[2]);
            int maxActiveLoans = getInteger("maxActiveLoans", args[3]);

            LendingOracleApplication lendingOracleApplication  = new LendingOracleApplication();
            lendingOracleApplication.init();

            lendingOracleApplication.lendingService.processRequest(inputFilePath, outputPath,startingCash, maxActiveLoans);

            System.out.println("Done processing loan applications.");
        } catch (Exception e) {
            System.out.println("Failed with an exception");
            e.printStackTrace();
        }
    }

    private void init() {
        lendingService = new LendingService();
    }

    private static String getString(String name, String value) throws IllegalArgumentException {
        return sanitizeValue(name, value);
    }

    private static int getInteger(String name, String value) throws IllegalArgumentException {
        String sanitized = sanitizeValue(name, value);
        return Integer.parseInt(sanitized);
    }

    private static String sanitizeValue(String name, String value) throws IllegalArgumentException {
        String errorMessage = "Invalid value received for "+name;

        if(value == null)
            throw new IllegalArgumentException(errorMessage);

        value = value.trim();

        if(value.isEmpty())
            throw new IllegalArgumentException(errorMessage);

        return value;
    }



}
