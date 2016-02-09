package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
//import edu.wpi.first.wpilibj.CANTalon.DonaldTrump;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;

import org.usfirst.frc.team85.robot.Addresses.*;

public class Intake {

	private Joystick opStick;

	private double LOADPOS;
	private double PICKUPPOSITION;

	private double INTAKETOL;
	private CANTalon leftAngleMotor, rightAngleMotor;
	
	private Relay loadMotor;

	public Intake(Joystick operatorStick) {
		
		opStick = operatorStick;
		
		leftAngleMotor = new CANTalon(INTAKE.LEFT_INTAKE_MOTOR);
		rightAngleMotor = new CANTalon(INTAKE.RIGHT_INTAKE_MOTOR);
		loadMotor = new Relay(INTAKE.LOAD_MOTOR);

		leftAngleMotor.changeControlMode(TalonControlMode.Position);
		rightAngleMotor.changeControlMode(TalonControlMode.Position);
		loadMotor.changeControlMode(TalonControlMode.Voltage); // Or Position?

		//TODO: Set all to left and right
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
	
	private void badPickUpLine() {	//Attempts to pick up loitering boulders 
		intakeMove(PICKUPPOSITION);
		loadMotor.set(-1);
	}

	private boolean loadCannon(boolean cannonReady) { //returns if loadMotor is trying to load the cannon
		if (intakeMove(LOADPOS) && cannonReady) {
			loadMotor.set(1);
			return true;
		}
		return false;
	}

	private boolean intakeMove(double target) {
		if (Math.abs(angleMotor.get()-target) <= INTAKETOL) {
			return true;
		}
		angleMotor.set(target);
		return false;
	}
	
}
