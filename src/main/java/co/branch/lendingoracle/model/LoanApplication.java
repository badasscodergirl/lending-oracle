package co.branch.lendingoracle.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.*;

public class LoanApplication {
    @SerializedName(value = "application_id")
    public int id;

    @SerializedName(value = "customer_id")
    public int customerId;

    public double principal;
    public  double fee;

    @SerializedName(value = "disbursement_date")
    public Date disbursementDate;

    @SerializedName(value = "repayment_fraction")
    public double repaymentFraction;

    @SerializedName(value = "installment_amount")
    public double installMentAmount;

    @SerializedName(value = "repaid_amount")
    public double repaidAmount;

    public List<Payment> repayments;

    public Date finalRepayDate;

    @Override
    public String toString() {
        /*return "ApplicationId="+this.id+": CustomerId="+this.customerId
                +": Date="+formatDate(disbursementDate)+": Amount="+principal+": EndDate="+formatDate(finalRepaymentDate())+"\n";*/
        return this.id+":::"+this.principal+"\n";
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public Date finalRepaymentDate() {
        if(finalRepayDate != null)
            return finalRepayDate;

        TreeMap<Date, Double> repayMap = new TreeMap<>();
        for(Payment payment: repayments) {
            repayMap.put(payment.date, repayMap.getOrDefault(payment.date, 0.0)+payment.amount);
        }
        finalRepayDate = repayMap.lastEntry().getKey();
        return finalRepayDate;
    }

    public boolean isDataValid() {
        if(principal <= 0 || disbursementDate == null || repayments == null) return false;
        return true;
    }

    public boolean isDefaulted() {
        if(repaymentFraction < 1.0) return true;
        long time = finalRepaymentDate().getTime() - disbursementDate.getTime();
        long days = time/(1000*60*60*24);
        return days > 90;
    }
}
