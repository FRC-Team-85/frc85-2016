package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PowerMonitoring {
	private static PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	public static void Monitor()
	{
		SmartDashboard.putNumber("Current[DriveLeftFront]", pdp.getCurrent(0));
		SmartDashboard.putNumber("Current[DriveLeftMid]", pdp.getCurrent(1));
		SmartDashboard.putNumber("Current[DriveLeftBack]", pdp.getCurrent(2));
		SmartDashboard.putNumber("Current[DriveRightFront]", pdp.getCurrent(15));
		SmartDashboard.putNumber("Current[DriveRightMid]", pdp.getCurrent(14));
		SmartDashboard.putNumber("Current[DriveRightBack]", pdp.getCurrent(13));
		
		SmartDashboard.putNumber("Current[CannonShooter1]", pdp.getCurrent(3));
		SmartDashboard.putNumber("Current[CannonShooter2]", pdp.getCurrent(12));
		SmartDashboard.putNumber("Current[CannonIndexerTop]", pdp.getCurrent(8));
		SmartDashboard.putNumber("Current[CannonIndexerBottom]", pdp.getCurrent(9));
		SmartDashboard.putNumber("Current[CannonArm]", pdp.getCurrent(4));
		
		SmartDashboard.putNumber("Current[IntakeLoad]", pdp.getCurrent(10));
		SmartDashboard.putNumber("Current[IntakeLeft]", pdp.getCurrent(11));
		SmartDashboard.putNumber("Current[IntakeRight]", pdp.getCurrent(5));
		
}
}
