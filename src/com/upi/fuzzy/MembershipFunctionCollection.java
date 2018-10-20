package com.upi.fuzzy;

import java.util.ArrayList;

/**
 * Represents a collection of membership functions.
 */
public class MembershipFunctionCollection extends ArrayList<MembershipFunction> {

    /**
     * Finds a membership function in a collection.
     * @param membershipFunctionName Membership function name.
     * @return The membership function, if founded.
     * @throws Exception
     */
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
			throw new Exception("MembershipFunction not found: "
					+ membershipFunctionName);
		else
			return membershipFunction;
	}
}