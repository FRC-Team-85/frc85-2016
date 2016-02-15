package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
//import edu.wpi.first.wpilibj.CANTalon.DonaldTrump;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

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

		//leftAngleMotor.changeControlMode(TalonControlMode.Position);
		//rightAngleMotor.changeControlMode(TalonControlMode.Position);

		//TODO: Set all to left and right
		//leftAngleMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);
		/*rightAngleMotor.

		angleMotor.reverseSensor(true); // Set to actual value

		angleMotor.setP(Constants.ARM_INTAKE.P);
		angleMotor.setI(Constants.ARM_INTAKE.I);
		angleMotor.setD(Constants.ARM_INTAKE.D);
		angleMotor.setF(Constants.ARM_INTAKE.F);

		angleMotor.enableLimitSwitch(true, true);
*/
		leftAngleMotor.enableBrakeMode(true);
		rightAngleMotor.enableBrakeMode(true);
        	
        	System.out.println("Intake Init Done");
	} 

	public boolean run (boolean cannonReady) {
		if (opStick.getRawButton(2) && !opStick.getRawButton(3)) { //Uses button A, move to ground and "suction"
			badPickUpLine();
		} else if (opStick.getRawButton(3)) { //Uses button B, loads the cannon
			//return loadCannon(cannonReady);
		} else {
			this.SetMotors(opStick.getRawAxis(1));
		}
/*
		System.out.println("Cannon is ready.");
		SmartDashboard.putNumber("LoadMotor: ", loadMotor.get());
*/
		return false;
	}
	
	private void badPickUpLine() {	//Attempts to pick up loitering boulders 
		System.out.println("Preparing Cannon...");
		intakeMove(PICKUPPOSITION);
		loadMotor.set(Relay.Value.kForward);
	}
/*
	private boolean loadCannon(boolean cannonReady) { //returns if loadMotor is trying to load the cannon
		if (intakeMove(LOADPOS) && cannonReady) {
			loadMotor.set(Relay.Value.kReverse);
			return true;
		}
		return false;
	}
*/
	private boolean intakeMove(double target) {
		if (Math.abs(leftAngleMotor.get()-target) <= INTAKETOL &&
				Math.abs(rightAngleMotor.get()-target) <= INTAKETOL) {
			return true;
		}
		this.SetMotors(target);
		return false;
	}
	
	private void SetMotors(double value) {
		leftAngleMotor.set(value);
		rightAngleMotor.set(value);
	}
	
	public void rollers() {
		if(opStick.getRawButton(6)) {
			loadMotor.set(Relay.Value.kForward);
		} else {
			loadMotor.set(Relay.Value.kOff);
		}
	}
	
	public void Stop() {
		//Incomplete
		DigitalInput limitSwitch;
		limitSwitch = new DigitalInput(0);
		
		boolean limit = limitSwitch.get();
		
		if (limit == false) {
			
		}
	}
	
}
