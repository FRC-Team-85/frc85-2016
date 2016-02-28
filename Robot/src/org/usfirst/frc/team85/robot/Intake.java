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

	DigitalInput upIntakeLimit;
	DigitalInput downIntakeLimit;
	
	private Joystick opStick;

	public final int LOADPOS = -555000;
	public final int LIFTHEIGHT = -880000;
	public final int FLOOR = -785000;
	public final int HOME = 1000;
	
	int _encPos;
	
	private double DEADBAND = 0.05;
	
	private double INTAKESLOWRANGE = 40000;	//35k
	private double INTAKETOL = 10000;	//10k
	private CANTalon leftAngleMotor, rightAngleMotor;
	
	private Relay loadMotor;
	private boolean check;
	
	//private DigitalInput upIntakeLimit = new DigitalInput(1);

	public Intake(Joystick operatorStick) {
		
		opStick = operatorStick;
		
		leftAngleMotor = new CANTalon(INTAKE.LEFT_INTAKE_MOTOR);
		rightAngleMotor = new CANTalon(INTAKE.RIGHT_INTAKE_MOTOR);
		loadMotor = new Relay(INTAKE.LOAD_MOTOR);

		upIntakeLimit = new DigitalInput(INTAKE.UP_INTAKE_LIMIT);
		downIntakeLimit = new DigitalInput(INTAKE.DOWN_INTAKE_LIMIT);
		
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
		
		check = false;
        	
        System.out.println("Intake Init Done");
	} 

	public boolean run(boolean cannonReady) {
/*		
		if (!check) {
			if (!upIntakeLimit.get()) {
				setMotors(-0.3);
			} else {
				check = true;
				rightAngleMotor.setEncPosition(0);
			}
			return false;
		}
	*/	
		_encPos = rightAngleMotor.getEncPosition();
		SmartDashboard.putNumber("View Robot/Intake Enc", _encPos);
		
		if(opStick.getRawButton(3)) { //Uses button B, loads the cannon
			return loadCannon(cannonReady);
		}
		else if (opStick.getPOV() == 0){ 
			intakeMove(HOME);
		} 
		else if (opStick.getPOV() == 180) {
			intakeMove(LIFTHEIGHT);
		}
		else if (opStick.getPOV() == 90) {
			intakeMove(FLOOR);
		}
		else if (opStick.getPOV() == 270) {
			intakeMove(LOADPOS);
		}
		else {
			setMotors(opStick.getRawAxis(1));
		}		
		
		if (opStick.getRawButton(5)) {
			loadMotor.set(Relay.Value.kReverse);
		} else if (opStick.getRawButton(6)) {
			loadMotor.set(Relay.Value.kForward);
		} else {
			loadMotor.set(Relay.Value.kOff);
		}
		return false;
	}
	
	private boolean loadCannon(boolean cannonReady) { //returns if loadMotor is trying to load the cannon
		loadMotor.set(Relay.Value.kForward);
		if (intakeMove(LOADPOS) && cannonReady) {
			return true;
		}
		return false;
	}

	private boolean intakeMove(double target) {	//setMotors(+) goes down, setMotors(-) goes up
		SmartDashboard.putNumber("Intake Position Target", target);
		if (target==LOADPOS) {
			if (_encPos > (LOADPOS-INTAKETOL) && _encPos < LOADPOS) {
				setMotors(0.0);
				return true;
			}
		} else {
			if (Math.abs(_encPos-target) <= INTAKETOL) {
				return true;
			}
		}
		if(_encPos < target) {
			if (Math.abs(_encPos-target) <= INTAKESLOWRANGE) {
				setMotors(-0.3);
			} else {
				setMotors(-1);
			}
		} else if(_encPos > target) {
			if (Math.abs(_encPos-target) <= INTAKESLOWRANGE) {
				setMotors(0.3);
			} else {
				setMotors(1);
			}
		}
		return false;
	}
	
	private void setMotors(double value) {		
		SmartDashboard.putData("Top Intake Limit: ", upIntakeLimit);
		SmartDashboard.putData("Bot Intake Limit: ", downIntakeLimit);
		SmartDashboard.putInt("Intake encoder", _encPos);
		
		boolean topLimit = upIntakeLimit.get();
		boolean botLimit = downIntakeLimit.get();
		
		if (value < 0 && topLimit == true) { //negative value goes up
			leftAngleMotor.set(0);
			rightAngleMotor.set(0);
			rightAngleMotor.setEncPosition(0);
		} else if (value > 0 && botLimit == true) { //positive value goes down
			leftAngleMotor.set(0);
			rightAngleMotor.set(0);
		} else {
			value *= 0.5;
			if (Math.abs(value) < DEADBAND) {
				value = 0;
			}
			leftAngleMotor.set(value);
			rightAngleMotor.set(value);
		}			
	}
	
}

