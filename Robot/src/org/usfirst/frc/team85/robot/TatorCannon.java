package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team85.robot.Addresses.*;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TatorCannon {

	private double LOADPOS;		//load pos
	private double FIREPOS;		//fire pos
	private double ARMTOL;		//angle tol

	private double LOADSPEED;	//loading speed

	private double FIRERPM;		//outerMotor speed
	private double RPMTOL;		//outerMotor tol

	private double LIGHT;		//innerMotor speed

	private Boolean firstCheck = false;

	private Joystick _operatorStick;

	private CANTalon _outerTopMotor, _outerBottomMotor,
					_innerTopMotor, _innerBottomMotor, _armMotor;

	private Intake _intake;
	
	private Timer _loadTimer;
	private double _loadTime;
	private boolean _loadInit, _loadComplete;

	public TatorCannon(Joystick operatorStick, Intake intake) {

		_operatorStick = operatorStick;

		_outerTopMotor = new CANTalon(CANNON.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(CANNON.OUTER_MOTOR_BOTTOM);
		_innerTopMotor = new CANTalon(CANNON.INNER_MOTOR_TOP);
		_innerBottomMotor = new CANTalon(CANNON.INNER_MOTOR_BOTTOM);
		_armMotor = new CANTalon(CANNON.ARM_MOTOR);

		_intake = intake;

		_outerTopMotor.changeControlMode(TalonControlMode.Speed);
		_outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		_armMotor.changeControlMode(TalonControlMode.Position);

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 		//Native units of 1/4096th of a revolution, but
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 	//Adjusted units of revolutions
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); //Native units of 1/1024th of full range

		_outerTopMotor.reverseSensor(true); //TODO: set to real value
		_outerBottomMotor.reverseSensor(true);
		_armMotor.reverseSensor(true);
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
		_armMotor.enableLimitSwitch(true, true);

        _outerTopMotor.enableBrakeMode(false);
        _outerBottomMotor.enableBrakeMode(false);
        _innerTopMotor.enableBrakeMode(true); //For precise control of ballfeeding
        _innerBottomMotor.enableBrakeMode(true);
        _armMotor.enableBrakeMode(false);
    }

    public void run() {	//main method
        fire();
        load();
    }
    
    private void fire() {
    	if (_operatorStick.getRawButton(99)) { //Uses button X
    		if ( armMove(FIREPOS)&&
    				(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) &&
    				(Math.abs(_outerTopMotor.get()-FIRERPM) <= RPMTOL) ) {
    			setInner(LIGHT);
    		}
    		setOuter(FIRERPM);
    		setInner(0.0);
    	}  
    }
    
    private boolean load() {
        if(_intake.run(readyToLoad()) && _operatorStick.getRawButton(99) && !_loadComplete) {	
        	// if _intake is trying to load the cannon, wants to load, and not done loading
        	if (!_loadInit) {
        		_loadTimer.reset();
        		_loadTimer.start();
        		_loadInit = true;
        	} else {
        		if (_loadTimer.get() > _loadTime) {
        			setInner(0.0);
        			setOuter(0.0);
            		_loadComplete = true;
        		} else {
        			setInner(LOADSPEED);
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
		_outerTopMotor.set(speed);
		_outerBottomMotor.set(speed);
    }
    
    private void setInner(double speed) {
		_innerTopMotor.set(speed);
		_innerBottomMotor.set(speed);
    }

    public boolean armMove(double target) {
    	if ( Math.abs(_armMotor.get() - target) <= ARMTOL) { //Because we're using a PID loop for positioning,
    		return true;									 //this entire if-block is probably unnecessary
    	}
    	_armMotor.set(target);
    	return false;
    }

}
