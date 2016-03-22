package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team85.robot.Addresses.*;

import edu.wpi.first.wpilibj.smartdashboard.*;

public class TatorCannon {

	public static final int LOADPOS = 0;		//auto load pos -- 
	public static final int FIREPOS = 170;		//auto fire pos -- 
	public static final int CLOSEFIRE = 247; //firing from right up to tower ramp
	public static final int YBUTTONHEIGHT = 175;
	public static final int ALITTLEOFFTHEGROUND = 1;
	public static final int AUTOHEIGHT = 100;
	
	private static final double DARTTOL = 1.25;		//auto angle tolerance
	private static final double DARTSLOW = 10;
	private static final double DARTMIN = 0;
	private static final double DARTCAPSLOW = 45;
	private static final double DARTMAX = 248;
	private static final boolean WYATTSPRIVILEGE = false;
	
	private static boolean resetAtTop;
	
	public static boolean hasBeenInit;

	private double LOADSPEED = -1.0;	//loading speed
	private double SPITSPEED = 1.0;
	private double FIRERPM = 1.0;		//outerMotor speed ---Voltage mode: 0.75
	private double CUSTOMFIRE;
	private double RPMTOL;				//outerMotor tol
	
	private CannonMode MODE;
	
	private boolean AutoOR = false;
	
	private Joystick _operatorStick;
	private Joystick _driveStick;

	private CANTalon _outerTopMotor, _outerBottomMotor, _dartMotor, _innerTopMotor, _innerBottomMotor;
	
	private Encoder _dartEncoder;
	
	
	private Intake _intake;
	
	private Timer _delayTimer;
	private static final double SPITDELAY = 0.5;
	private static final double LOADDELAY = 0.5;
	private static final double LOADTIME = 1.0;	// = 0.0;	//milliseconds
	private static final double STORAGEDELAY = 0.5;
	private boolean _spitInit, _loadInit, _loadComplete, _storageInit;
	
	private boolean _justFireInit;
	private static final double JUSTFIREDELAY = 1.0, JUSTFIREALLDONE = 2.0;
		
	DigitalInput _topDartLimit;
	DigitalInput _bottomDartLimit;
	DigitalInput _ballNotPresentSensor;
	
	private boolean init;

	public TatorCannon(Joystick operatorStick, Joystick driveStick, Intake intake) {

		_operatorStick = operatorStick;
		_driveStick = driveStick;
		
		_intake = intake;
		
		_outerTopMotor = new CANTalon(CANNON.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(CANNON.OUTER_MOTOR_BOTTOM);
		_dartMotor = new CANTalon(CANNON.DART_MOTOR);

		_innerTopMotor = new CANTalon(CANNON.INNER_MOTOR_TOP);
		_innerBottomMotor = new CANTalon(CANNON.INNER_MOTOR_BOTTOM);
		
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
	
	public boolean init() {
		if (!init) {
			if (!_bottomDartLimit.get()) {
				_dartMotor.set(0);
				setEncoderBot();
				init = true;
				hasBeenInit = true;
			} else {
				_dartMotor.set(0.3);				
			}
		}
		System.out.println("Cannon init:" + init);
		return init;
	}

    public void run(boolean Autonomous) {	//main method
    	AutoOR = Autonomous;
    	    	
    	if (!AutoOR) {
    		setCannonMode();
    		runCannonMode();
    	} else {
    		//Senpai make it do
    	}
    	SmartDashboard.putNumber("Dart Enc", getEncoderValue());
    	SmartDashboard.putBoolean("View Robot/Ball In Storage", !_ballNotPresentSensor.get());

    	SmartDashboard.putData("Dart Encoder", _dartEncoder);
    	SmartDashboard.putData("Ball not present", _ballNotPresentSensor);
    	
    	
    }
    
    private void buttonHeights() {
    	if (_operatorStick.getRawButton(4)){
    		armMove(YBUTTONHEIGHT);
    		_intake.intakeMove(Intake.FLOOR);
    		return;
    	} else if (_operatorStick.getRawButton(9)) {
    		
    		return;
    	} else if (_operatorStick.getRawButton(2)) {
    		
    		return;
    	} else if (_operatorStick.getRawButton(1)) {
    		if (armMove(CLOSEFIRE)) {
    			_intake.intakeMove(Intake.HOME);
    		} else {
    			_intake.intakeMove(Intake.FORTYFIVE);
    		}
    		return;
    	} else if (_operatorStick.getPOV() == 0){ //auto heights both
			armMove(AUTOHEIGHT);
			return;
		} else if (_operatorStick.getPOV() == 270) { //left
			armMove(ALITTLEOFFTHEGROUND);
			return;
		} else {
    		manualArmMotor(_operatorStick.getRawAxis(3));
    		return;
		}
    }
    
    private boolean readyToLoad(){
    	return (Math.abs(_dartMotor.get() - LOADPOS) <= DARTTOL);
    }

    public boolean armMove(double target) {
    	if (hasBeenInit) {
    		if ( Math.abs(getEncoderValue() - target) <= DARTTOL) {
    			manualArmMotor(0);
    			return true;
    		}
    		double speed = ((getEncoderValue() - target) > 0) ? 1.0 : -1.0;
//  	  	double mult = (Math.abs(_dartEncoder.get() - target) <= DARTSLOW) ? 0.5 : 1.0;
        	if (Intake.hasBeenInit && !_intake.belowFortyFive()) {
        		speed = 0;
        		_intake.intakeMove(Intake.FORTYFIVE);
        	}
    		manualArmMotor(speed);
    	}
    	
    	return false;
    }
    
    public boolean runAs(CannonMode mode) {
    	MODE = mode;
    	return runCannonMode();
    }
    
    public void setCannonMode() {
    	if (_driveStick.getRawButton(2)) {		//VISION
  //  		MODE = CannonMode.VISION;
    	} else if (_driveStick.getRawButton(7)) {		//Left bumper
    		MODE = CannonMode.CHARGE;
    	} else if (_operatorStick.getRawButton(7) && (WYATTSPRIVILEGE||!_bottomDartLimit.get())
    			/*Because We KNOW Better than to TRUST WIFI*/) {	//Left Trigger
    		MODE = CannonMode.SPIT;
    	} else if (_operatorStick.getRawButton(8) && _ballNotPresentSensor.get()) {	//Right trigger
    		MODE = CannonMode.STORAGE;
    	} else if (_operatorStick.getRawButton(3) && _ballNotPresentSensor.get()) {	//Intake Button B
    		MODE = CannonMode.IN;
    	} else {
    		MODE = CannonMode.MANUAL;
    	}
    	    }
    //====================
    double previousError = 0;
    double upKp = 1/15;
    double upKd = 0;
    double downKp = 1/15;
    double downKd = 0;
    double maxUpPower = -1.0, minUpPower = -0.65;
    double maxDownPower = 0.8, minDownPower = 0.5;
    
    public void initSafeCoding(){
    	SmartDashboard.putNumber("ZZZ tc upKp", upKp);
    	SmartDashboard.putNumber("ZZZ tc upKd", upKd);
    	SmartDashboard.putNumber("ZZZ tc downKp", downKp);
    	SmartDashboard.putNumber("ZZZ tc downKd", downKd);
    	SmartDashboard.putNumber("ZZZ tc maxUpPower", maxUpPower);
    	SmartDashboard.putNumber("ZZZ tc minUpPower", minUpPower);
    	SmartDashboard.putNumber("ZZZ tc maxDownPower", maxDownPower);
    	SmartDashboard.putNumber("ZZZ tc minDownPower", minDownPower);
    }
    
    public void muchSafeCoding(){
    	//Adjust with FX java on programming computer, along side Driver Station
    	upKp = SmartDashboard.getNumber("ZZZ tc upKp", upKp);
    	upKd = SmartDashboard.getNumber("ZZZ tc upKd", upKd);
    	downKp = SmartDashboard.getNumber("ZZZ tc downKp", downKp);
    	downKd = SmartDashboard.getNumber("ZZZ tc downKd", downKd);
    	maxUpPower = SmartDashboard.getNumber("ZZZ tc maxUpPower", maxUpPower);
    	minUpPower = SmartDashboard.getNumber("ZZZ tc minUpPower", minUpPower);
    	maxDownPower = SmartDashboard.getNumber("ZZZ tc maxDownPower", maxDownPower);
    	minDownPower = SmartDashboard.getNumber("ZZZ tc minDownPower", minDownPower);
    }
    //=====================
    public boolean runCannonMode() {
    		
    	switch (MODE) {
    		case VISION:
    			double error = ImageProcessing.yPixelsToTarget();
    	    	double changeInError = error - previousError;
    	    	previousError = error;
    	    	if (ImageProcessing.withinYTolerance()) {
    	    		manualArmMotor(0.0);
    	    		return true;
    	    	}
    	    	double power = (double) ((error > 0) ?
    	    		upKp * error + upKd * changeInError
    	    		:downKp * error + downKd * changeInError);
    	    	
    	    	System.out.println("tc " + error + " " + power);
    	    	
    	    	power = (power > 0) ?
/*Up*/				(power > maxUpPower) ?
						maxUpPower
						: (power < minUpPower) ?
							minUpPower
							: power
/*Down*/			:(power < maxDownPower) ?
						maxDownPower
						: (power > minDownPower) ?
							minDownPower
							: power;
    	    	
    	    	manualArmMotor(power);
    	    	return false;
//    			break;
    		case CHARGE:
/*    			
 				Starts the firing motors, driver waits for it to speed up,
    			then starts the index motors to feed the ball to the firing motors
    			Turns on outer firing motors
*/    			if (SmartDashboard.getBoolean("DB/Button 3", false)) {
					CUSTOMFIRE = (SmartDashboard.getNumber("DB/Slider 3", 1.0));
					CUSTOMFIRE = (CUSTOMFIRE < 0 || CUSTOMFIRE > 1) ? 1.0 : CUSTOMFIRE;
					setOuter(CUSTOMFIRE);
				} else {
	    			setOuter(FIRERPM);    	    	
				}
    			if(_driveStick.getRawButton(8)) { //Turns on index motors
    	    		indexOut();
    	    	}						
        		buttonHeights();
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
        		armMove(LOADPOS);
    			setOuter(LOADSPEED);
    			indexIn();
    			break;
    		case MANUAL: //Turns everything off
        		buttonHeights();
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
    		case JUSTFIRE:
    			if (!_justFireInit) {
    				_justFireInit = true;
    				_delayTimer.reset();
    			} else {
					setOuter(FIRERPM);
					if (_delayTimer.get() > JUSTFIREALLDONE) {
						indexOff();
					} else if (_delayTimer.get() > JUSTFIREDELAY) {
    					indexOut();
    				}
    			}
    			break;
    	}
    	return false;
    }

    private void setOuter(double speed) {
		_outerTopMotor.set(speed);
		_outerBottomMotor.set(speed);
    }
    private void indexOut() {	//Ready and Aim and Fire
		_innerTopMotor.set(1);
		_innerBottomMotor.set(1);
    }
    private void indexIn() {	//Bring into Storage
		_innerTopMotor.set(-1);
		_innerBottomMotor.set(-1);
    }
    private void indexOff() {	//Free to Shove around
		_innerTopMotor.set(0);
		_innerBottomMotor.set(0);
    }
    public void manualArmMotor(double value) {
    	
		boolean topLimit = _topDartLimit.get();		//open = TRUE
		boolean botLimit = _bottomDartLimit.get();

    	if (!_bottomDartLimit.get()) {
    		setEncoderBot();
    		hasBeenInit = true;
    	}
    	
    	if (!_topDartLimit.get()) {
    		setEncoderTop();
    	}
		
		if ((value < .05 && topLimit == false) || (value > -.05 && botLimit == false) ){
			value = 0;
		} else if (value < 0 && getEncoderValue() > DARTMAX - DARTCAPSLOW) {
			value *= 0.65;
		} else if (value < 0) {		//UP
			value *= 1.0;	//0.8
		} else if (value > 0 && getEncoderValue() <= DARTMIN + DARTCAPSLOW) {
			value *= 0.3;
		} else if (value > 0) {		//DOWN
			value *= 0.8;	//0.5
		}
				
		//DEADBAND
		value = (Math.abs(value) < 0.05) ? 0 : value;
    	if (hasBeenInit && Intake.hasBeenInit) {
    		if (value != 0 && !_intake.belowFortyFive()) {
    			value = 0;
    			_intake.intakeMove(Intake.FORTYFIVE);
    		}
    	}
		_dartMotor.set(value);
			
	}
    
    private void setEncoderBot() {
    	_dartEncoder.reset();
    	resetAtTop = false;
    }
    
    private void setEncoderTop() {
    	_dartEncoder.reset();
    	resetAtTop = true;
    }
    
    private double getEncoderValue() {
    	return resetAtTop ? _dartEncoder.get() + DARTMAX : _dartEncoder.get();
    }
    
}
