package com.upi.fuzzy;

public class FuzzyRule {

	private String text = "";
	private double value = 0;

	private String Validate(String text) {
		try {
			int count = 0;
			int position = text.indexOf("(");
			String[] tokens = text.replace("(", "").replace(")", "").split(" ");

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

			if (tokens[0] != "IF")
				throw new Exception("'IF' not found: " + text);

			if (tokens[tokens.length - 4] != "THEN")
				throw new Exception("'THEN' not found: " + text);

			if (tokens[tokens.length - 2] != "IS")
				throw new Exception("'IS' not found: " + text);

			for (int i = 2; i < (tokens.length - 5); i = i + 2) {
				if ((tokens[i] != "IS") && (tokens[i] != "AND")
						&& (tokens[i] != "OR"))
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

	// / <summary>
	// / Default constructor.
	// / </summary>
	public FuzzyRule() {
	}

	// / <param name="text">The text of the rule.</param>
	public FuzzyRule(String text) {
		this.text = text;
	}

	// / <summary>
	// / Returns the conditions of the rule.
	// / The part of the rule between IF and THEN.
	// / </summary>
	// / <returns>The conditions of the rule.</returns>
	public String Conditions() {
        int a = this.text.indexOf("IF ");
        int b = this.text.indexOf(" THEN");
        String test = this.text.substring(this.text.indexOf("IF ") + 3,
                this.text.indexOf(" THEN"));
		return this.text.substring(this.text.indexOf("IF ") + 3,
				this.text.indexOf(" THEN"));
	}
}
