package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

import org.usfirst.frc.team85.robot.Addresses.*;

public class Intake {

	DigitalInput upIntakeLimit;
	DigitalInput downIntakeLimit;
	
	private Joystick opStick;

	public static final int LOADPOS = 		-215000;//-487048;
	public static final int LIFTHEIGHT = 	-300000;//NOT TESTED//-744000;
	public static final int FLOOR = 		-297000;//-690000;
	public static final int HOME = 			   1000;
	public static final int PORTDOWN =		0; //down position
	public static final int PORTUP =		0; //for portcolus
	public static final int HORIZONTAL = 	-220000;//-500000;
	public static final int FORTYFIVE =  	-111000;//-250000;
	public static final int AUTOANGLE =  	-150000;//-265000;//-330000
	
	int _encPos;
	
	private double DEADBAND = 0.15;
	
	private double INTAKESLOWRANGE = 20000;	//40k
	private double INTAKETOL = 5000;	//10k
	private CANTalon leftAngleMotor, rightAngleMotor, loadMotor;
	private boolean init;
	public static boolean hasBeenInit;
	
	private Timer _delayTimer;
	private boolean intakeInit;
	private double INTAKEDELAY = 0.5;
	
	//private DigitalInput upIntakeLimit = new DigitalInput(1);

	public Intake(Joystick operatorStick) {
		
		opStick = operatorStick;
		
		leftAngleMotor = new CANTalon(INTAKE.LEFT_INTAKE_MOTOR);
		rightAngleMotor = new CANTalon(INTAKE.RIGHT_INTAKE_MOTOR);
		loadMotor = new CANTalon(INTAKE.LOAD_MOTOR);

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
		
		_delayTimer = new Timer();
		_delayTimer.start();
				     	
        System.out.println("Intake Init Done");
	} 
	
	public void bootInit(){
		init = false;
	}
	
	public boolean init() {
		if (!init) {
			if (upIntakeLimit.get()) {
				leftAngleMotor.set(0);
				rightAngleMotor.set(0);
				rightAngleMotor.setEncPosition(0);
				init = true;
				hasBeenInit = true;
			} else {
				leftAngleMotor.set(-0.2);
				rightAngleMotor.set(-0.2);
			}
		}
		System.out.println("Intake init:" + init);
		return init;
	}

	public boolean run(boolean cannonReady) {
		_encPos = rightAngleMotor.getEncPosition();
		SmartDashboard.putNumber("Intake Position", _encPos);

		if(opStick.getRawButton(3) && hasBeenInit) { //Uses button B, loads the cannon
			return loadCannon(cannonReady);
		}
		else if (opStick.getPOV() == 0 && hasBeenInit){ //auto heights both
			intakeMove(AUTOANGLE);
		} 
		else if (opStick.getPOV() == 180 && hasBeenInit) { // on tower ramp
			intakeMove(LIFTHEIGHT);
		}
		else if (opStick.getPOV() == 90 && hasBeenInit) { //right
			intakeMove(FLOOR);
		}
		else if (opStick.getPOV() == 270 && hasBeenInit) { //left
			intakeMove(LOADPOS);
		}
		else {
			setMotors(opStick.getRawAxis(1));
		}		
		
		if (opStick.getRawButton(5)) {
			loadMotor.set(-1);
		} else if (opStick.getRawButton(6)) {
			loadMotor.set(1);
		} else {
			loadMotor.set(0);
		}
		intakeInit = false;
		return false;
	}
	
	private boolean loadCannon(boolean cannonReady) { //returns if loadMotor is trying to load the cannon
		if (!intakeInit) {
			_delayTimer.reset();
			intakeInit = true;
		} else if (intakeInit && _delayTimer.get() > INTAKEDELAY) {
			loadMotor.set(1);
		}
		if (/*intakeMove(LOADPOS)*/chompa() && cannonReady) {
			return true;
		}
		return false;
	}
	
	public boolean chompa() {
		_encPos = rightAngleMotor.getEncPosition();
		SmartDashboard.putNumber("Intake Position", _encPos);
		SmartDashboard.putNumber("Intake Position Target", LOADPOS);
		if(_encPos <= LOADPOS) {
			if (Math.abs(_encPos-LOADPOS) <= INTAKESLOWRANGE) {
				setMotors(-0.4);//-0.8);
			} else {
				setMotors(-1);
			}
		} else if(_encPos > LOADPOS) {
			if (Math.abs(_encPos-LOADPOS) <= INTAKESLOWRANGE) {
				setMotors(.3);//0.5);
			} else {
				setMotors(1);
			}
		}
		return (_encPos >= (LOADPOS-INTAKETOL) && _encPos <= LOADPOS) ? true : false;
	}

	public boolean intakeMove(double target) {	//setMotors(+) goes down, setMotors(-) goes up
		_encPos = rightAngleMotor.getEncPosition();
		SmartDashboard.putNumber("Intake Position", _encPos);
		SmartDashboard.putNumber("Intake Position Target", target);
		if (target==LOADPOS) {
			if (_encPos >= (LOADPOS-INTAKETOL) && _encPos <= LOADPOS) {
				setMotors(0.0);
				return true;
			}
		} else {
			if (Math.abs(_encPos-target) <= INTAKETOL) {
				setMotors(0.0);
				return true;
			}
		}
		if(_encPos < target) {
			if (Math.abs(_encPos-target) <= INTAKESLOWRANGE) {
				setMotors(-0.4);//-0.8);
			} else {
				setMotors(-1);
			}
		} else if(_encPos > target) {
			if (Math.abs(_encPos-target) <= INTAKESLOWRANGE) {
				setMotors(.3);//0.5);
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
			setArmMotors(0);
			resetPos();
		} else if (value > 0 && botLimit == true) { //positive value goes down
			setArmMotors(0);
		} else {
			value *= 0.5;
			if (Math.abs(value) < DEADBAND) {
				value = 0;
			}
			setArmMotors(value);
		}			
	}
	
	private void setArmMotors(double value) {
		leftAngleMotor.set(value);
		rightAngleMotor.set(value);
		SmartDashboard.putNumber("ArmMotor Output", value);
	}
	
	public void resetPos() {
		rightAngleMotor.setEncPosition(0);
		hasBeenInit = true;
	}
	
	public boolean belowFortyFive() {
		return (rightAngleMotor.getEncPosition() < FORTYFIVE);
	}
	
}

//Testing Testing