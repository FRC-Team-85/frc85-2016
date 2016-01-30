package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

import org.usfirst.frc.team85.robot.Addresses.*;

public class Intake {


	private double LOADPOS;
	private double FEEDPOS;

	private double INTAKETOL;
	private CANTalon angleMotor, loadMotor;

	public Intake() {
		angleMotor = new CANTalon(INTAKE.ANGLE_MOTOR);
		loadMotor = new CANTalon(INTAKE.LOAD_MOTOR);

		angleMotor.changeControlMode(TalonControlMode.Position);
		loadMotor.changeControlMode(TalonControlMode.Voltage); // Or Position?

		angleMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);

		angleMotor.reverseSensor(true); // Set to actual value

		angleMotor.setP(Constants.ARM_INTAKE.P);
		angleMotor.setI(Constants.ARM_INTAKE.I);
		angleMotor.setD(Constants.ARM_INTAKE.D);
		angleMotor.setF(Constants.ARM_INTAKE.F);

		angleMotor.enableLimitSwitch(true, true);

		angleMotor.setBrakeMode(true);
		loadMotor.setBrakeMode(true); // Or not?

	}

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
