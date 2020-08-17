package co.branch.lendingoracle.service;

import co.branch.lendingoracle.model.LoanApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LendingService {

    private InputParser inputParser;
    private OutputWriter outputWriter;

    public LendingService() {
        this.inputParser = new InputParser();
        this.outputWriter = new OutputWriter();
    }

    public void processRequest(String inputFilePath, String outputPath, int startingCash, int maxLoanCount) throws Exception {
        List<LoanApplication> loanApplications = this.inputParser.readFileInput(inputFilePath);

        if(loanApplications == null || loanApplications.isEmpty()) {
            throw new Exception("Could not read input or empty input");
        }

        LoanProcessor processor = LoanProcessor.getProcessor(startingCash, maxLoanCount, loanApplications);
        Set<Integer> acceptedApplications =  processor.processRequest();

        List<Integer> accepted = new ArrayList<>(acceptedApplications);
        Collections.sort(accepted);
        this.outputWriter.writeOutput(accepted, outputPath);
    }


}
