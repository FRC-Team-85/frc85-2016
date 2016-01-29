package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TankDrive {

    private Joystick _controller;

    private CANTalon _masterLeftMotor, _slaveLeftMotorA, _slaveLeftMotorB,
    				_masterRightMotor, _slaveRightMotorA, _slaveRightMotorB;

    private Encoder _LeftEncoder, _RightEncoder;

    public TankDrive(Joystick DriveController) {
        _controller = DriveController;

        _masterLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);	//MASTER LEFT
        _masterLeftMotor.enableBrakeMode(false);

        _slaveLeftMotorA = new CANTalon(Addresses.LEFT_MID_MOTOR);
        _slaveLeftMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorA.set(Addresses.LEFT_FRONT_MOTOR);

        _slaveLeftMotorB = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        _slaveLeftMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveLeftMotorB.set(Addresses.LEFT_FRONT_MOTOR);

        _masterRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);	//MASTER RIGHT
        _masterRightMotor.enableBrakeMode(false);

        _slaveRightMotorA = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        _slaveRightMotorA.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorA.set(Addresses.RIGHT_FRONT_MOTOR);

        _slaveRightMotorB = new CANTalon(Addresses.RIGHT_BACK_MOTOR);
        _slaveRightMotorB.changeControlMode(TalonControlMode.Follower);
        _slaveRightMotorB.set(Addresses.RIGHT_FRONT_MOTOR);

        _LeftEncoder = new Encoder(Addresses.LEFT_ENCODER_CH1,
                    Addresses.LEFT_ENCODER_CH2);
        _RightEncoder = new Encoder(Addresses.RIGHT_ENCODER_CH1,
                    Addresses.RIGHT_ENCODER_CH2);
        
        setVoltageRamp(1.0);
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
        double turn = -0.75*Math.sin(0.5*Math.PI*Math.pow(_controller.getZ(), 3));

        double left = thrust + turn;
        double right = thrust - turn;
    	
        setMotors((left + skim(right)), (right + skim(left)));
    }
    
    double skim(double v) {

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
        lSpeed = (Math.abs(lSpeed) <= .2) ? 0.0 : lSpeed;
        rSpeed = (Math.abs(rSpeed) <= .2) ? 0.0 : rSpeed;

        _masterLeftMotor.set(lSpeed);
        _masterRightMotor.set(-rSpeed);
    }

}
