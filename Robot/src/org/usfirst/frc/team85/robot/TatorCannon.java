package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team85.robot.Addresses.*;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class TatorCannon {

	private double LOADPOS;		//auto load pos -- around 4.70
	private double FIREPOS;		//auto fire pos -- around 2.70
	private double ARMTOL;		//auto angle tol

	private double LOADSPEED = -1.0;	//loading speed
	private double SPITSPEED = 1.0;
	private double FIRERPM = 1.0;		//outerMotor speed ---Voltage mode: 0.75
	private double RPMTOL;		//outerMotor tol
	
	private static final double ARM_LOW_LIMIT = 4.45;//4.70;//4.15;	//Rename these
	private static final double ARM_HIGH_LIMIT = 2.65;//2.75;		//Rename these
	
	private CannonMode MODE = CannonMode.OFF;
	
	private boolean AutoOR = false;
	
	private Joystick _operatorStick;
	private Joystick _driveStick;

	private CANTalon _outerTopMotor, _outerBottomMotor, _armMotor;
	private Relay _innerTopMotor, _innerBottomMotor;
	
	private AnalogInput _armPot;
	private Encoder _dartEncoder;
	private Intake _intake;
	
	private Timer _delayTimer;
	private static final double SPITDELAY = 0.5;
	private static final double LOADDELAY = 0.5;
	private static final double LOADTIME = 1.0;	// = 0.0;	//milliseconds
	private static final double STORAGEDELAY = 0.5;
	private boolean _spitInit, _loadInit, _loadComplete, _storageInit;
	
	private double _currentPosition;
	private double _armAxis;
	
	DigitalInput topDartLimit = new DigitalInput(1);
	DigitalInput bottomDartLimit = new DigitalInput(2);
	DigitalInput _ballNotPresentSensor;

	public TatorCannon(Joystick operatorStick, Joystick driveStick, Intake intake) {

		_operatorStick = operatorStick;
		_driveStick = driveStick;
		
		_outerTopMotor = new CANTalon(CANNON.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(CANNON.OUTER_MOTOR_BOTTOM);
		_armMotor = new CANTalon(CANNON.ARM_MOTOR);
		_armPot = new AnalogInput(CANNON.ARM_POT);

		_innerTopMotor = new Relay(CANNON.INNER_MOTOR_TOP, Relay.Direction.kBoth);
		_innerBottomMotor = new Relay(CANNON.INNER_MOTOR_BOTTOM, Relay.Direction.kBoth);
		
		_ballNotPresentSensor = new DigitalInput(CANNON.BALL_NOT_PRESENT_SENSOR);
		
		_intake = intake;
		
		_dartEncoder = new Encoder(Addresses.CANNON.DART_ENCODER_CH_A, Addresses.CANNON.DART_ENCODER_CH_B);
		
		_delayTimer = new Timer();
		_delayTimer.start();
		
/*
		_outerTopMotor.changeControlMode(TalonControlMode.Speed);
		_outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		_armMotor.changeControlMode(TalonControlMode.Position);
		
		_outerTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 		//Native units of 1/4096th of a revolution, but
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 	//Adjusted units of revolutions
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); 							//Native units of 1/1024th of full range

		_outerTopMotor.reverseSensor(true); //TODO: set to real value
		_outerBottomMotor.reverseSensor(true);
		_armMotor.reverseSensor(true);

		_outerTopMotor.setP(Constants.CANNON.P);
		_outerTopMotor.setI(Constants.CANNON.I);
		_outerTopMotor.setD(Constants.CANNON.D);
		_outerTopMotor.setF(Constants.CANNON.F);

		_outerBottomMotor.setP(Constants.CANNON.P);
		_outerBottomMotor.setI(Constants.CANNON.I);
		_outerBottomMotor.setD(Constants.CANNON.D);
		_outerBottomMotor.setF(Constants.CANNON.F);

		_armMotor.setP(Constants.ARM_CANNON.P);
		_armMotor.setI(Constants.ARM_CANNON.I);
		_armMotor.setD(Constants.ARM_CANNON.D);
		_armMotor.setF(Constants.ARM_CANNON.F);

		_armMotor.enableLimitSwitch(true, true);
*/
        _outerTopMotor.enableBrakeMode(false);
        _outerBottomMotor.enableBrakeMode(false);
        _armMotor.enableBrakeMode(true);
        System.out.println("TatorCannon Init Done");
        
    }

    public void run(boolean Autonomous) {	//main method
    	AutoOR = Autonomous;
    	if (!AutoOR) {
    		setArmMotor(_operatorStick.getRawAxis(3));
    		setCannonMode();
    		runCannonMode();
    	} else {
    		//Senpai make it do
    	}
    	SmartDashboard.putData("Dart Encoder", _dartEncoder);
    }
    
    private boolean readyToLoad(){
    	return (Math.abs(_armMotor.get() - LOADPOS) <= ARMTOL);
    }

    public boolean armMove(double target) {
    	if ( Math.abs(_armMotor.get() - target) <= ARMTOL) { //Because we're using a PID loop for positioning,
    		return true;									 //this entire if-block is probably unnecessary
    	}
    	_armMotor.set(target);
    	return false;
    }
    
    public void setCannonMode() {
    	SmartDashboard.putData("Ball not present ", _ballNotPresentSensor);
    	if (_driveStick.getRawButton(7)) {				//Left bumper
    		MODE = CannonMode.CHARGE;
    	} else if (_operatorStick.getRawButton(7)) {
    		MODE = CannonMode.SPIT;
    	} else if ( (_operatorStick.getRawButton(8)||_operatorStick.getRawButton(3)) && _ballNotPresentSensor.get()) {	//Right trigger
    		MODE = CannonMode.STORAGE;
    	} else {
    		MODE = CannonMode.OFF;
    	}
    	
    	if (AutoOR) {
    		
    	}
    }
    
    public void runCannonMode() {
    		
    	switch (MODE) {
    		case CHARGE:
/*    			
 				Starts the firing motors, driver waits for it to speed up,
    			then starts the index motors to feed the ball to the firing motors
    			Turns on outer firing motors
*/    			
    			setOuter(FIRERPM);    	    	
    			if(_driveStick.getRawButton(8)) { //Turns on index motors
    	    		indexOut();
    	    	}
    	    	break;
    		case SPIT:
    			if (!_spitInit) {
    				_spitInit = true;
    				_delayTimer.reset();
    			} else {
					setOuter(SPITSPEED);
    				if (_delayTimer.get() > SPITDELAY) {
    					indexOut();
    				}
    			}
    			break;
    		case STORAGE: //Sucks in ball
    			setOuter(LOADSPEED);
    			indexIn();
    			break;
    		case OFF: //Turns everything off
    			setOuter(0.0);
    			indexOff();
        		_spitInit = false;
        		_loadInit = false;
        		_storageInit = false;
    			break;
    		case AUTOFIRE:
    			if (true) {	//opButton or Auto
    				if (armMove(FIREPOS) &&
        					(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) &&
        					(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) ) {
    					indexOut();
        				//MODE = asdfasdfasdfas;
    				} else {
        				setOuter(FIRERPM);
        				indexOff();
        			}
    			}
    			break;
    		case AUTOLOAD:
    			if (_intake.run(readyToLoad()) && (true) && !_loadComplete) {	//opButton or Auto
    	        	// if _intake is trying to load the cannon, wants to load, and not done loading
    	        	if (!_loadInit) {
    	        		_delayTimer.reset();
    	        		_loadInit = true;
    	        	} else {
	        			setOuter(LOADSPEED);
    	        		if (_delayTimer.get() > LOADDELAY){
    	        			indexIn();
    	        		} else if (_delayTimer.get() > LOADTIME) {
    	        			indexOff();
    	        			setOuter(0.0);
    	            		_loadComplete = true;
    	        		}
    	        	}
    	        } else {
    	        	_loadInit = false;
    	        	_loadComplete = false;
    	        }
    	        //return _loadComplete;
    			break;
    	}
    }

    private void setOuter(double speed) {
		_outerTopMotor.set(speed);
		_outerBottomMotor.set(speed);
    }
    private void indexOut() {	//Ready and Aim and Fire
		_innerTopMotor.set(Relay.Value.kForward);
		_innerBottomMotor.set(Relay.Value.kForward);
    }
    private void indexIn() {	//Bring into Storage
		_innerTopMotor.set(Relay.Value.kReverse);
		_innerBottomMotor.set(Relay.Value.kReverse);
    }
    private void indexOff() {	//Free to Shove around
		_innerTopMotor.set(Relay.Value.kOff);
		_innerBottomMotor.set(Relay.Value.kOff);
    }
    public void setArmMotor(double value) {
		boolean topLimit = topDartLimit.get();
		boolean botLimit = bottomDartLimit.get();
		
		if (value < .05 && topLimit == false) { 
			_armMotor.set(0);
		} else if (value > -.05 && botLimit == false) { 
			_armMotor.set(0);
		} else if (value < 0) {
			_armMotor.set(value * 0.65);
		} else if (value > 0) {
			_armMotor.set(value * 0.4);
		}
			
	}
}
    	  