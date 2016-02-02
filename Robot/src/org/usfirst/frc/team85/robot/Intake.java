package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;

import org.usfirst.frc.team85.robot.Addresses.*;

public class Intake {

	private Joystick opStick;

	private double LOADPOS;
	private double PICKUPPOSITION;

	private double INTAKETOL;
	private CANTalon angleMotor, loadMotor;

	public Intake(Joystick operatorStick) {
		
		opStick = operatorStick;
		
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

		angleMotor.enableBrakeMode(true);
		loadMotor.enableBrakeMode(true); // Or not?
	}

	public boolean run(boolean cannonReady) {
		if (opStick.getRawButton(2) && !opStick.getRawButton(3)) { //Uses button A, move to ground and "suction"
			badPickUpLine();
		} else if (opStick.getRawButton(3)) { //Uses button B, loads the cannon
			return loadCannon(cannonReady);
		}
		return false;
	}
	
	private void badPickUpLine() {
		intakeMove(PICKUPPOSITION);
		loadMotor.set(-1);
	}

	private boolean loadCannon(boolean cannonReady) {
		if (intakeMove(LOADPOS) && cannonReady) {
			loadMotor.set(1);
			return true;
		}
		return false;
	}

	private void groundFeed() {
		if (intakeMove(PICKUPPOSITION)) {

		}
	}

	private boolean intakeMove(double target) {
		if (Math.abs(angleMotor.get()-target) <= INTAKETOL) {
			return true;
		}
		angleMotor.set(target);
		return false;
	}
	
}
