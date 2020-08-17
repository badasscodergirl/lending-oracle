package co.branch.lendingoracle.service;

import co.branch.lendingoracle.model.LoanApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InputParser {

    public List<LoanApplication> readFileInput(String filePath) {
        File file = new File(filePath);
        Type loanApplicationListType = new TypeToken<ArrayList<LoanApplication>>(){}.getType();
        Gson gson = new Gson();

        try (InputStream is = new FileInputStream(file)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return gson.fromJson(br, loanApplicationListType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
