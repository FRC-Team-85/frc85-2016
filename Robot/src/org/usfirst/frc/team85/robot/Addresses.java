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

		public static final int INNER_MOTOR_TOP = 1;		//Relay
		public static final int INNER_MOTOR_BOTTOM = 2;		//Relay

		public static final int ARM_MOTOR = 9;
		
		public static final int ARM_POT = 0;
		public static final int BALL_NOT_PRESENT_SENSOR = 4;
	}

    	//Intake
	public class INTAKE {
		public static final int LOAD_MOTOR = 3; //TODO: Relay output number

		public static final int LEFT_INTAKE_MOTOR = 7;
		public static final int RIGHT_INTAKE_MOTOR = 8;
	}

}
