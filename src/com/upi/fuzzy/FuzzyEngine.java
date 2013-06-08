package com.upi.fuzzy;

/**
 * @author lutfi
 */
// / <summary>
// / Represents the inferential engine.
// / </summary>
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

            if (connective.equals("AND")) {
                if (tokenValue < value)
                    value = tokenValue;
            } else if (connective.equals("OR")) {
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

    // void ReadVariable(XmlNode xmlNode)
    // {
    // com.upi.fuzzy.LinguisticVariable linguisticVariable =
    // this.linguisticVariableCollection.Find(xmlNode.Attributes["NAME"].InnerText);
    //
    // foreach (XmlNode termNode in xmlNode.ChildNodes)
    // {
    // String[] points = termNode.Attributes["POINTS"].InnerText.Split();
    // linguisticVariable.com.upi.fuzzy.MembershipFunctionCollection.Add(new
    // com.upi.fuzzy.MembershipFunction(
    // termNode.Attributes["NAME"].InnerText,
    // Convert.ToDouble(points[0]),
    // Convert.ToDouble(points[1]),
    // Convert.ToDouble(points[2]),
    // Convert.ToDouble(points[3])));
    // }
    // }

    // / <summary>
    // / A collection of linguistic variables.
    // / </summary>
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

    // / <summary>
    // / Calculates the defuzzification value with the CoG (Center of Gravity)
    // technique.
    // / </summary>
    // / <returns>The defuzzification value.</returns>
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
            numerator += membershipFunction.Centorid()
                    * membershipFunction.Area();
            denominator += membershipFunction.Area();
        }

        return numerator / denominator;
    }

    // / <summary>
    // / Sets the FilePath property and saves the project into a FCL-like XML
    // file.
    // / </summary>
    // / <param name="path">Path of the destination document.</param>
    // public void Save(String path)
    // {
    // this.setFilePath(path);
    // this.Save();
    // }

    // / <summary>
    // / Saves the project into a FCL-like XML file.
    // / </summary>
    // public void Save()
    // {
    // if (this.filePath == "")
    // throw new Exception("FilePath not set");
    //
    // int i = 0;
    // XmlTextWriter xmlTextWriter = new XmlTextWriter(this.filePath,
    // Encoding.UTF8);
    // xmlTextWriter.Formatting = Formatting.Indented;
    // xmlTextWriter.WriteStartDocument(true);
    // xmlTextWriter.WriteStartElement("FUNCTION_BLOCK");
    //
    // foreach (com.upi.fuzzy.LinguisticVariable linguisticVariable in
    // this.linguisticVariableCollection)
    // {
    // if (linguisticVariable.Name == this.consequent)
    // xmlTextWriter.WriteStartElement("VAR_OUTPUT");
    // else
    // xmlTextWriter.WriteStartElement("VAR_INPUT");
    //
    // xmlTextWriter.WriteAttributeString("NAME", linguisticVariable.Name);
    // xmlTextWriter.WriteAttributeString("TYPE", "REAL");
    // xmlTextWriter.WriteAttributeString("RANGE",
    // linguisticVariable.MinValue().ToString() + " " +
    // linguisticVariable.MaxValue().ToString());
    // xmlTextWriter.WriteEndElement();
    // }
    //
    // for (com.upi.fuzzy.LinguisticVariable linguisticVariable :
    // this.getLinguisticVariableCollection())
    // {
    // if (linguisticVariable.getName() == this.consequent)
    // {
    // xmlTextWriter.WriteStartElement("DEFUZZIFY");
    // xmlTextWriter.WriteAttributeString("METHOD", "CoG");
    // xmlTextWriter.WriteAttributeString("ACCU", "MAX");
    // }
    // else
    // xmlTextWriter.WriteStartElement("FUZZIFY");
    //
    // xmlTextWriter.WriteAttributeString("NAME", linguisticVariable.getName());
    //
    // for (com.upi.fuzzy.MembershipFunction membershipFunction :
    // linguisticVariable.getMembershipFunctionCollection())
    // {
    // xmlTextWriter.WriteStartElement("TERM");
    // xmlTextWriter.WriteAttributeString("NAME", membershipFunction.Name);
    // xmlTextWriter.WriteAttributeString("POINTS",
    // membershipFunction.X0 + " " +
    // membershipFunction.X1 + " " +
    // membershipFunction.X2 + " " +
    // membershipFunction.X3);
    // xmlTextWriter.WriteEndElement();
    // }
    //
    // xmlTextWriter.WriteEndElement();
    // }
    //
    // xmlTextWriter.WriteStartElement("RULEBLOCK");
    // xmlTextWriter.WriteAttributeString("AND", "MIN");
    // xmlTextWriter.WriteAttributeString("OR", "MAX");
    //
    // for (com.upi.fuzzy.FuzzyRule fuzzyRule : this.fuzzyRuleCollection)
    // {
    // i++;
    // xmlTextWriter.WriteStartElement("RULE");
    // xmlTextWriter.WriteAttributeString("NUMBER", i);
    // xmlTextWriter.WriteAttributeString("TEXT", fuzzyRule.getText());
    // xmlTextWriter.WriteEndElement();
    // }
    //
    // xmlTextWriter.WriteEndElement();
    //
    // xmlTextWriter.WriteEndElement();
    // xmlTextWriter.WriteEndDocument();
    // xmlTextWriter.Close();
    // }

    // / <summary>
    // / Sets the FilePath property and loads a project from a FCL-like XML
    // file.
    // / </summary>
    // / <param name="path">Path of the source file.</param>
    // public void Load(String path)
    // {
    // this.setFilePath(path);
    // this.Load();
    // }

    // / <summary>
    // / Loads a project from a FCL-like XML file.
    // / </summary>
    // public void Load()
    // {
    // if (this.filePath == "")
    // throw new Exception("FilePath not set");
    //
    // XmlDocument xmlDocument = new XmlDocument();
    // xmlDocument.Load(this.filePath);
    //
    // for (XmlNode xmlNode in xmlDocument.GetElementsByTagName("VAR_INPUT"))
    // {
    // this.com.upi.fuzzy.LinguisticVariableCollection.Add(new
    // com.upi.fuzzy.LinguisticVariable(xmlNode.Attributes["NAME"].InnerText));
    // }
    //
    // this.consequent =
    // xmlDocument.GetElementsByTagName("VAR_OUTPUT")[0].Attributes["NAME"].InnerText;
    // this.com.upi.fuzzy.LinguisticVariableCollection.Add(new
    // com.upi.fuzzy.LinguisticVariable(this.consequent));
    //
    // foreach (XmlNode xmlNode in xmlDocument.GetElementsByTagName("FUZZIFY"))
    // {
    // ReadVariable(xmlNode);
    // }
    //
    // ReadVariable(xmlDocument.GetElementsByTagName("DEFUZZIFY")[0]);
    //
    // foreach (XmlNode xmlNode in xmlDocument.GetElementsByTagName("RULE"))
    // {
    // this.fuzzyRuleCollection.Add(new
    // com.upi.fuzzy.FuzzyRule(xmlNode.Attributes["TEXT"].InnerText));
    // }
    // }
}
