package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
//import edu.wpi.first.wpilibj.CANTalon.DonaldTrump; why?
//import edu.wpi.first.wpilibj.CANTalon.BernieSanders.getFreeMoney(19039640481000);
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

import org.usfirst.frc.team85.robot.Addresses.*;

public class Intake {

	DigitalInput upIntakeLimit = new DigitalInput(0);
	DigitalInput downIntakeLimit = new DigitalInput(3);
	
	private Joystick opStick;

	private int LOADPOS = -550000;
	private int LIFTHEIGHT = -880000;
	private int OPENDOOR = -784380;
	private int HOME = 0;
	
	int _encPos;
	
	private double INTAKETOL = 1000; //TODO: Bigger
	private CANTalon leftAngleMotor, rightAngleMotor;
	
	private Relay loadMotor;
	
	//private DigitalInput upIntakeLimit = new DigitalInput(1);

	public Intake(Joystick operatorStick) {
		
		opStick = operatorStick;
		
		leftAngleMotor = new CANTalon(INTAKE.LEFT_INTAKE_MOTOR);
		rightAngleMotor = new CANTalon(INTAKE.RIGHT_INTAKE_MOTOR);
		loadMotor = new Relay(INTAKE.LOAD_MOTOR);

		//leftAngleMotor.changeControlMode(TalonControlMode.Position);
		//rightAngleMotor.changeControlMode(TalonControlMode.Position);

		//TODO: Set all to left and right
		//leftAngleMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);
/*
		rightAngleMotor.

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

	public boolean run(boolean cannonReady) {
		
		_encPos = rightAngleMotor.getEncPosition();
		if(opStick.getRawButton(3)) { //Uses button B, loads the cannon
			return loadCannon(cannonReady);
		}
		else if (opStick.getPOV() == 0){ //Uses button A, move to ground and "suction"
			intakeMove(HOME);
		} 
		else if (opStick.getPOV() == 180) {
			intakeMove(LIFTHEIGHT);
		}
		else if (opStick.getPOV() == 90) {
			intakeMove(OPENDOOR);
		}
		else if (opStick.getPOV() == 270) {
			intakeMove(LOADPOS);
		}
		else {
			setMotors(opStick.getRawAxis(1));
		}
		
		
		
		if(opStick.getRawButton(6)) {
			loadMotor.set(Relay.Value.kForward);
		} else {
			loadMotor.set(Relay.Value.kOff);
		}
		return false;
	}
	
	private void badPickUpLine() {	//Attempts to pick up loitering boulders 
		intakeMove(LOADPOS);
		loadMotor.set(Relay.Value.kForward);
	}

	private boolean loadCannon(boolean cannonReady) { //returns if loadMotor is trying to load the cannon
		if (intakeMove(LOADPOS) && cannonReady) {
			loadMotor.set(Relay.Value.kReverse);
			return true;
		}
		return false;
	}

	private boolean intakeMove(double target) {
		SmartDashboard.putNumber("Intake Position Target", target);
		if (Math.abs(_encPos-target) <= INTAKETOL) {
			return true;
		}
		else if(_encPos < target) {
			setMotors(-1);
		}
		else if(_encPos > target) {
			setMotors(1);
		}
		
		return false;
	}
	
	private void setMotors(double value) {		
		SmartDashboard.putData("Top Intake Limit: ", upIntakeLimit);
		SmartDashboard.putData("Bot Intake Limit: ", downIntakeLimit);
		SmartDashboard.putInt("Intake encoder", rightAngleMotor.getEncPosition());
		
		boolean topLimit = upIntakeLimit.get();
		boolean botLimit = downIntakeLimit.get();
		
		if (value < .05 && topLimit == true) { 
			leftAngleMotor.set(0);
			rightAngleMotor.set(0);
			rightAngleMotor.setEncPosition(0);
		} else if (value > -.05 && botLimit == true) { 
			leftAngleMotor.set(0);
			rightAngleMotor.set(0);
		} else {
			leftAngleMotor.set(value/2);
			rightAngleMotor.set(value/2);
		}			
	}
}

