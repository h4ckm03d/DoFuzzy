package com.upi.fuzzy;

import java.util.ArrayList;

/// <summary>
/// Represents a collection of membership functions.
/// </summary>
public class MembershipFunctionCollection extends ArrayList<MembershipFunction> {
	// / <summary>
	// / Finds a membership function in a collection.
	// / </summary>
	// / <param name="membershipFunctionName">Membership function name.</param>
	// / <returns>The membership function, if founded.</returns>
	public MembershipFunction Find(String membershipFunctionName)
			throws Exception {
		MembershipFunction membershipFunction = null;

		for (MembershipFunction function : this) {
			if (function.getName().equals(membershipFunctionName)) {
				membershipFunction = function;
				break;
			}
		}

		if (membershipFunction == null)
			throw new Exception("com.upi.fuzzy.MembershipFunction not found: "
					+ membershipFunctionName);
		else
			return membershipFunction;
	}
}