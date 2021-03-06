// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight extends SubsystemBase {
  /** Creates a new Limelight. */
  public Limelight() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);


  }

  public static void getDistance() {
    //from documentation, the distance can be found using a fixed camera angle
    //distance = (height2 - height1) / tan(angle1 + angle2)
    //height1 is limelight elevation, height2 is target height
    //angle1 is limelight angle, angle2 is target angle
    //What we need: limelight angle on the robot, distance from center of limelight lens to ground,
    //distance from height of the target to the floor


    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry ts = table.getEntry("ts");

    double targetAngle = ty.getDouble(0.0); //ofset of target
    double offsetX = tx.getDouble(0.0);
    double objectArea = ta.getDouble(0.0);
    double robotSkew = ts.getDouble(0.0);

    double limelightAngle = 80; //Angle the limelight is positioned at
    double limelightElevation = 14; //How far the limelight is above the ground
    double targetHeight = 60; //How tall is the target from above the ground

    //This code gives distance relative from the ground, not to the target.
    double angleDegrees = limelightAngle + targetAngle;
    double angleRadians = angleDegrees * (Math.PI / 180.0);
    double distance = (targetHeight - limelightElevation) / Math.tan(angleRadians);
    SmartDashboard.putNumber("Robot Distance: ", distance);

    //Experimentation to get distance to the target, for example the top of the shipping hub
    //cos theta = adj / hyp, where adj is the distance above, and hypotenuse is the distance we want to find
    //cos theta, where theta is the Target angle, relative to the abgle of the camera
    //So the distance to target = distance / cos theta
    double angleHyp = targetAngle;
    double distanceFromTarget = (distance / Math.cos(angleHyp));
    SmartDashboard.putNumber("Experimental distance: ", distanceFromTarget);


    //double availableTargets = tv.getDouble(0.0); //I would revisit this so the robot does not 
                                                 //run crazy if there are no available targets
    
    SmartDashboard.putNumber("Object Offset X: ", offsetX);
    SmartDashboard.putNumber("Object Offset Y: ", targetAngle);
    SmartDashboard.putNumber("Limelight Area: ", objectArea);
    SmartDashboard.putNumber("Limelight Skew: ", robotSkew);
  }

  @Override
  public void periodic() {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry ts = table.getEntry("ts");
    //NetworkTableEntry tv = table.getEntry("tv");

    double offsetX = tx.getDouble(0.0);
    double offsetY = ty.getDouble(0.0);
    double objectArea = ta.getDouble(0.0);
    double robotSkew = ts.getDouble(0.0);
    //double availableTargets = tv.getDouble(0.0); //I would revisit this so the robot does not 
                                                 //run crazy if there are no available targets
    
    SmartDashboard.putNumber("Object Offset X: ", offsetX);
    SmartDashboard.putNumber("Object Offset Y: ", offsetY);
    SmartDashboard.putNumber("Limelight Area: ", objectArea);
    SmartDashboard.putNumber("Limelight Skew: ", robotSkew);

    getDistance();
  }
}
