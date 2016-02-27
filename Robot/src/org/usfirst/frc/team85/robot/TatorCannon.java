package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team85.robot.Addresses.*;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class TatorCannon {

	private static double LOADPOS;		//auto load pos -- 
	private static double FIREPOS;		//auto fire pos -- 
	private static double DARTTOL;		//auto angle tol
	private static final double ARMMIN = 0;
	private static final double ARMMAX = 0;
	private static final boolean WYATTSPRIVILEGE = false;

	private double LOADSPEED = -0.5;	//loading speed
	private double SPITSPEED = 1.0;
	private double FIRERPM = 1.0;		//outerMotor speed ---Voltage mode: 0.75
	private double RPMTOL;				//outerMotor tol
	
	private CannonMode MODE = CannonMode.OFF;
	
	private boolean AutoOR = false;
	
	private Joystick _operatorStick;
	private Joystick _driveStick;

	private CANTalon _outerTopMotor, _outerBottomMotor, _dartMotor;
	private Relay _innerTopMotor, _innerBottomMotor;
	
	private Encoder _dartEncoder;
	private Intake _intake;
	
	private Timer _delayTimer;
	private static final double SPITDELAY = 0.5;
	private static final double LOADDELAY = 0.5;
	private static final double LOADTIME = 1.0;	// = 0.0;	//milliseconds
	private static final double STORAGEDELAY = 0.5;
	private boolean _spitInit, _loadInit, _loadComplete, _storageInit;
		
	DigitalInput _topDartLimit;
	DigitalInput _bottomDartLimit;
	DigitalInput _ballNotPresentSensor;

	public TatorCannon(Joystick operatorStick, Joystick driveStick, Intake intake) {

		_operatorStick = operatorStick;
		_driveStick = driveStick;
		
		_intake = intake;
		
		_outerTopMotor = new CANTalon(CANNON.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(CANNON.OUTER_MOTOR_BOTTOM);
		_dartMotor = new CANTalon(CANNON.DART_MOTOR);

		_innerTopMotor = new Relay(CANNON.INNER_MOTOR_TOP, Relay.Direction.kBoth);
		_innerBottomMotor = new Relay(CANNON.INNER_MOTOR_BOTTOM, Relay.Direction.kBoth);
		
		_topDartLimit = new DigitalInput(CANNON.DART_TOP_LIMIT);
		_bottomDartLimit = new DigitalInput(CANNON.DART_BOTTOM_LIMIT);
		_ballNotPresentSensor = new DigitalInput(CANNON.BALL_NOT_PRESENT_SENSOR);
		
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
        _dartMotor.enableBrakeMode(true);
        System.out.println("TatorCannon Init Done");
        
    }

    public void run(boolean Autonomous) {	//main method
    	AutoOR = Autonomous;
    	if (!AutoOR) {
    		manualArmMotor();
    		setCannonMode();
    		runCannonMode();
    	} else {
    		//Senpai make it do
    	}
    	SmartDashboard.putData("Dart Encoder", _dartEncoder);
    }
    
    private boolean readyToLoad(){
    	return (Math.abs(_dartMotor.get() - LOADPOS) <= DARTTOL);
    }

    public boolean armMove(double target) {
    	if ( Math.abs(_dartMotor.get() - target) <= DARTTOL) { //Because we're using a PID loop for positioning,
    		return true;									 //this entire if-block is probably unnecessary
    	}
    	_dartMotor.set(target);
    	return false;
    }
    
    public void setCannonMode() {
    	SmartDashboard.putData("Ball not present ", _ballNotPresentSensor);
    	if (_driveStick.getRawButton(7)) {		//Left bumper
    		MODE = CannonMode.CHARGE;
    	} else if (_operatorStick.getRawButton(7) && (WYATTSPRIVILEGE||!_bottomDartLimit.get())
    			/*Because We KNOW Better than to TRUST WIFI*/) {	//Left Trigger
    		MODE = CannonMode.SPIT;
    	} else if (_operatorStick.getRawButton(8) && _ballNotPresentSensor.get()) {	//Right trigger
    		MODE = CannonMode.STORAGE;
    	} else if (_operatorStick.getRawButton(3) && _ballNotPresentSensor.get()) {	//Intake Button B
    		MODE = CannonMode.IN;
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
    		case IN: //Sucks in ball
//    			armMove(LOADPOS);
    			if (_bottomDartLimit.get()) {
    				armMove(0.4);
    			}
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
    public void manualArmMotor() {
    	double value = _operatorStick.getRawAxis(3);
    	
		boolean topLimit = _topDartLimit.get();		//open = TRUE
		boolean botLimit = _bottomDartLimit.get();

    	if (!_bottomDartLimit.get()) {
    		_dartEncoder.reset();
    	}
		
		if ((value < .05 && topLimit == false) || (value > -.05 && botLimit == false) ){
			value = 0;
		} else if (value < 0) {		//DOWN
			value *= 0.65;
		} else if (value > 0) {		//UP
			value *= 0.4;
		}
				
		//DEADBAND
		value = (Math.abs(value) < 0.05) ? 0 : value;
		_dartMotor.set(value);
			
	}
}