package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	public ImageProcessing() {
	
	int d; //actual distance away from target, inches
	int w = 20; //width of target, inches
	int f = 0; //focal length of lense on camera (temp value for now)
	int p = 0;//number of pixels, would need to retrieve from GRIP (temp value for now)

	d = (w*f) / p; //find the distance of the target based on the actual width and focal length divided by number of pixels 
	
	}
	/*NetworkTable _table;
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
	}*/
}

