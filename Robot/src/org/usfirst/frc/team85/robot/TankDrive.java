package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TankDrive {
	
    private Joystick _controllerLeft, _controllerRight;
    
    private CANTalon _masterLeftMotor, _slaveLeftMotorA, _slaveLeftMotorB,
    				_masterRightMotor, _slaveRightMotorA, _slaveRightMotorB;

    private Encoder _LeftEncoder, _RightEncoder;

    public TankDrive(Joystick leftDriveController, Joystick rightDriveController) {
        _controllerLeft = leftDriveController;
        _controllerRight = rightDriveController;
        
        _masterLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
        _masterLeftMotor.enableBrakeMode(false);
        
        _slaveLeftMotorA = new CANTalon(Addresses.LEFT_MID_MOTOR);
        _slaveLeftMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorA.set(Addresses.LEFT_FRONT_MOTOR);
        _slaveLeftMotorA.enableBrakeMode(false);
        
        _slaveLeftMotorB = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        _slaveLeftMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorB.set(Addresses.LEFT_FRONT_MOTOR);
        _slaveLeftMotorB.enableBrakeMode(false);
        
        _masterRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
        _masterRightMotor.enableBrakeMode(false);
        
        _slaveRightMotorA = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        _slaveRightMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorA.set(Addresses.RIGHT_FRONT_MOTOR);
        _slaveRightMotorA.enableBrakeMode(false);
        
        _slaveRightMotorB = new CANTalon(Addresses.RIGHT_BACK_MOTOR);
        _slaveRightMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorB.set(Addresses.RIGHT_FRONT_MOTOR);
        _slaveRightMotorB.enableBrakeMode(false);
        
        _LeftEncoder = new Encoder(Addresses.LEFT_ENCODER_CH1,
                    Addresses.LEFT_ENCODER_CH2);
        _RightEncoder = new Encoder(Addresses.RIGHT_ENCODER_CH1,
                    Addresses.RIGHT_ENCODER_CH2);
    }
    
    public void drive() {
        double controllerL = _controllerLeft.getY();
        double controllerR = _controllerRight.getY();
        
        controllerL = _controllerLeft.getRawButton(1) ? controllerL / 2 : controllerL;
        controllerR = _controllerRight.getRawButton(1) ? controllerR / 2 : controllerR;
        
        if (_controllerLeft.getRawButton(1)) {
        	controllerL *= 0.5;
        }
       	
        setMotors(controllerL, controllerR);
    }
    
    private void setMotors(double lSpeed, double rSpeed) {
        lSpeed = (Math.abs(lSpeed) <= .2) ? 0.0 : lSpeed;
        rSpeed = (Math.abs(rSpeed) <= .2) ? 0.0 : rSpeed;
        
        if (_controllerLeft.getRawButton(3)||_controllerRight.getRawButton(3)) {
        	lSpeed = 0.0;
        	rSpeed = 0.0;
        }
    	
        _masterLeftMotor.set(lSpeed);
        _masterRightMotor.set(rSpeed);
    }
    
}
