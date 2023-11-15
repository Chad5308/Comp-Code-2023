// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.HashMap;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {



    public static final double kTrackWidth = Units.inchesToMeters(22.5);
    // Distance between right and left wheels
    public static final double kWheelBase = Units.inchesToMeters(23.75);
    // Distance between front and back wheels


public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
     
    new Translation2d(kWheelBase / 2, -kTrackWidth / 2), //front left
      new Translation2d(kWheelBase / 2, kTrackWidth / 2), //front right
      new Translation2d(-kWheelBase / 2, -kTrackWidth / 2), //back left
      new Translation2d(-kWheelBase / 2, kTrackWidth / 2)); //back right



    public static final int kFrontLeftDriveMotorPort = 3;
    public static final int kBackLeftDriveMotorPort = 11;
    public static final int kFrontRightDriveMotorPort = 6;
    public static final int kBackRightDriveMotorPort = 9;

    public static final int kFrontLeftTurningMotorPort = 2;
    public static final int kBackLeftTurningMotorPort = 12;
    public static final int kFrontRightTurningMotorPort = 7;
    public static final int kBackRightTurningMotorPort = 10;

    public static final boolean kFrontLeftTurningEncoderReversed = false;
    public static final boolean kBackLeftTurningEncoderReversed = false;
    public static final boolean kFrontRightTurningEncoderReversed = false;
    public static final boolean kBackRightTurningEncoderReversed = false;

    public static final boolean kFrontLeftDriveEncoderReversed = true;
    public static final boolean kBackLeftDriveEncoderReversed = true;
    public static final boolean kFrontRightDriveEncoderReversed = false;
    public static final boolean kBackRightDriveEncoderReversed = false;

    public static final int kFrontLeftDriveAbsoluteEncoderPort = 4;
    public static final int kBackLeftDriveAbsoluteEncoderPort = 13;
    public static final int kFrontRightDriveAbsoluteEncoderPort = 5;
    public static final int kBackRightDriveAbsoluteEncoderPort = 8;

    public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = false;
    public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
    public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
    public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;

/*  public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -191.15;
    public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = -3.22;
    public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = 0.61;
    public static final double kBackRightDriveAbsoluteEncoderOffsetRad = -4.28;
*/

    public static final double kBRDegrees = 9.405; //332.31
    public static final double kBLDegrees = 294.875; //295.92
    public static final double kFLDegrees = 68.82; //72.85
    public static final double kFRDegrees = 242.93; //278.31

/* 
    public static final double kBRDegrees = kFRDriveAbsoluteEncoderOffsetRad *180 / Math.PI;
    public static final double kBLDegrees = kFLDriveAbsoluteEncoderOffsetRad *180 / Math.PI;
    public static final double kFLDegrees = kBLDriveAbsoluteEncoderOffsetRad *180 / Math.PI;
    public static final double kFRDegrees = kBRDriveAbsoluteEncoderOffsetRad *180 / Math.PI;
    */
 

    public static final double kPhysicalMaxSpeedMetersPerSecond = 4.4196;
    public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = 2 * 2 * Math.PI;


    //4 is medium, 2.75 is pretty fast, >2 is to fast. <4 is to slow
    public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond / 1;
    public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond / 1.14;
    public static final double kTeleDriveMaxAccelerationUnitsPerSecond = 3;
    public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = 2;
}
  
  public static final class ModuleConstants {
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4);
    public static final double kDriveMotorGearRatio = 7 / 1;
    public static final double kTurningMotorGearRatio = 12.8 / 1;
    public static final double kDriveEncoderRot2Meter = 1/23.58;
    
    
    //get this a little closer. maybe 28.23
    public static final double kTurningConversionFactor2Deg =  28.25;
    public static final double kDriveEncoderRPM2MeterPerSec = kDriveEncoderRot2Meter / 60;
    public static final double kTurningEncoderRPM2DegPerSec = kTurningConversionFactor2Deg / 60;
    public static final double kPTurning = 0.01; //test a higher value 0.025 is to high
    public static final double kITurning = 0.00015;
    public static final double kDTurning = 0.05;
}

//Max speed
    public static final class OIConstants {
      public static final int kDriverControllerPort = 1;
      public static final int kOPControllerPort = 1;
      public static final int kPulleyStopPort = 3;
      public static final int kliftController = 2;
      public static final int kDriverStickPort = 0;
      public static final int kThetaStickPort = 4;

      public static final int kDriverYAxis = 1;
      public static final int kDriverXAxis = 0;
      public static final int kDriverRotAxis = 4;
      public static final int kDriverFieldOrientedButtonIdx = 1;

      public static final double kDeadband = 0.09;
  }
  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = DriveConstants.kPhysicalMaxSpeedMetersPerSecond / 2.75; //1.75
    public static final double kMaxAngularSpeedRadiansPerSecond =  DriveConstants.kPhysicalMaxAngularSpeedRadiansPerSecond / 2.75; //1.75
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularAccelerationUnitsPerSecond = 3;
    public static final double kPXController = 0.025;
    public static final double kPYController = 0.025;
    public static final double kPThetaController = 0.025;

    public static final HashMap<String, Command> AUTO_EVENT_MAP = new HashMap<>();

    //public static final Trajector

    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = 
            new TrapezoidProfile.Constraints(
                    kMaxAngularSpeedRadiansPerSecond,
                    kMaxAngularAccelerationUnitsPerSecond);
  }


 
  //Pneumatics Constants
  public static final class PneumaticsConstants {
    //Intake numbers 5+1
     public static final int L_INTAKE_IN = 7;
     public static final int L_INTAKE_OUT = 2; //correct //1
  
    //Extension number 7+2
 public static final int EXTENSION_IN = 1;
 public static final int EXTENSION_OUT = 6;
 
//  public static final int FLIPPER_IN = 0;
//  public static final int FLIPPER_OUT = 6;
 
   }
   public static final class PullyConstants {
 
     public static final int pulleyMotorNum = 14;
     //public static final int extendSwitchstatePort = 1;
     public static final int retractSwitchstatePort = 9;
     public static final double pulleyPIDControllerPVal = 0.1;
     public static final double pulleyEncoder2deg = 28.25;
     }

     public static final class ForkLiftConstants {
      public static final int forkLiftMotorNum = 16;
     }
}
