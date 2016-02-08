package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
//You're trash -Craig Smith
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team85.robot.Addresses.*;

public class TankDrive {

    private Joystick _controller;

    private CANTalon _masterLeftMotor, _slaveLeftMotorA, _slaveLeftMotorB,
    				_masterRightMotor, _slaveRightMotorA, _slaveRightMotorB;

    private Encoder _LeftEncoder, _RightEncoder;

    public TankDrive(Joystick DriveController) {
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

        _LeftEncoder = new Encoder(DRIVE.LEFT_ENCODER_CH1,
                    DRIVE.LEFT_ENCODER_CH2);
        _RightEncoder = new Encoder(DRIVE.RIGHT_ENCODER_CH1,
                    DRIVE.RIGHT_ENCODER_CH2);

        setVoltageRamp(0.5);
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

    public void drive() {
        double thrust = _controller.getY();
        double turn;
        double turnControl = _controller.getZ();
        double turnControlCubed = turnControl * turnControl * turnControl; //Faster than Math.pow
                                                                           //Could be made faster

        if (_controller.getRawButton(7) && _controller.getRawButton(8)) {
        	turn = -.375 * turnControlCubed; // -0.375x^3
        } else {
        	turn = -1.15 * turnControlCubed +
                0.38 * (turnControlCubed * turnControlCubed * turnControlCubed); //Brian's dumb curve, fastified (-1.15x^3+0.38x^9)
    	}

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
        SmartDashboard.putNumber("Lspeed.set(): ", _masterLeftMotor.get());
        SmartDashboard.putNumber("Rspeed.get(): ", rSpeed);
        SmartDashboard.putNumber("Rspeed.set(): ", _masterRightMotor.get());
    }
    
    public double getLeftAvg() {
    	return (_masterLeftMotor.get() +
    			_slaveLeftMotorA.get() +
                _slaveLeftMotorB.get())/3;
    }
    
    public double getRightAvg() {
    	return (_masterRightMotor.get() +
                _slaveRightMotorA.get() +
                _slaveRightMotorB.get())/3;
    }

}
