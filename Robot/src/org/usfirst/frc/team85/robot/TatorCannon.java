package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class TatorCannon {
	
	private Joystick _operatorStick;
	
	private CANTalon _outerTopMotor, _outerBottomMotor,
					_innerTopMotor, _innerBottomMotor, _heightMotor;
	
	private Encoder _tatorCannonEncoderTop, _tatorCannonEncoderBottom;
	
	private AnalogInput _cannonPOT;
	
	private DigitalInput _armLimitTop, _armLimitBottom;

	public TatorCannon(Joystick operatorStick) {
		_operatorStick = operatorStick;
		
		_outerTopMotor = new CANTalon(Addresses.OUTER_MOTOR_TOP);
		_outerBottomMotor = new CANTalon(Addresses.OUTER_MOTOR_BOTTOM);
		_innerTopMotor = new CANTalon(Addresses.INNER_MOTOR_TOP);
		_innerBottomMotor = new CANTalon(Addresses.INNER_MOTOR_BOTTOM);
		_heightMotor = new CANTalon(Addresses.ARM_MOTOR);
		
		_tatorCannonEncoderTop = new Encoder(Addresses.CANNON_ENCODER_TOP_CH1,
                Addresses.CANNON_ENCODER_TOP_CH2);
		_tatorCannonEncoderBottom = new Encoder(Addresses.CANNON_ENCODER_BOTTOM_CH1,
                Addresses.CANNON_ENCODER_BOTTOM_CH2);
		
		_cannonPOT = new AnalogInput(Addresses.CANNON_POT);
		
		_armLimitTop = new DigitalInput(Addresses.ARM_LIMIT_TOP);
		_armLimitBottom = new DigitalInput(Addresses.ARM_LIMIT_BOTTOM);
	}

	




















}
