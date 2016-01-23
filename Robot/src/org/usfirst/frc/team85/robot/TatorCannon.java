package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TatorCannon {
	double armP, armI, armD, armF, shooterP, shooterI, shooterD, shooterF;

    private Boolean firstCheck = false;

    private Joystick _operatorStick;

    private CANTalon _outerTopMotor, _outerBottomMotor,
                    _innerTopMotor, _innerBottomMotor, _armMotor;

    public TatorCannon(Joystick operatorStick) {
		shooterP = 0.0;
		shooterI = 0.0;
		shooterD = 0.0;
		shooterF = 0.0;

		armP = 0.0;
		armI = 0.0;
		armD = 0.0;
		armF = 0.0;

        _operatorStick = operatorStick;

        _outerTopMotor = new CANTalon(Addresses.OUTER_MOTOR_TOP);
        _outerBottomMotor = new CANTalon(Addresses.OUTER_MOTOR_BOTTOM);
        _innerTopMotor = new CANTalon(Addresses.INNER_MOTOR_TOP);
        _innerBottomMotor = new CANTalon(Addresses.INNER_MOTOR_BOTTOM);
        _armMotor = new CANTalon(Addresses.ARM_MOTOR);

        _outerTopMotor.changeControlMode(TalonControlMode.Speed);
        _outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		_armMotor.changeControlMode(TalonControlMode.Position);

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);

		_armMotor.configPotentiometerTurns(1); //TODO: set to real value

		_outerTopMotor.setSensorDirection(true); //TODO: set to real value
		_outerBottomMotor.setSensorDirection(true);
		_armMotor.setSensorDirection(true);

		_outerTopMotor.ConfigNominalOutputVoltage(0.0, -0.0);
		_outerTopMotor.ConfigPeakOutputVoltage(12.0, -12.0);
		_outerBottomMotor.ConfigNominalOutputVoltage(0.0, -0.0);
		_outerBottomMotor.ConfigPeakOutputVoltage(-12.0, 12.0);
		_armMotor.ConfigNominalOutputVoltage(0.0, -0.0);
		_armMotor.ConfigPeakOutputVoltage(12.0, -12.0);

			//Set closed-loop coefficients
		_outerTopMotor.setProfile(0);
		_outerTopMotor.setP(shooterP);
		_outerTopMotor.setI(shooterI);
		_outerTopMotor.setD(shooterD);
		_outerTopMotor.setD(shooterF);

		_outerBottomMotor.setProfile(0);
		_outerBottomMotor.setP(shooterP);
		_outerBottomMotor.setI(shooterI);
		_outerBottomMotor.setD(shooterD);
		_outerBottomMotor.setF(shooterF);

		_armMotor.setProfile(0);
		_armMotor.setP(armP);
		_armMotor.setI(armI);
		_armMotor.setD(armD);
		_armMotor.setF(armF);

		_armMotor.enableLimitSwitch(true, true);

        _outerTopMotor.enableBrakeMode(false);
        _outerBottomMotor.enableBrakeMode(false);
        _innerTopMotor.enableBrakeMode(false);
        _innerBottomMotor.enableBrakeMode(false);
        _armMotor.enableBrakeMode(false);
    }

    public void run() {
        _operatorStick.getY();
    }

    private void checkLimits() {
        _armLimitTop.get();
        _armLimitBottom.get();
    }

    //When robot starts up, moves cannon all the way down
    public void armCheck(){
        if (!firstCheck){
            _armMotor.enableForwardSoftLimit(false);
            _armMotor.enableReverseSoftLimit(false);
            if (!_armLimitBottom.get()) {
                _armMotor.set (_armMotor.get() - 1);    //Rotations????
            } else {
                firstCheck = true;
                _armMotor.setPosition(0);
                _armMotor.enableForwardSoftLimit(true);
                _armMotor.setForwardSoftLimit(0.25);    //rotations
                _armMotor.enableReverseSoftLimit(true);
                _armMotor.setReverseSoftLimit(0.0);        //rotations
            }
        }
    }

    }
