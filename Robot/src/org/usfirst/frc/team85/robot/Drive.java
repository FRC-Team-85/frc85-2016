package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Drive {

    //private RobotDrive _drive;
    
    private Joystick _controller; 
    
    private CANTalon _frontLeftMotor;
    private CANTalon _midLeftMotor;
    private CANTalon _backLeftMotor;
    
    private CANTalon _frontRightMotor;
    private CANTalon _midRightMotor;
    private CANTalon _backRightMotor;

    private Encoder _LeftEncoder;
    private Encoder _RightEncoder;

    public Drive(Joystick drivecontroller) {
        _controller = drivecontroller;
        _frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
        _midLeftMotor = new CANTalon(Addresses.LEFT_MID_MOTOR);
        _backLeftMotor = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        _frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
        _midRightMotor = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        _backRightMotor = new CANTalon(Addresses.RIGHT_BACK_MOTOR);

        _LeftEncoder = new Encoder(Addresses.LEFT_ENCODER_CHANNEL_A,
                    Addresses.LEFT_ENCODER_CHANNEL_B);
        _RightEncoder = new Encoder(Addresses.RIGHT_ENCODER_CHANNEL_A,
                    Addresses.RIGHT_ENCODER_CHANNEL_B);

        //_drive = new RobotDrive(_frontLeftMotor, _midLeftMotor, _backLeftMotor, _frontRightMotor, _midRightMotor, _backRightMotor);
    }
    
    public void drive() {
        double controllerL = _controller.getRawAxis(1);
        double controllerR = _controller.getRawAxis(4);
        // // Adjustment of values
        setMotors(controllerL, controllerR);
    }
    
    private void setMotors(double lSpeed, double rSpeed) {
        _frontLeftMotor.set(lSpeed);
        _midLeftMotor.set(lSpeed);
        _backLeftMotor.set(lSpeed);
        
        _frontRightMotor.set(rSpeed);
        _midRightMotor.set(rSpeed);
        _backRightMotor.set(rSpeed);
    }
}
