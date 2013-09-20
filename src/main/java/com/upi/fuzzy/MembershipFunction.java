package com.upi.fuzzy;

public class MembershipFunction {

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getX3() {
		return x3;
	}

	public void setX3(double x3) {
		this.x3 = x3;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	private double x0 = 0;
	private double x1 = 0;
	private double x2 = 0;
	private double x3 = 0;
	private double value = 0;

    /**
     * Default constructor
     */
	public MembershipFunction() {
	}

    /**
     * The name that identificates the membership function
     * @param name
     */
	public MembershipFunction(String name) {
		this.setName(name);
	}

    /**
     *
     * @param name The name that identificates the linguistic variable
     * @param x0 The value of the (x0, 0) point.
     * @param x1 The value of the (x1, 1) point.
     * @param x2 The value of the (x2, 2) point.
     * @param x3 The value of the (x3, 3) point.
     */
	public MembershipFunction(String name, double x0, double x1, double x2,
			double x3) {
		this.setName(name);
		this.setX0(x0);
		this.setX1(x1);
		this.setX2(x2);
		this.setX3(x3);
	}

    /**
     * Calculate the centroid of a trapezoidal membership function.
     * @return The value of centroid.
     */
	public double Centroid() {
		double a = this.x2 - this.x1;
		double b = this.x3 - this.x0;
		double c = this.x1 - this.x0;

		return ((2 * a * c) + (a * a) + (c * b) + (a * b) + (b * b))
				/ (3 * (a + b)) + this.x0;
	}

    /**
     * Calculate the area of a trapezoidal membership function.
     * @return The value of area.
     */
	public double Area() {
		double a = this.Centroid() - this.x0;
		double b = this.x3 - this.x0;

		return (this.value * (b + (b - (a * this.value)))) / 2;
	}
}
