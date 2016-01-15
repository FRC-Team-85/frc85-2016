package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Drive {

    private RobotDrive _drive;
    private Joystick _controller;
    private CANTalon _frontLeftMotor;
    private CANTalon _frontRightMotor;
    private CANTalon _midLeftMotor;
    private CANTalon _midRightMotor;
    private CANTalon _backLeftMotor;
    private CANTalon _backRightMotor;

    private Encoder _frontLeftEncoder;
    private Encoder _frontRightEncoder;
    private Encoder _midLeftEncoder;
    private Encoder _midRightEncoder;
    private Encoder _backLeftEncoder;
    private Encoder _backRightEncoder;

    public Drive(Joystick drivecontroller) {
        _controller = drivecontroller
        _frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
        _frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
        _midLeftMotor = new CANTalon(Addresses.LEFT_MID_MOTOR);
        _midRightMotor = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        _backLeftMotor = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        _backRightMotor = new CANTalon(Addresses.RIGHT_BACK_MOTOR);

        _frontLeftEncoder = new Encoder(Addresses.FRONT_LEFT_ENCODER_CHANNEL_A,
                    Addresses.FRONT_LEFT_ENCODER_CHANNEL_B);
        _frontRightEncoder = new Encoder(Addresses.FRONT_RIGHT_ENCODER_CHANNEL_A,
                    Addresses.FRONT_RIGHT_ENCODER_CHANNEL_B);
        _midLeftEncoder = new Encoder(Addresses.MID_LEFT_ENCODER_CHANNEL_A,
                    Addresses.MID_LEFT_ENCODER_CHANNEL_B);
        _midRightEncoder = new Encoder(Addresses.MID_RIGHT_ENCODER_CHANNEL_B,
                    Addresses.MID_RIGHT_ENCODER_CHANNEL_B);
        _backLeftEncoder = new Encoder(Addresses.BACK_LEFT_ENCODER_CHANNEL_B,
                    Addresses.BACK_LEFT_ENCODER_CHANNEL_B);
        _backRightEncoder = new Encoder(Addresses.BACK_RIGHT_ENCODER_CHANNEL_A,
                    Addresses.BACK_RIGHT_ENCODER_CHANNEL_B);
    }
}
