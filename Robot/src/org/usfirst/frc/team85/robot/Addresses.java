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

		public static final int LEFT_ENCODER_CH1 = 0;
		public static final int LEFT_ENCODER_CH2 = 1;

		public static final int RIGHT_ENCODER_CH1 = 2;
		public static final int RIGHT_ENCODER_CH2 = 3;
	}

    	//Cannon
	public class CANNON {
		public static final int OUTER_MOTOR_TOP = 7;
		public static final int OUTER_MOTOR_BOTTOM = 8;

		public static final int INNER_MOTOR_TOP = 9;
		public static final int INNER_MOTOR_BOTTOM = 10;

		public static final int ARM_MOTOR = 11;
	}

    	//Intake
	public class INTAKE {
		public static final int INTAKE_LOAD_MOTOR = 12;

		public static final int INTAKE_ANGLE_MOTOR = 13;

    	//Another analog for intake position
    
		public static final int INTAKE_POT = 1;
	}

}
