package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Drive {
    private Joystick _controllerLeft; 
    private Joystick _controllerRight;
    
    private CANTalon _frontLeftMotor;
    private CANTalon _midLeftMotor;
    private CANTalon _backLeftMotor;
    
    private CANTalon _frontRightMotor;
    private CANTalon _midRightMotor;
    private CANTalon _backRightMotor;

    private Encoder _LeftEncoder;
    private Encoder _RightEncoder;

    public Drive(Joystick leftDriveController, Joystick rightDriveController) {
        _controllerLeft = leftDriveController;
        _controllerRight = rightDriveController;
        
        _frontLeftMotor = new CANTalon(Addresses.LEFT_FRONT_MOTOR);
        _midLeftMotor = new CANTalon(Addresses.LEFT_MID_MOTOR);
        _backLeftMotor = new CANTalon(Addresses.LEFT_BACK_MOTOR);
        _frontRightMotor = new CANTalon(Addresses.RIGHT_FRONT_MOTOR);
        _midRightMotor = new CANTalon(Addresses.RIGHT_MID_MOTOR);
        _backRightMotor = new CANTalon(Addresses.RIGHT_BACK_MOTOR);

        _LeftEncoder = new Encoder(Addresses.LEFT_ENCODER_CH1,
                    Addresses.LEFT_ENCODER_CH2);
        _RightEncoder = new Encoder(Addresses.RIGHT_ENCODER_CH1,
                    Addresses.RIGHT_ENCODER_CH2);
    }
    
    public void drive() {
        double controllerL = _controllerLeft.getRawAxis(2);
        double controllerR = _controllerRight.getRawAxis(2);
        
        boolean lTrigger = _controllerLeft.getRawButton(1);
        boolean rTrigger = _controllerLeft.getRawButton(1);
        
        //Dead-bands of +/- 0.2 for both controllers
        if (!lTrigger && controllerL <= .2 && controllerL >= -.2) {
        	controllerL = 0;
        }
        if (!rTrigger && controllerR <= .2 && controllerR >= -.2) {
        	controllerR = 0;
        }
        
        //Halve speeds if corresponding trigger is pressed
        double adjustedL = lTrigger ? controllerL / 2 : controllerL;
        double adjustedR = rTrigger ? controllerR / 2 : controllerR;
        
        //Negative because we are using motors made of anti-matter
        setMotors(-adjustedL, -adjustedR);
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
