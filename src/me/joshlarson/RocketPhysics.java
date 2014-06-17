package me.joshlarson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RocketPhysics {
	
	private static final double GRAVITY = 9.8066;
	private static final double METERS_PER_FOOT = 3.28084;
	private static final double NEWTONS_PER_POUND = 0.224808943;
	
	public static void main(String [] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		System.out.printf("Data input file (blank for System.in): ");
		String inputFile = scan.nextLine();
		if (inputFile.length() > 0) {
			scan.close();
			scan = new Scanner(new File(inputFile));
		}
		System.out.println("All available calculations based on certain rocket properties");
		System.out.println();
		System.out.printf("Specific Impulse (seconds):     ");
		double spSeconds = scan.nextDouble(); // seconds
		if (inputFile.length() > 0)
			System.out.println(spSeconds);
		double spVelocity = spSeconds*GRAVITY; // m/s
		System.out.printf("Specific Impulse (m/s):         %.3f\n", spVelocity);
		System.out.printf("Specific Impulse (ft/s):        %.3f\n", spVelocity*METERS_PER_FOOT);
		System.out.printf("Specific Impulse (mi/hr):       %.3f\n", spVelocity*METERS_PER_FOOT/5280*60*60);
		System.out.println();
		System.out.printf("Mass of Propellant (kg):        ");
		double mass = scan.nextDouble(); // kg
		if (inputFile.length() > 0)
			System.out.println(mass);
		System.out.printf("Cubic Area of Propellant (m^3): ");
		double cubicArea = scan.nextDouble(); // m^3
		if (inputFile.length() > 0)
			System.out.println(cubicArea);
		double density = mass / cubicArea; // kg/m^3
		System.out.printf("Density of Propellant (kg/m^3): %.3f\n", density);
		System.out.printf("Area of Propellant (m^2):       ");
		double area = scan.nextDouble(); // m^2
		if (inputFile.length() > 0)
			System.out.println(area);
		double massFlowRate = area * density * spVelocity; // kg / s
		double thrust = massFlowRate * spVelocity; // Newtons
		System.out.println();
		System.out.printf("Mass Flow Rate: (kg/second):    %.3f\n", massFlowRate);
		System.out.printf("Thrust (Newtons):               %.3f\n", thrust);
		System.out.printf("Thrust (Pounds):                %.3f\n", thrust * NEWTONS_PER_POUND);
		System.out.printf("Mass of Payload (kg):           ");
		double rocketMass = scan.nextDouble();
		if (inputFile.length() > 0)
			System.out.println(rocketMass);
		System.out.printf("Weight of Payload (Newtons):    %.3f\n", rocketMass * GRAVITY);
		double acceleration = thrust - (rocketMass + mass) * GRAVITY;
		double burnTime = mass / massFlowRate;
		double height = .5 * acceleration * burnTime * burnTime;
		System.out.println();
		System.out.printf("Burn Time (seconds):            %.3f\n", burnTime);
		System.out.printf("Height of Rocket (meters):      %.3f\n", height);
		System.out.printf("Height of Rocket (ft):          %.3f\n", height*METERS_PER_FOOT);
		System.out.printf("Height of Rocket (miles):       %.3f\n", height*METERS_PER_FOOT/5280);
		double maxHeight = 0;
		double maxVelocity = acceleration * burnTime;
		for (double maxTime = 0; ; maxTime += 0.0001) {
			double nHeight = -.5 * GRAVITY * maxTime * maxTime + maxVelocity * maxTime + height;
			if (nHeight > maxHeight) {
				maxHeight = nHeight;
			} else {
				break;
			}
		}
		System.out.printf("Max Height of Rocket (meters):  %.3f\n", maxHeight);
		System.out.printf("Max Height of Rocket (ft):      %.3f\n", maxHeight*METERS_PER_FOOT);
		System.out.printf("Max Height of Rocket (miles):   %.3f\n", maxHeight*METERS_PER_FOOT/5280);
		
		scan.close();
	}
	
}
