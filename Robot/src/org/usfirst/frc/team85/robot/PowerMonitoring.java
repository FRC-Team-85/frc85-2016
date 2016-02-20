package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PowerMonitoring {
	private static PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public static void Monitor()
	{
		SmartDashboard.putNumber("Current[DriveLeftFront]", pdp.getCurrent(Addresses.DRIVE.LEFT_FRONT_MOTOR));
		SmartDashboard.putNumber("Current[DriveLeftMid]", pdp.getCurrent(Addresses.DRIVE.LEFT_MID_MOTOR));
		SmartDashboard.putNumber("Current[DriveLeftBack]", pdp.getCurrent(Addresses.DRIVE.LEFT_BACK_MOTOR));
		SmartDashboard.putNumber("Current[DriveRightFront]", pdp.getCurrent(Addresses.DRIVE.RIGHT_FRONT_MOTOR));
		SmartDashboard.putNumber("Current[DriveRightMid]", pdp.getCurrent(Addresses.DRIVE.RIGHT_MID_MOTOR));
		SmartDashboard.putNumber("Current[DriveRightBack]", pdp.getCurrent(Addresses.DRIVE.RIGHT_BACK_MOTOR));
		
		SmartDashboard.putNumber("Current[CannonOuterTop]", pdp.getCurrent(Addresses.CANNON.OUTER_MOTOR_TOP));
		SmartDashboard.putNumber("Current[CannonOuterBottom]", pdp.getCurrent(Addresses.CANNON.OUTER_MOTOR_BOTTOM));
		SmartDashboard.putNumber("Current[CannonInnerTop]", pdp.getCurrent(Addresses.CANNON.INNER_MOTOR_TOP));
		SmartDashboard.putNumber("Current[CannonInnerBottom]", pdp.getCurrent(Addresses.CANNON.INNER_MOTOR_BOTTOM));
		SmartDashboard.putNumber("Current[CannonArm]", pdp.getCurrent(Addresses.CANNON.ARM_MOTOR));
		
		SmartDashboard.putNumber("Current[IntakeLoad]", pdp.getCurrent(Addresses.INTAKE.LOAD_MOTOR));
		SmartDashboard.putNumber("Current[IntakeLeft]", pdp.getCurrent(Addresses.INTAKE.LEFT_INTAKE_MOTOR));
		SmartDashboard.putNumber("Current[IntakeRight]", pdp.getCurrent(Addresses.INTAKE.RIGHT_INTAKE_MOTOR));
		
}
}
