package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class TatorCannon {

	private Boolean firstCheck = false;

	private Joystick _operatorStick;

	private CANTalon _outerTopMotor, _outerBottomMotor,
					_innerTopMotor, _innerBottomMotor, _armMotor;

	double shooterP = 0.0;
	double shooterI = 0.0;
	double shooterD = 0.0;
	double shooterF = 0.0;

	double armP = 0.0;
	double armI = 0.0;
	double armD = 0.0;
	double armF = 0.0;

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

		_outerTopMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder); 		//Native units of 1/4096th of a revolution, so
		_outerBottomMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder); 	//Adjusted units of revolutions
		_armMotor.setFeedbackDevice(FeedbackDevice.AnalogPot); //Native units of 1/1024th of full range

		_outerTopMotor.reverseSensor(true); //TODO: set to real value
		_outerBottomMotor.reverseSensor(true);
		_armMotor.reverseSensor(true);

		_outerTopMotor.setP(shooterP);
		_outerTopMotor.setI(shooterI);
		_outerTopMotor.setD(shooterD);
		_outerTopMotor.setF(shooterF);

		_outerBottomMotor.setP(shooterP);
		_outerBottomMotor.setI(shooterI);
		_outerBottomMotor.setD(shooterD);
		_outerBottomMotor.setF(shooterF);

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

    private boolean armAtTop() {
		boolean softLimited = _armMotor.isForwardSoftLimitEnabled() && _armMotor.get() >= _armMotor.getForwardSoftLimit();
		return _armMotor.isFwdLimitSwitchClosed() || softLimited;
	}

	private boolean armAtBottom() {
		boolean softLimited = _armMotor.isReverseSoftLimitEnabled() && _armMotor.get() <= _armMotor.getReverseSoftLimit();
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
                _armMotor.setForwardSoftLimit(0.25); //Tuning required (rotations)
                _armMotor.enableReverseSoftLimit(true);
                _armMotor.setReverseSoftLimit(0.0); //Rotations
            }
        }
    }

}
