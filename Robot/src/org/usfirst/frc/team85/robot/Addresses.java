package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.*;

public class Addresses {

        //Controllers
	public class CONTROLLERS {
		public static final int DRIVESTICK = 0;

		public static final int OPERATORSTICK = 1;
	}

        //Drive
	public class DRIVE {
		public static final int LED = 0;
		public static final int BIGLIGHT = 1;
		
		public static final int LEFT_FRONT_MOTOR = 1;
		public static final int LEFT_MID_MOTOR = 2;
		public static final int LEFT_BACK_MOTOR = 3;

		public static final int RIGHT_FRONT_MOTOR = 4;
		public static final int RIGHT_MID_MOTOR = 5;
		public static final int RIGHT_BACK_MOTOR = 6;
	}

    	//Cannon
	public class CANNON {
		
		public static final int OUTER_MOTOR_TOP = 11;
		public static final int OUTER_MOTOR_BOTTOM = 10;

		public static final int INNER_MOTOR_TOP = 13;		//NOT Relay	////////////////////////////////////////
		public static final int INNER_MOTOR_BOTTOM = 14;		//NOT Relay ///////////////////////////////////////

		public static final int DART_MOTOR = 9;

		public static final int DART_TOP_LIMIT = 1;
		public static final int DART_BOTTOM_LIMIT = 2;		
		public static final int BALL_NOT_PRESENT_SENSOR = 4;

		public static final int DART_ENCODER_CH_A = 5;
		public static final int DART_ENCODER_CH_B = 6;
	}

    	//Intake
	public class INTAKE {
		public static final int LOAD_MOTOR = 12; //NOT Relay output number	///////////////////////////

		public static final int LEFT_INTAKE_MOTOR = 7;
		public static final int RIGHT_INTAKE_MOTOR = 8;
		
		public static final int UP_INTAKE_LIMIT = 0;
		public static final int DOWN_INTAKE_LIMIT = 3;
	}

}
