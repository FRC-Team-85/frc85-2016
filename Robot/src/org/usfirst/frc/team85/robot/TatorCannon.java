package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team85.robot.Addresses.*;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TatorCannon {

	private double LOADPOS;		//load pos
	private double FIREPOS;		//fire pos
	private double ARMTOL;		//angle tol

	private double LOADSPEED;	//loading speed

	private double FIRERPM;		//outerMotor speed
	private double RPMTOL;		//outerMotor tol
	
	private static final double ARM_LOW_LIMIT = 4.45;//4.70;//4.15;
	private static final double ARM_HIGH_LIMIT = 2.65;//2.75;

	private Boolean firstCheck = false;

	private Joystick _operatorStick;
	private Joystick _driveStick;

	private CANTalon _outerTopMotor, _outerBottomMotor, _armMotor;
	private Relay _innerTopMotor, _innerBottomMotor;
	
	private AnalogInput _armPot;
	private Intake _intake;
	
	private Timer _loadTimer;
	private double _loadTime;	// = 0.0;	//millisecs
	private boolean _loadInit, _loadComplete;
	
	private double _currentPosition;
	private double _armAxis;

	public TatorCannon(Joystick operatorStick, Joystick driveStick, Intake intake) {

		_operatorStick = operatorStick;
		_driveStick = driveStick;
		
		_outerTopMotor = new CANTalon(CANNON.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(CANNON.OUTER_MOTOR_BOTTOM);
		_armMotor = new CANTalon(CANNON.ARM_MOTOR);
		_armPot = new AnalogInput(CANNON.ARM_POT);

		_innerTopMotor = new Relay(CANNON.INNER_MOTOR_TOP, Relay.Direction.kBoth);
		_innerBottomMotor = new Relay(CANNON.INNER_MOTOR_BOTTOM, Relay.Direction.kBoth);
		
		_intake = intake;

//		_outerTopMotor.changeControlMode(TalonControlMode.Speed);
//		_outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		//_armMotor.changeControlMode(TalonControlMode.Position);
		
		

		//_outerTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 		//Native units of 1/4096th of a revolution, but
		//_outerBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 	//Adjusted units of revolutions
		//_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); //Native units of 1/1024th of full range

		//_outerTopMotor.reverseSensor(true); //TODO: set to real value
		//_outerBottomMotor.reverseSensor(true);
		//_armMotor.reverseSensor(true);
/*
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
*/
		//_armMotor.enableLimitSwitch(true, true);

        _outerTopMotor.enableBrakeMode(false);
        _outerBottomMotor.enableBrakeMode(false);
        _armMotor.enableBrakeMode(true);
        System.out.println("TatorCannon Init Done");
        
    }

    public void run(boolean Autonomous) {	//main method
        //fire(Autonomous);
        //load(Autonomous);
    	_currentPosition = _armPot.getVoltage();
    	_armAxis = _operatorStick.getRawAxis(3);
    	if ((_currentPosition < ARM_LOW_LIMIT && _currentPosition > ARM_HIGH_LIMIT) ||
    			(_currentPosition > ARM_HIGH_LIMIT && _armAxis > 0) ||
    			(_currentPosition < ARM_LOW_LIMIT && _armAxis < 0)) {
    		_armMotor.set(_armAxis);
    	} else {
    		_armMotor.set(0);
    	}
    	
    	
    	
    	//SmartDashboard.putNumber("Arm pot", );
    }
    
    private void fire(boolean Autonomous) {
    	if (_operatorStick.getRawButton(99) || Autonomous) { //Uses button X
    		if ( armMove(FIREPOS) &&
    				(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) &&
    				(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) ) {
    			setInnerIgnition();
    		}
    		setOuter(FIRERPM);
    		setInnerTrump();
    	}  
    }
    
    private boolean load(boolean Autonomous) {
        if(_intake.run(readyToLoad()) && (_operatorStick.getRawButton(99) || Autonomous) && !_loadComplete) {	
        	// if _intake is trying to load the cannon, wants to load, and not done loading
        	if (!_loadInit) {
        		_loadTimer.reset();
        		_loadTimer.start();
        		_loadInit = true;
        	} else {
        		if (_loadTimer.get() > _loadTime) {
        			setInnerFree();
        			setOuter(0.0);
            		_loadComplete = true;
        		} else {
        			setInnerHold();
        			setOuter(LOADSPEED);
        		}
        	}
        } else {
        	_loadInit = false;
        	_loadComplete = false;
        }
        return _loadComplete;
    }

    private boolean readyToLoad(){
    	return (Math.abs(_armMotor.get() - LOADPOS) <= ARMTOL);
    }

    private boolean armAtTop() {
		boolean softLimited = _armMotor.isForwardSoftLimitEnabled() &&
							_armMotor.get() >= _armMotor.getForwardSoftLimit();
		return _armMotor.isFwdLimitSwitchClosed() || softLimited;
	}

	private boolean armAtBottom() {
		boolean softLimited = _armMotor.isReverseSoftLimitEnabled() &&
							_armMotor.get() <= _armMotor.getReverseSoftLimit();
		return _armMotor.isRevLimitSwitchClosed() || softLimited;
	}

    public void armCheck(){ //When robot starts up, moves cannon all the way down
        if (!firstCheck){
            _armMotor.enableForwardSoftLimit(false);
            _armMotor.enableReverseSoftLimit(false);
            if (!armAtBottom()) {
                _armMotor.set (_armMotor.get() - 0.01);
            }
            firstCheck = true;
            _armMotor.setPosition(0);
            _armMotor.enableForwardSoftLimit(true);
            _armMotor.setForwardSoftLimit(0.25); //Tuning required (rotations (probably))
            _armMotor.enableReverseSoftLimit(true);
            _armMotor.setReverseSoftLimit(0.0); //Rotations (probably), but still 0 even if it isn't
        }
    }

    private void setOuter(double speed) {
		_outerTopMotor.set(1);
		_outerBottomMotor.set(1);
    }
    private void setInnerIgnition() {	//Ready and Aim and Fire
		_innerTopMotor.set(Relay.Value.kForward);
		_innerBottomMotor.set(Relay.Value.kForward);
    }
    private void setInnerHold() {	//Bring into Storage
		_innerTopMotor.set(Relay.Value.kReverse);
		_innerBottomMotor.set(Relay.Value.kReverse);
    }
    private void setInnerFree() {	//Free to Shove around
		_innerTopMotor.set(Relay.Value.kOff);
		_innerBottomMotor.set(Relay.Value.kOff);
    }
    private void setInnerTrump() {	//Sturdy Like the Wall of Mexico
		_innerTopMotor.set(Relay.Value.kForward);
		_innerBottomMotor.set(Relay.Value.kForward);
    }

    public boolean armMove(double target) {
    	if ( Math.abs(_armMotor.get() - target) <= ARMTOL) { //Because we're using a PID loop for positioning,
    		return true;									 //this entire if-block is probably unnecessary
    	}
    	_armMotor.set(target);
    	return false;
    }
    
    public void DANGER() {
    	double set = _operatorStick.getY();
    				
    	System.out.println("!Danger!" + set);

    	_outerTopMotor.set(set);
    	_outerBottomMotor.set(set);
    	System.out.println("OTM "+_outerTopMotor.get());
    	System.out.println("OBM "+_outerBottomMotor.get());
    	
    	_armMotor.set(_operatorStick.getX()*100);
    	System.out.print("ARM"+_armMotor.get());
    }
 
    	public void shootBall() {
    		int mode;
    		SmartDashboard.putNumber("pot", _currentPosition);
    		if (_driveStick.getRawButton(5)) {
    			mode = 5;
    		} else if (_driveStick.getRawButton(6)) { //Probably should be deleted
    			mode = 6; //Probably should be deleted
    		} else if (_operatorStick.getRawButton(8)) {
    			mode = 8;
    		} else {
    			mode = 0;
    		}
    		
    		
    		switch (mode) {
    		case 5: //Starts the firing motors, driver waits for it to speed up, then starts the index motors to feed the ball to the firing motors
    			
    			//Turns on outer firing motors
    			_outerBottomMotor.set(.75);
    	    	_outerTopMotor.set(.75);
    	    	
    	    	if(_driveStick.getRawButton(6)) {
    	    		//Turns on index motors
    	    		_innerBottomMotor.set(Relay.Value.kForward);
        	    	_innerTopMotor.set(Relay.Value.kForward);
    	    	}
    			break;
    		
    		case 8: //Sucks in ball
    			_outerBottomMotor.set(-.75);
    			_outerTopMotor.set(-.75);
    	       	_innerBottomMotor.set(Relay.Value.kReverse);
    	    	_innerTopMotor.set(Relay.Value.kReverse);
    			break;
    		case 0: //Turns everything off
    			 _outerTopMotor.set(0);
    	         _outerBottomMotor.set(0);
    	         _innerBottomMotor.set(Relay.Value.kOff);
    	         _innerTopMotor.set(Relay.Value.kOff); 
    		}
    	}
/*
    		if (_driveStick.getRawButton(5)) {
    	_outerBottomMotor.set(.75);
    	_outerTopMotor.set(.75);
    	}

    	  	
    	
    	if (_driveStick.getRawButton(6)) {
    	_innerBottomMotor.set(Relay.Value.kForward);
    	_innerTopMotor.set(Relay.Value.kForward);
    	}
    	
    	
    	if (_operatorStick.getRawButton(8) && !_driveStick.getRawButton(5) && !_driveStick.getRawButton(6)) {
    	_outerTopMotor.set(-.75);
    	_outerBottomMotor.set(-.75);
       	_innerBottomMotor.set(Relay.Value.kReverse);
    	_innerTopMotor.set(Relay.Value.kReverse);
    	}
    	else {
            _outerTopMotor.set(0);
            _outerBottomMotor.set(0);
          	_innerBottomMotor.set(Relay.Value.kOff);
        	_innerTopMotor.set(Relay.Value.kOff); 
    	}
    		
    	} */
    }
    	  