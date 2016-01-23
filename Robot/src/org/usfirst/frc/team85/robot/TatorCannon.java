package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

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

		_outerTopMotor.changeControlMode(ControlMode.Speed);
		_outerBottomMotor.changeControlMode(ControlMode.Speed);
		_armMotor.changeControlMode(ControlMode.Position);

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder); 		//Native units of 1/4096th of a revolution, so
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder); 	//Speed will be in units of 0.1465rpm
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); //Native units of 1/1024th of full range

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

    private boolean armAtTop() {
		boolean softLimited = _armMotor.isForwardSoftLimitEnabled() && _armMotor.get() >= _armMotor.getForwardSoftLimit();
		return _armMotor.isFwdLimitSwitchClosed() || softLimited;
	}

	private boolean armAtBottom() {
		boolean softLimited = _armMotor.isReverseSoftLimitEnabled() && _armMotor.get() <= _armMotor.getReverseSoftLimit();
		return _armMotor.isRevLimitSwitchClosed() || softLimited;
	}

	private void setRpmTop(double speed) {
		_outerTopMotor.set(speed * 0.1465);
	}

	private void setRpmBottom(double speed) {
		_outerBottomMotor.set(speed * 0.1465);
	}

	private double getRpmTop() {
		_outerTopMotor.get() / 0.1465;
	}

	private double getRpmBottom() {
		_outerBottomMotor.get() / 0.1465;
	}


    //When robot starts up, moves cannon all the way down
    public void armCheck(){
        if (!firstCheck){
            _armMotor.enableForwardSoftLimit(false);
            _armMotor.enableReverseSoftLimit(false);
            if (!armAtBottom()) {
                _armMotor.set (_armMotor.get() - 5);
            } else {
                firstCheck = true;
                _armMotor.setPosition(0);
                _armMotor.enableForwardSoftLimit(true);
                _armMotor.setForwardSoftLimit(0.25); //Tuning required
                _armMotor.enableReverseSoftLimit(true);
                _armMotor.setReverseSoftLimit(0.0);
            }
        }
    }

}
