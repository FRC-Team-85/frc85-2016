package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class TankDrive {
	
    private Joystick _controllerLeft, _controllerRight;
    
    private CANTalon _frontLeftMotor, _midLeftMotor, _backLeftMotor,
    				_frontRightMotor, _midRightMotor, _backRightMotor;

    private Encoder _LeftEncoder, _RightEncoder;

    public TankDrive(Joystick leftDriveController, Joystick rightDriveController) {
        _controllerLeft = leftDriveController;
        _controllerRight = rightDriveController;
        
        _frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
        _midLeftMotor = new CANTalon(Addresses.LEFT_MID_MOTOR);
        	_midLeftMotor.changeControlMode(ControlMode.Follower);
        	_midLeftMotor.set(_frontLeftMotor.getDeviceID());
        _backLeftMotor = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        	_backLeftMotor.changeControlMode(ControlMode.Follower);
        	_backLeftMotor.set(_frontLeftMotor.getDeviceID());
        _frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
        _midRightMotor = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        	_midRightMotor.changeControlMode(ControlMode.Follower);
        	_midRightMotor.set(_frontLeftMotor.getDeviceID());
        _backRightMotor = new CANTalon(Addresses.RIGHT_BACK_MOTOR);
        	_backRightMotor.changeControlMode(ControlMode.Follower);
        	_backRightMotor.set(_frontLeftMotor.getDeviceID());

        _LeftEncoder = new Encoder(Addresses.LEFT_ENCODER_CH1,
                    Addresses.LEFT_ENCODER_CH2);
        _RightEncoder = new Encoder(Addresses.RIGHT_ENCODER_CH1,
                    Addresses.RIGHT_ENCODER_CH2);
    }
    
    public void drive() {
        double controllerL = _controllerLeft.getRawAxis(2);
        double controllerR = _controllerRight.getRawAxis(2);
        
        //Halve speeds if corresponding trigger is pressed
        controllerL = _controllerLeft.getRawButton(1) ? controllerL / 2 : controllerL;
        controllerR = _controllerRight.getRawButton(1) ? controllerR / 2 : controllerR;
       	
        //Negative because we are using motors made of anti-matter
        setMotors(-controllerL, -controllerR);
    }
    
    private void setMotors(double lSpeed, double rSpeed) {
        lSpeed = (Math.abs(lSpeed) <= .2) ? 0.0 : lSpeed;
        rSpeed = (Math.abs(rSpeed) <= .2) ? 0.0 : rSpeed;
    	
        _frontLeftMotor.set(lSpeed);//_midLeftMotor.set(lSpeed);//_backLeftMotor.set(lSpeed);
        _frontRightMotor.set(rSpeed);//_midRightMotor.set(rSpeed);//_backRightMotor.set(rSpeed);
    }
    
    private void setBrakeMode(boolean breakMode){
    	_frontLeftMotor.enableBrakeMode(breakMode);//_midLeftMotor.enableBrakeMode(breakMode);//_backLeftMotor.enableBrakeMode(breakMode);
        _frontRightMotor.enableBrakeMode(breakMode);//_midRightMotor.enableBrakeMode(breakMode);//_backRightMotor.enableBrakeMode(breakMode);
    }
    
}
