package com.upi.fuzzy;

public class FuzzyRule {

	private String text = "";
	private double value = 0;

	private String Validate(String text) {
		try {
			int count = 0;
			int position = text.indexOf("(");
			String[] tokens = text.toLowerCase().replace("(", "").replace(")", "").split(" ");

			while (position >= 0) {
				count++;
				position = text.indexOf("(", position + 1);
			}

			position = text.indexOf(")");
			while (position >= 0) {
				count--;
				position = text.indexOf(")", position + 1);
			}

			if (count > 0)
				throw new Exception("missing right parenthesis: " + text);
			else if (count < 0)
				throw new Exception("missing left parenthesis: " + text);

			if (tokens[0] != "if")
				throw new Exception("'if' not found: " + text);

			if (tokens[tokens.length - 4] != "then")
				throw new Exception("'then' not found: " + text);

			if (tokens[tokens.length - 2] != "is")
				throw new Exception("'is' not found: " + text);

			for (int i = 2; i < (tokens.length - 5); i = i + 2) {
				if ((tokens[i] != "is") && (tokens[i] != "and")
						&& (tokens[i] != "or"))
					throw new Exception("Syntax error: " + tokens[i]);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public FuzzyRule() {
	}

	public FuzzyRule(String text) {
		this.text = text;
	}

    /**
     * Return the conditions of the rule
     * The part of the rule between IF and then.
     * @return The conditions of the rule.
     */
	public String Conditions() {
		return this.text.toLowerCase().substring(this.text.indexOf("if ") + 3,
				this.text.indexOf(" then"));
	}
}
