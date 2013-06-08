package com.upi.fuzzy;

import java.util.ArrayList;
/// <summary>
/// Represents a collection of rules.
/// </summary>

public class LinguisticVariableCollection extends ArrayList<LinguisticVariable> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2441276112206205549L;

	// / <summary>
	// / Finds a linguistic variable in a collection.
	// / </summary>
	// / <param name="linguisticVariableName">Linguistic variable name.</param>
	// / <returns>The linguistic variable, if founded.</returns>
	public LinguisticVariable Find(String linguisticVariableName) throws Exception
			{
		LinguisticVariable linguisticVariable = null;

		for (LinguisticVariable variable : this) {
			if (variable.getName().equals(linguisticVariableName)) {
				linguisticVariable = variable;
				break;
			}
		}

		if (linguisticVariable == null)
			throw new Exception("com.upi.fuzzy.LinguisticVariable not found: "
					+ linguisticVariableName);
		else
			return linguisticVariable;
	}
}
