package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team85.robot.Addresses.*;

public class TankDrive {
	
    private Joystick _controller;

    private Relay greenLED;
    private Relay bigLED;

    private CANTalon _masterLeftMotor, _slaveLeftMotorA, _slaveLeftMotorB,
    				_masterRightMotor, _slaveRightMotorA, _slaveRightMotorB;

    private boolean LEDToggle, LEDToggled;
    private boolean bigLEDToggle, bigLEDToggled;
    
    public TankDrive(Joystick DriveController) {
   	 greenLED = new Relay(DRIVE.LED, Relay.Direction.kForward);
	 bigLED = new Relay(DRIVE.BIGLIGHT, Relay.Direction.kForward);
    	
        _controller = DriveController;
        _masterLeftMotor = new CANTalon(DRIVE.LEFT_FRONT_MOTOR);	//MASTER LEFT
        _masterLeftMotor.enableBrakeMode(false);

        _slaveLeftMotorA = new CANTalon(DRIVE.LEFT_MID_MOTOR);
        _slaveLeftMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorA.set(DRIVE.LEFT_FRONT_MOTOR);

        _slaveLeftMotorB = new CANTalon(DRIVE.LEFT_BACK_MOTOR);
        _slaveLeftMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorB.set(DRIVE.LEFT_FRONT_MOTOR);

        _masterRightMotor = new CANTalon(DRIVE.RIGHT_FRONT_MOTOR);	//MASTER RIGHT
        _masterRightMotor.enableBrakeMode(false);

        _slaveRightMotorA = new CANTalon(DRIVE.RIGHT_MID_MOTOR);
        _slaveRightMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorA.set(DRIVE.RIGHT_FRONT_MOTOR);

        _slaveRightMotorB = new CANTalon(DRIVE.RIGHT_BACK_MOTOR);
        _slaveRightMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorB.set(DRIVE.RIGHT_FRONT_MOTOR);

        //setVoltageRamp(0.5);
        System.out.println("TankDrive Init Done");
        
    }

	public void setVoltageRamp(double rate) {
		setVoltageRamp(rate, _masterLeftMotor, _slaveLeftMotorA,
                            _slaveLeftMotorB, _masterRightMotor,
                            _slaveRightMotorA, _slaveRightMotorB);
	}

    private void setVoltageRamp(double rate, CANTalon... motors) {
        for (CANTalon motor : motors) {
            motor.setVoltageRampRate(rate);
        }
    }

    public void setBrakeMode(boolean enabled) {
        setBrakeMode(enabled, _masterLeftMotor, _slaveLeftMotorA,
                            _slaveLeftMotorB, _masterRightMotor,
                            _slaveRightMotorA, _slaveRightMotorB);
    }

    private void setBrakeMode(boolean enabled, CANTalon... motors) {
        for (CANTalon motor : motors) {
            motor.enableBrakeMode(enabled);
        }
    }
    
    private boolean centered;

    public void manualDrive() {
    	setVoltageRamp(0);
        double thrust = _controller.getY();
        double turn;
        double turnControl = _controller.getZ();
        double turnControlCubed = turnControl * turnControl * turnControl; //Faster than Math.pow, could be faster
                                                                           
        turn = -1.15 * turnControlCubed +
                0.38 * (turnControlCubed * turnControlCubed * turnControlCubed); //Brian's dumb curve, fastified 0.8(-1.15x^3+0.38x^9)
        
    	if (_controller.getRawButton(5) && _controller.getRawButton(6)) {
      //  	thrust *= 1;
       // 	turn *= 1;
        } else if (_controller.getRawButton(5)) {
        	//"Quick Turn"
        	thrust *= .5;
       // 	turn *= 1;
        } else if (_controller.getRawButton(6)) {
        	//Slow speed for going over defenses
        	thrust *= 0.5;
        	turn *= 0.5;
        } else {
       // 	thrust *= 1;
        	turn *= 0.8;
        }
        
        if (_controller.getRawButton(10)) {	
        	if (!LEDToggled){
        		LEDToggle = !LEDToggle;
        		LEDToggled = true;
        	}
        } else {
        	LEDToggled = false;
        }
        ledToggle(LEDToggle);

        if (_controller.getRawButton(4)) {	
        	if (!bigLEDToggled){
        		bigLEDToggle = !bigLEDToggle;
        		bigLEDToggled = true;
        	}
        } else {
        	bigLEDToggled = false;
        }
        bigLedToggle(bigLEDToggle);


        double left = thrust + turn;
        double right = thrust - turn;

        setMotors(left + skim(right), right + skim(left));
            
    }

    double skim(double v) { // Sets adjustments to the right side if the left
                            // motor is at maximum and vice versa
    	  if (v > 1.0) {
    		  return -(v - 1.0);
    	  } else if (v < -1.0) {
    		  return -(v + 1.0);
    	  } else {
    		  return 0.0;
    	  }
    }

    //public for use in auto
    public void setMotors(double lSpeed, double rSpeed) {

    	//deadbands
        lSpeed = (Math.abs(lSpeed) <= 0.2) ? 0.0 : lSpeed;
        rSpeed = (Math.abs(rSpeed) <= 0.2) ? 0.0 : rSpeed;

        _masterLeftMotor.set(lSpeed);
        _masterRightMotor.set(-rSpeed);
        SmartDashboard.putNumber("Lspeed.get(): ", lSpeed);
        SmartDashboard.putNumber("Lspeed.set(): ", getLeftSpeed());
        SmartDashboard.putNumber("Rspeed.get(): ", -rSpeed);
        SmartDashboard.putNumber("Rspeed.set(): ", -getRightSpeed());
    }

    public double getLeftSpeed() {
    	return _masterLeftMotor.get();
    }

    public double getRightSpeed() {
    	return _masterRightMotor.get();
    }
    
    public void ledToggle(boolean on) {
    	if (on) {
        	greenLED.set(Relay.Value.kForward);
       	} else {
       		greenLED.set(Relay.Value.kOff);
       	}
    }
    
    public void bigLedToggle(boolean on) {
    	if (on) {
        	bigLED.set(Relay.Value.kForward);
       	} else {
       		bigLED.set(Relay.Value.kOff);
       	}
    }
    
    double previousError = 0;
    double Kp = 0.0005; //  1/160;
    double Kd = 0.020;
    double Ki = 0.;
    double maxPower = 0.95, minPower = 0.35;//.95,.35
    
    public void initSafeCoding(){
    	SmartDashboard.putNumber("ZZZ drive Kp", Kp);
    	SmartDashboard.putNumber("ZZZ drive Kd", Kd);
    	SmartDashboard.putNumber("ZZZ drive maxPower", maxPower);
    	SmartDashboard.putNumber("ZZZ drive minPower", minPower);
    }
    
    public void muchSafeCoding(){
    	//Adjust with FX java on programming computer, along side Driver Station
    	Kp = SmartDashboard.getNumber("ZZZ drive Kp", Kp);
    	Kd = SmartDashboard.getNumber("ZZZ drive Kd", Kd);
    	maxPower = SmartDashboard.getNumber("ZZZ drive maxPower", maxPower);
    	minPower = SmartDashboard.getNumber("ZZZ drive minPower", minPower);
    }
    
    public boolean visionCenter() {
    	ledToggle(true);    	
    	
    	this.setVoltageRamp(16);
    	
    	double error = ImageProcessing.xPixelsToTarget();
    	double changeInError = error - previousError;
    	previousError = error;
    	
    	if (ImageProcessing.isVisionGone()){
    		setMotors(0, 0);
    		return false;
    	}
    	   	
    	if (ImageProcessing.withinXTolerance()) {
    		setMotors(0, 0);
    		SmartDashboard.putBoolean("ALIGNED", true);
    		return true;
    	}
    	
    	double power = Kp * error + Kd * changeInError;
    	System.out.println(error + " " + power);
    	    	
    	if (Math.abs(power) > maxPower) {
    		if (power > 0) power = maxPower;
    		else power = -maxPower;
    	}
    	if (Math.abs(power) < minPower) {
    		if (power > 0) power = minPower;
    		else power = -minPower;
    	}
    	setMotors(power, -power);
		SmartDashboard.putBoolean("ALIGNED", false);
    	return false;
    }    
}

