package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	NetworkTable _table;
	CameraServer _server;
	SmartDashboard _dashboard;
	
	public ImageProcessing(NetworkTable table, CameraServer server, SmartDashboard dashboard) {
		_table = table;
		_server = server;
		_dashboard = dashboard;

	}
	
	public void process() {

    	try {
			double area = _table.getNumber("area", 0);
			if (area>0) {
				System.out.println("Area: " + area);
			} else {
				System.out.println("No Contour Found");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
