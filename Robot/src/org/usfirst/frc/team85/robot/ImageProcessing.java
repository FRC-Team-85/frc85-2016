package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	NetworkTable _table;
	CameraServer _server;
	SmartDashboard _dashboard;
	
	private double area, height, width, centerX, centerY;
	
	public ImageProcessing(NetworkTable table, CameraServer server, SmartDashboard dashboard) {
		_table = table;
		_server = server;
		_dashboard = dashboard;

	}
	
	public void process() {

		height = find("height");
		width = find("width");
		centerX = find("centerX");
		centerY = find("centerY");
    	
	}
	
	private double find(String key) {
		try {
			double num = _table.getNumber(key, 0);
			if (num > 0) {
				System.out.println(key + num);
				return num;
			} else {
				System.out.println("No Contour Found");
				return 0;
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return 0;
	}
	
	/*
	method that returns targeting angle change for drive
	 */
}
