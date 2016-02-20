package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	private NetworkTable _table;
	
	private boolean contourFound;
	
	public double area, height, width, centerX, centerY;
	
	public ImageProcessing() {		
		System.out.println("TESTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
		
		//Setup NetworkTables
        try {
        	NetworkTable.setClientMode();
        	NetworkTable.setIPAddress("roborio-85-frc.local");
        	_table = NetworkTable.getTable("grip");
        } catch(Exception ex) {
        	System.out.println(ex.toString());
        }
	}
	
	public void process() {
    	try {
			area = _table.getNumber("area", 0);
			contourFound = (area>0) ? true : false;
			centerX = _table.getNumber("centerX", 160) - 160;
			centerY = _table.getNumber("centerX", 120) - 120;
			if (area>0) {
				System.out.println("Area: " + area);
			} else {
				System.out.println("No Contour Found");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
/*
	method that returns targeting angle change for drive
*/
	public void center() {
		if (contourFound /*_opStick.getRawButton()*/) {
			if (centerX < 10) {
				//move robot (to the right?) until centerX is less than 10  
                                //_drive.setMotors(FORWARD,BACKWARD);
			} else if (centerX > -10) {
				//move robot (to the left?) until centerX is more than -10
                                //_drive.setMotors(BACKWARD,FORWARD);
			}
			
			if (centerX > -10 && centerX < 10 /*and if the ball is in possession*/) {
				//Shoot the ball
			}
		}
	}

}

