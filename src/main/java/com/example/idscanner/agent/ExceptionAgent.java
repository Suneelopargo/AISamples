package com.example.idscanner.agent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExceptionAgent {

    public List<String> explain(List<String> errors) {

        List<String> explanations = new ArrayList<>();

        for (String error : errors) {

            switch (error) {

                case "ERROR_MISSING_DOCUMENTS":
                    explanations.add("❌ Documents missing. Please upload required documents.");
                    break;

                case "ERROR_INVALID_BILLING_CODE":
                    explanations.add("❌ Invalid billing code as per insurer rules.");
                    break;

                case "ERROR_POLICY_MISMATCH":
                    explanations.add("❌ Policy number is invalid or inactive.");
                    break;
            }
        }

        return explanations;
    }
}

