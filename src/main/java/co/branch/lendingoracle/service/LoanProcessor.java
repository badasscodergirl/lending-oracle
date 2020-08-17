package co.branch.lendingoracle.service;

import co.branch.lendingoracle.model.LoanApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoanProcessor {

    private int totalCash;
    private int maxActiveLoans;
    private List<LoanApplication> loanApplications;
    private TreeMap<Date, List<LoanApplication>> byDisbursementDate;
    private Set<Integer> activeLoanCustomers;
    private List<Integer> defaultedCustomers;
    private LinkedList<LoanApplication> activeLoans;
    private static Date yearEnd;


    private Set<Integer> acceptedApplications;

    private LoanProcessor() {}

    public static LoanProcessor getProcessor(int startingCash, int maxActiveLoans, List<LoanApplication> loanApplications) {
        LoanProcessor instance = new LoanProcessor();
        instance.totalCash = startingCash;
        instance.maxActiveLoans = maxActiveLoans;
        instance.loanApplications = loanApplications;
        instance.defaultedCustomers = new LinkedList<>();
        instance.activeLoanCustomers = new HashSet<>();
        instance.acceptedApplications = new HashSet<>();
        instance.byDisbursementDate = new TreeMap<>();
        instance.activeLoans = new LinkedList<>();

        try {
            yearEnd = parseDate("31-12-2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return instance;
    }

    /**
     * Filter out the defaulted loans and create a map of disbursed date with list of applications
     */
    public Set<Integer> processRequest() {
        filterData();

        for(Date date: byDisbursementDate.keySet()) {
            processApplicationsForDay(date, byDisbursementDate.get(date));
        }

        clearClosed(yearEnd);
        return acceptedApplications;
    }


    /**
     * Clear out loans before this date and adjust the balancce
     */
    private void clearClosed(Date date) {
        while(!activeLoans.isEmpty() && date.compareTo(activeLoans.peekFirst().finalRepaymentDate()) >= 0) {
            LoanApplication repaidLoan = activeLoans.removeFirst();
            totalCash += repaidLoan.repaidAmount;
            activeLoanCustomers.remove(repaidLoan.customerId);
        }
    }

    /**
     * 1. Give the loan whose repayment date is nearest
     *        2. Give the smallest loan first
     *        3. Give loan with highest fee
     */
    private void processApplicationsForDay(Date date, List<LoanApplication> applications) {
        clearClosed(date);

        if(activeLoans.size() < maxActiveLoans) {

            sortSameDayApplications(applications);
            for(LoanApplication application: applications) {

                /* 1. Check if customer has active loan.
                   2. Check if we have cash balance */
                if(!activeLoanCustomers.contains(application.customerId) && canDisburseLoan(application.principal)) {
                    acceptedApplications.add(application.id);
                    activeLoanCustomers.add(application.customerId);
                    totalCash -= application.principal;
                    activeLoans.add(application);

                }
                if(activeLoans.size() == maxActiveLoans) break;
            }
        }
    }

    private boolean canDisburseLoan(double principal) {
        return totalCash - principal > 0;
    }

    /**
     * Sorting applications of same day on final repayment date, fee and principal amount
     */
    private void sortSameDayApplications(List<LoanApplication> applications) {
        applications.sort((a, b) -> {
            if(a.finalRepaymentDate().equals(a.finalRepaymentDate())) {
                if(b.fee == a.fee) return (int) (a.principal - b.principal);
                return (int)(b.fee - a.fee);
            }
            return a.finalRepaymentDate().compareTo(b.finalRepaymentDate());
        });
    }

    /**
     * Filtering out defaulted customers
     * Filtering out loans whose repayment dates are > year end
     */
    private void filterData() {
        byDisbursementDate = new TreeMap<>();

        loanApplications.forEach(la -> {
            if(la.isDefaulted()) {
                defaultedCustomers.add(la.customerId);
            }
        });

        loanApplications.forEach(la -> {
            if(la.isDataValid() && !defaultedCustomers.contains(la.customerId) && la.finalRepaymentDate().compareTo(yearEnd) < 0) {
                if(!byDisbursementDate.containsKey(la.disbursementDate)) {
                    byDisbursementDate.put(la.disbursementDate, new ArrayList<>());
                }
                byDisbursementDate.get(la.disbursementDate).add(la);
            }
        });
    }

    private static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(date);
    }
}
