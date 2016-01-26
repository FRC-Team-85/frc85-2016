package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class Intake {
	

	private double LOADPOS;
	private double FEEDPOS;
	
	private double INTAKETOL;
	private CANTalon angleMotor, loadMotor;

	/*
	public boolean run() {
		if (button) {
			load();
		} else if (button) {
			return feed();
		} else {
			return false;
		}
	}

	private boolean loadCannon(boolean cannonReady) {
		if (intakeMove(LOADPOS) && cannonReady) {
			loadMotor.set(value);
		}
	}

	private void groundFeed() {
		if (intakeMove(FEEDPOS)) {
		
		}
	}
	
	private boolean intakeMove(double target) {
		if (Math.abs(angleMotor.get()-target) =< INTAKETOL) {
			return true;
		} else {
			motor.set(target);
			return false;
		}
	} 
	 */
}
