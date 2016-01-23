package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TatorCannon {

	private Boolean firstCheck = false;

	private Joystick _operatorStick;

	private CANTalon _outerTopMotor, _outerBottomMotor,
					_innerTopMotor, _innerBottomMotor, _armMotor;

	private Encoder _tatorCannonEncoderTop, _tatorCannonEncoderBottom;

	private AnalogInput _cannonPOT;

	private DigitalInput _armLimitTop, _armLimitBottom;

	public TatorCannon(Joystick operatorStick) {
		_operatorStick = operatorStick;

		_outerTopMotor = new CANTalon(Addresses.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(Addresses.OUTER_MOTOR_BOTTOM);
		_innerTopMotor = new CANTalon(Addresses.INNER_MOTOR_TOP);
		_innerBottomMotor = new CANTalon(Addresses.INNER_MOTOR_BOTTOM);
		_armMotor = new CANTalon(Addresses.ARM_MOTOR);

		_tatorCannonEncoderTop = new Encoder(Addresses.CANNON_ENCODER_TOP_CH1,
                Addresses.CANNON_ENCODER_TOP_CH2);
		_tatorCannonEncoderBottom = new Encoder(Addresses.CANNON_ENCODER_BOTTOM_CH1,
                Addresses.CANNON_ENCODER_BOTTOM_CH2);

		_cannonPOT = new AnalogInput(Addresses.CANNON_POT);

		_armLimitTop = new DigitalInput(Addresses.ARM_LIMIT_TOP);
		_armLimitBottom = new DigitalInput(Addresses.ARM_LIMIT_BOTTOM);

		_outerTopMotor.changeControlMode(TalonControlMode.Speed);
		_outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		_armMotor.changeControlMode(TalonControlMode.Position);

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot);

		_outerTopMotor.reverseSensor(true); //TODO: set to real value
		_outerBottomMotor.reverseSensor(true);
		_armMotor.reverseSensor(true);

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
