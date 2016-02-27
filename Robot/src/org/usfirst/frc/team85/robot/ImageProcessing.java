package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	private NetworkTable _table;
	
	public static boolean contourFound;
	
	public static double area, height, width, centerX, centerY;
	
	public static final int IMGTOL = 10;
	
	public ImageProcessing() {		
		
		//Setup NetworkTables
        try {
        	NetworkTable.setClientMode();
        	NetworkTable.setIPAddress("roborio-85-frc.local");
        } catch(Exception ex) {
        	System.out.println(ex.toString());
        }
	}
	
	public void process() {
		
    	try {
    		_table = NetworkTable.getTable("GRIP/contoursReport");
			area = _table.getNumberArray("area", new double[]{0})[0];
			contourFound = (area>0) ? true : false;
			centerX = _table.getNumberArray("centerX", new double[] {160})[0] - 160;
			centerY = _table.getNumberArray("centerY", new double[] {120})[0] - 120;
/*			if (area>0) {
				System.out.println("Area: " + area);
			} else {
				System.out.println("No Contour Found");
			}
*/
		} catch (Exception ex) {
			System.out.println(ex);
		}
//			area = SmartDashboard.getNumber("GRIP/contoursReport/area", 0);
//			contourFound = (area>0) ? true : false;
//			centerX = SmartDashboard.getNumber("GRIP/contoursReport/centerX", 160) - 160;
//			centerY = SmartDashboard.getNumber("GRIP/contoursReport/centerY", 120) - 120;
    	
    	SmartDashboard.putNumber("Vision/Center X", centerX);
    	SmartDashboard.putNumber("Vision/Center Y", centerY);
    	SmartDashboard.putNumber("Vision/Area", area);
    	SmartDashboard.putBoolean("Vision/Target Found", contourFound);
	}
	
	public boolean center(boolean condition) {/*_opStick.getRawButton(), autonomous all ready*/
		if (contourFound && condition) {
			if (centerX < IMGTOL) {
				//move robot (to the right?) until centerX is less than 10  
                                //_drive.setMotors(FORWARD,BACKWARD);
			} else if (centerX > -IMGTOL) {
				//move robot (to the left?) until centerX is more than -10
                                //_drive.setMotors(BACKWARD,FORWARD);
			}
			
			if (centerX > -IMGTOL && centerX < IMGTOL /*and if the ball is in possession*/) {
				//Shoot the ball
				return true;	//In pos
			}
		}
		return false;	//Not in pos
	}
	
	
}

