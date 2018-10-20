package com.upi.fuzzy;

/**
 * @author lutfi
 */

/**
 * Represents the inferential engine.
 */
public class FuzzyEngine {
    private LinguisticVariableCollection linguisticVariableCollection = new LinguisticVariableCollection();
    private String consequent = "";
    private FuzzyRuleCollection fuzzyRuleCollection = new FuzzyRuleCollection();
    private String filePath = "";

    private LinguisticVariable GetConsequent() throws Exception {
        return this.linguisticVariableCollection.Find(this.consequent);
    }

    private double Parse(String text) throws Exception {
        int counter = 0;
        int firstMatch = 0;

        if (!text.startsWith("(")) {
            String[] tokens = text.split(" ");
            return this.linguisticVariableCollection.Find(tokens[0]).Fuzzify(
                    tokens[2]);
        }

        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case '(':
                    counter++;
                    if (counter == 1)
                        firstMatch = i;
                    break;

                case ')':
                    counter--;
                    if ((counter == 0) && (i > 0)) {
                        String subString = text.substring(firstMatch + 1, i);
                        String subStringBrackets = text.substring(firstMatch, i + 1
                        );
                        int length = subStringBrackets.length();
                        text = text.replace(subStringBrackets,
                                String.valueOf(Parse(subString)));
                        i = i - (length - 1);
                    }
                    break;

                default:
                    break;
            }
        }

        return Evaluate(text);
    }

    private double Evaluate(String text) {
        String[] tokens = text.split(" ");
        String connective = "";
        double value = 0;

        for (int i = 0; i <= ((tokens.length / 2) + 1); i = i + 2) {
            double tokenValue = Double.parseDouble(tokens[i]);

            if (connective.equals("and")) {
                if (tokenValue < value)
                    value = tokenValue;
            } else if (connective.equals("or")) {
                if (tokenValue > value)
                    value = tokenValue;
            } else {
                value = tokenValue;
            }

            if ((i + 1) < tokens.length)
                connective = tokens[i + 1];
        }

        return value;
    }

    /**
     * @return A collection of linguistic variables.
     */
    public LinguisticVariableCollection getLinguisticVariableCollection() {
        return linguisticVariableCollection;
    }

    public void setLinguisticVariableCollection(
            LinguisticVariableCollection linguisticVariableCollection) {
        this.linguisticVariableCollection = linguisticVariableCollection;
    }

    public String getConsequent() {
        return consequent;
    }

    public void setConsequent(String consequent) {
        this.consequent = consequent;
    }

    public FuzzyRuleCollection getFuzzyRuleCollection() {
        return fuzzyRuleCollection;
    }

    public void setFuzzyRuleCollection(FuzzyRuleCollection fuzzyRuleCollection) {
        this.fuzzyRuleCollection = fuzzyRuleCollection;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Calculates the defuzzification value with the CoG (Center of Gravity)
     * @return The defuzzification value.
     * @throws Exception
     */
    public double Defuzzify() throws Exception {
        double numerator = 0;
        double denominator = 0;

        // Reset values
        for (MembershipFunction membershipFunction : this.GetConsequent()
                .getMembershipFunctionCollection()) {
            membershipFunction.setValue(0);
        }

        for (FuzzyRule fuzzyRule : this.fuzzyRuleCollection) {
            fuzzyRule.setValue(Parse(fuzzyRule.Conditions()));

            String[] tokens = fuzzyRule.getText().split(" ");
            MembershipFunction membershipFunction = this.GetConsequent()
                    .getMembershipFunctionCollection()
                    .Find(tokens[tokens.length - 1]);

            if (fuzzyRule.getValue() > membershipFunction.getValue())
                membershipFunction.setValue(fuzzyRule.getValue());
        }

        for (MembershipFunction membershipFunction : this.GetConsequent()
                .getMembershipFunctionCollection()) {
            numerator += membershipFunction.Centroid()
                    * membershipFunction.Area();
            denominator += membershipFunction.Area();
        }
        return numerator / denominator;
    }
}
