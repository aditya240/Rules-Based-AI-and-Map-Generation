package s3.ai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rules {
    private List<String> conditions;
    private String result;

    public Rules(List<String> conditions, String result) {
        this.conditions = conditions;
        this.result = result;
    }

    public List<String> getConditions() {
        return this.conditions;
    }

    public String getResult() {
        return this.result;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

class RuleParser {
    private List<Rules> rules;

    public RuleParser() {
        this.rules = new ArrayList<>();
    }

    public List<Rules> getRules() {
        return this.rules;
    }

    public void loadRules(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    if (line.charAt(0) != '#') {
                        String[] parts = line.split(" :- ");
                        String result = parts[0];
                        List<String> conditions = splitConditions(parts[1]);
                        rules.add(new Rules(conditions, result));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> splitConditions(String conditionStr) {
        List<String> conditions = new ArrayList<>();
        int balance = 0;
        StringBuilder condition = new StringBuilder();

        for (char ch : conditionStr.toCharArray()) {
            switch(ch) {
                case '(':
                    balance++;
                    condition.append(ch);
                    break;
                case ')':
                    balance--;
                    condition.append(ch);
                    break;
                case ',':
                    if (balance == 0) {
                        conditions.add(condition.toString().trim());
                        condition = new StringBuilder();
                    } else {
                        condition.append(ch);
                    }
                    break;
                default:
                    condition.append(ch);
            }
        }
        if (condition.length() > 0) {
            conditions.add(condition.toString().trim());
        }

        return conditions;
    }
}

