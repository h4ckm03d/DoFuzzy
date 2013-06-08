package com.upi.fuzzy;

/// <summary>
/// Represents a linguistic variable.
/// </summary>
public class LinguisticVariable {
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MembershipFunctionCollection getMembershipFunctionCollection() {
		return membershipFunctionCollection;
	}

	public void setMembershipFunctionCollection(
			MembershipFunctionCollection membershipFunctionCollection) {
		this.membershipFunctionCollection = membershipFunctionCollection;
	}

	public double getInputValue() {
		return inputValue;
	}

	public void setInputValue(double inputValue) {
		this.inputValue = inputValue;
	}

	private MembershipFunctionCollection membershipFunctionCollection = new MembershipFunctionCollection();
	private double inputValue = 0;

	// / <summary>
	// / Default constructor.
	// / </summary>
	public LinguisticVariable() {
	}

	// / <param name="name">The name that identificates the linguistic
	// variable.</param>
	public LinguisticVariable(String name) {
		this.setName(name);
	}

	// / <param name="name">The name that identificates the linguistic
	// variable.</param>
	// / <param name="membershipFunctionCollection">A membership functions
	// collection for the lingusitic variable.</param>
	public LinguisticVariable(String name,
			MembershipFunctionCollection membershipFunctionCollection) {
		this.setName(name);
		this.setMembershipFunctionCollection(membershipFunctionCollection);
	}

	// / <summary>
	// / Implements the fuzzification of the linguistic variable.
	// / </summary>
	// / <param name="membershipFunctionName">The membership function for which
	// fuzzify the variable.</param>
	// / <returns>The degree of membership.</returns>
	public double Fuzzify(String membershipFunctionName) throws Exception {
		MembershipFunction membershipFunction = this
				.getMembershipFunctionCollection().Find(membershipFunctionName);

		if ((membershipFunction.getX0() <= this.getInputValue())
				&& (this.getInputValue() < membershipFunction.getX1()))
			return (this.getInputValue() - membershipFunction.getX0())
					/ (membershipFunction.getX1() - membershipFunction.getX0());
		else if ((membershipFunction.getX1() <= this.getInputValue())
				&& (this.getInputValue() <= membershipFunction.getX2()))
			return 1;
		else if ((membershipFunction.getX2() < this.getInputValue())
				&& (this.getInputValue() <= membershipFunction.getX3()))
			return (membershipFunction.getX3() - this.getInputValue())
					/ (membershipFunction.getX3() - membershipFunction.getX2());
		else
			return 0;
	}

	// / <summary>
	// / Returns the minimum value of the linguistic variable.
	// / </summary>
	// / <returns>The minimum value of the linguistic variable.</returns>
	public double MinValue() {
		double minValue = this.membershipFunctionCollection.get(0).getX0();

		for (int i = 1; i < this.membershipFunctionCollection.size(); i++) {
			if (this.membershipFunctionCollection.get(i).getX0() < minValue)
				minValue = this.membershipFunctionCollection.get(i).getX0();
		}

		return minValue;
	}

	// / <summary>
	// / Returns the maximum value of the linguistic variable.
	// / </summary>
	// / <returns>The maximum value of the linguistic variable.</returns>
	public double MaxValue() {
		double maxValue = this.membershipFunctionCollection.get(0).getX3();

		for (int i = 1; i < this.membershipFunctionCollection.size(); i++) {
			if (this.membershipFunctionCollection.get(i).getX3() > maxValue)
				maxValue = this.membershipFunctionCollection.get(i).getX3();
		}

		return maxValue;
	}

	// / <summary>
	// / Returns the difference between MaxValue() and MinValue().
	// / </summary>
	// / <returns>The difference between MaxValue() and MinValue().</returns>
	public double Range() {
		return this.MaxValue() - this.MinValue();
	}
}
