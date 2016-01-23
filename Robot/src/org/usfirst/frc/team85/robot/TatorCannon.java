package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class TatorCannon {

	private Boolean firstCheck = false;

	private Joystick _operatorStick;

	private CANTalon _outerTopMotor, _outerBottomMotor,
					_innerTopMotor, _innerBottomMotor, _armMotor;

	public TatorCannon(Joystick operatorStick) {


		_operatorStick = operatorStick;

		_outerTopMotor = new CANTalon(Addresses.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(Addresses.OUTER_MOTOR_BOTTOM);
		_innerTopMotor = new CANTalon(Addresses.INNER_MOTOR_TOP);
		_innerBottomMotor = new CANTalon(Addresses.INNER_MOTOR_BOTTOM);
		_armMotor = new CANTalon(Addresses.ARM_MOTOR);

		_outerTopMotor.changeControlMode(TalonControlMode.Speed);
		_outerBottomMotor.changeControlMode(TalonControlMode.Speed);
		_armMotor.changeControlMode(TalonControlMode.Position);

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 		//Native units of 1/4096th of a revolution, but
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative); 	//Adjusted units of revolutions
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); //Native units of 1/1024th of full range

		_outerTopMotor.reverseSensor(true); //TODO: set to real value
		_outerBottomMotor.reverseSensor(true);
		_armMotor.reverseSensor(true);

		_outerTopMotor.setP(Constants.shooterP);
		_outerTopMotor.setI(Constants.shooterI);
		_outerTopMotor.setD(Constants.shooterD);
		_outerTopMotor.setF(Constants.shooterF);

		_outerBottomMotor.setP(Constants.shooterP);
		_outerBottomMotor.setI(Constants.shooterI);
		_outerBottomMotor.setD(Constants.shooterD);
		_outerBottomMotor.setF(Constants.shooterF);

		_armMotor.setP(Constants.armP);
		_armMotor.setI(Constants.armI);
		_armMotor.setD(Constants.armD);
		_armMotor.setF(Constants.armF);

		_armMotor.enableLimitSwitch(true, true);

        _outerTopMotor.enableBrakeMode(false);
        _outerBottomMotor.enableBrakeMode(false);
        _innerTopMotor.enableBrakeMode(true); //For precise control of ballfeeding
        _innerBottomMotor.enableBrakeMode(true);
        _armMotor.enableBrakeMode(false);
    }

    public void run() {
        _operatorStick.getY();
    }

    private boolean armAtTop() {
		boolean softLimited = _armMotor.isForwardSoftLimitEnabled() &&
							_armMotor.get() >= _armMotor.getForwardSoftLimit();
		return _armMotor.isFwdLimitSwitchClosed() || softLimited;
	}

	private boolean armAtBottom() {
		boolean softLimited = _armMotor.isReverseSoftLimitEnabled() &&
							_armMotor.get() <= _armMotor.getReverseSoftLimit();
		return _armMotor.isRevLimitSwitchClosed() || softLimited;
	}

    //When robot starts up, moves cannon all the way down
    public void armCheck(){
        if (!firstCheck){
            _armMotor.enableForwardSoftLimit(false);
            _armMotor.enableReverseSoftLimit(false);
            if (!armAtBottom()) {
                _armMotor.set (_armMotor.get() - 0.01);
            } else {
                firstCheck = true;
                _armMotor.setPosition(0);
                _armMotor.enableForwardSoftLimit(true);
                _armMotor.setForwardSoftLimit(0.25); //Tuning required (rotations (probably))
                _armMotor.enableReverseSoftLimit(true);
                _armMotor.setReverseSoftLimit(0.0); //Rotations (probably)
            }
        }
    }

}
