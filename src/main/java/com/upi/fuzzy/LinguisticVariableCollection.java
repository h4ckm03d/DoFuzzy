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

    /**
     * Finds a linguistic variable in a collection.
     * @param linguisticVariableName Linguistic variable name.
     * @return The linguistic variable, if founded.
     * @throws Exception
     */
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
			throw new Exception("LinguisticVariable not found: "
					+ linguisticVariableName);
		else
			return linguisticVariable;
	}
}
