package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;
public class SwerveModule extends SubsystemBase {

  //initalize all variables
    public static CANSparkMax steerMotor;
    public CANSparkMax driveMotor;
    public final SparkMaxPIDController turningPidController;
    private static final double RAMP_RATE = 0.85;//1.5;
    public RelativeEncoder driveMotorEncoder;
    public RelativeEncoder steerMotorEncoder;
    public CANCoder absoluteEncoder;
    private boolean absoluteEncoderReversed;
    public final SparkMaxPIDController drivePidController;



  //New Swerve Module start
  public SwerveModule(int steerNum, int driveNum, boolean invertDrive, boolean invertSteer, int absoluteEncoderId,
  double absoluteEncoderOffsetRad, Boolean absoluteEncoderReversed) {
      //Create and configure a new Drive motor
      driveMotor = new CANSparkMax(driveNum, MotorType.kBrushless);
      driveMotor.restoreFactoryDefaults();
      driveMotor.setInverted(invertDrive);
      driveMotor.setOpenLoopRampRate(RAMP_RATE);
      driveMotor.setIdleMode(IdleMode.kBrake);
      

      
  


    //Create and configure a new steer motor
    steerMotor = new CANSparkMax(steerNum, MotorType.kBrushless);
    steerMotor.restoreFactoryDefaults();
    steerMotor.setInverted(invertSteer);
    steerMotor.setOpenLoopRampRate(RAMP_RATE);
    steerMotor.setIdleMode(IdleMode.kBrake);
    turningPidController = steerMotor.getPIDController();
    drivePidController = driveMotor.getPIDController();

    turningPidController.setP(Constants.ModuleConstants.kPTurning);
    turningPidController.setI(Constants.ModuleConstants.kITurning);
    turningPidController.setD(Constants.ModuleConstants.kITurning);
    
    turningPidController.setPositionPIDWrappingEnabled(true);
    turningPidController.setPositionPIDWrappingMaxInput(1080); 
    turningPidController.setPositionPIDWrappingMinInput(720);

    drivePidController.setP(0.95);
    



    this.absoluteEncoderReversed = absoluteEncoderReversed;
    absoluteEncoder = new CANCoder(absoluteEncoderId);
    
    //Create the built in motor encoders
 
    //Drive motor encoder
    driveMotorEncoder = driveMotor.getEncoder();
    driveMotorEncoder.setPositionConversionFactor( 1/23.58);
    driveMotorEncoder.setVelocityConversionFactor(Constants.ModuleConstants.kDriveEncoderRPM2MeterPerSec);

    //Steer motor encoder 
    steerMotorEncoder = steerMotor.getEncoder();
    steerMotorEncoder.setPositionConversionFactor(Constants.ModuleConstants.kTurningConversionFactor2Deg);
    steerMotorEncoder.setVelocityConversionFactor(Constants.ModuleConstants.kTurningEncoderRPM2DegPerSec);

 //reset encoders after init phase
    resetDrive();
    System.out.println("reset encoders");
  }
  
public static SparkMaxPIDController getPIDController() {
  return steerMotor.getPIDController();
}

//Reset encoder method. Called after init
  
  public void resetDrive() {
   driveMotorEncoder.setPosition(0);
   steerMotorEncoder.setPosition(0);

  }
//stop method that stops the motors when the stick/s are within the deadzone < 0.01
public void stop() {
  driveMotor.set(0);
  steerMotor.set(0);
}

    //configure our Absolute Encoder for the MK4 drive system
  CANCoderConfiguration config = new CANCoderConfiguration();

  //Get the absolute encoder values
  public double getAbsoluteEncoderDeg(double AEOffset) {
    double angle = absoluteEncoder.getAbsolutePosition();
    angle *= 180 / Math.PI;
    return angle  * (absoluteEncoderReversed ? -1 : 1) - AEOffset;
    // return angle  * (absoluteEncoderReversed ? -1 : 1);
  }
  
  //Motor calls
  //Get the Drive values. Value is in degrees.
  public double getDrivePosition() {
    return driveMotorEncoder.getPosition();
  }
  public double getDriveVelocity() {
    return driveMotorEncoder.getVelocity();
  }
    //Get the Steer values. Value is in degrees.
  public double getSteerPosition() {
     return Math.abs(steerMotorEncoder.getPosition() % 360);
  }
  public double getSteerVelocity() {
    return steerMotorEncoder.getVelocity();
  }

  public double getPositionMeters() {
    return driveMotorEncoder.getPosition(); //* 4;
  }
  
 





  
//Creating the current state of the modules. A drive velo and an angle are needed. We use an off set of -90 for the angle
public SwerveModuleState gState() {
    return new SwerveModuleState(getDriveVelocity(), Rotation2d.fromDegrees(steerMotorEncoder.getPosition()));
  }
  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(driveMotorEncoder.getPosition(), Rotation2d.fromDegrees(steerMotorEncoder.getPosition()));
  }

//This is our setDesiredState alg. Takes the current state and the desired state shown by the controller and points the wheels to that 
//location
public void setDesiredState(SwerveModuleState state) {
  //stick deadzone. No movement will occur if the stick has a value less than 0.01  
  if (Math.abs(state.speedMetersPerSecond) < 0.01) {
        stop();
        return;
  }
 state = optimizer2(state, gState().angle.getDegrees());

//  SmartDashboard.putString("States [" + steerMotor.getDeviceId() + "[", state.toString());

//call our drive motor and steer motor. Steer motor is multiplied by 3 to get 90deg instead of 30deg when strafing direct right/left
 driveMotor.set(state.speedMetersPerSecond / Constants.DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
turningPidController.setReference(state.angle.getDegrees(), ControlType.kPosition);
absoluteEncoder.setPosition(0);

/*SmartDashboard debug stuff, printing out our drive and steer values for debugging. Uncomment as needed
  SmartDashboard.putNumber("Speed", getDriveVelocity());
  SmartDashboard.putNumber("Drive Speed" + driveMotor.getDeviceId(), state.speedMetersPerSecond / Constants.DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
 */
}
public SwerveModuleState optimizer( SwerveModuleState desiredState, Rotation2d currentAngle) {
  var delta = desiredState.angle.minus(currentAngle);

  if (Math.abs(delta.getDegrees()) > 90) {
    return new SwerveModuleState(
        -desiredState.speedMetersPerSecond,
        desiredState.angle.rotateBy(Rotation2d.fromDegrees(180.0)));
  } else {
    return new SwerveModuleState(desiredState.speedMetersPerSecond, desiredState.angle);
  }

}

public SwerveModuleState optimizer2(SwerveModuleState desiredState, double currentAngle) {
  var delta = desiredState.angle.minus(Rotation2d.fromDegrees(currentAngle));
  // SmartDashboard.putString("Delta Value [" + steerMotor.getDeviceId() + "]", delta.toString());
  // SmartDashboard.putNumber("Current Angle [" + steerMotor.getDeviceId() + "]", currentAngle);
  // SmartDashboard.putString("Desired State [" + steerMotor.getDeviceId() + "]", desiredState.toString());
  // SmartDashboard.putString("gState Angle [" + steerMotor.getDeviceId() + "]", gState().angle.toString());

  if (Math.abs(delta.getDegrees()) > 90) {
    return new SwerveModuleState(
      -desiredState.speedMetersPerSecond,
      (Rotation2d.fromDegrees(180).minus(desiredState.angle)).times(-1)
    );
  }
  


  return new SwerveModuleState(desiredState.speedMetersPerSecond, desiredState.angle);
}



//Auto face forward alg. Takes the current angle and the desired angle, foward, and sets the RE = to the offset needed to set the
//motors to facefoward. Enabling and disabling zeros the RE values. 


public void wheelFaceForward(double AEOffset) {

steerMotorEncoder.setPosition(getAbsoluteEncoderDeg(AEOffset));



try{
  Thread.sleep(10);
  turningPidController.setReference(0, ControlType.kPosition);
 }catch (Exception e) {
  
 }

 }


}


  


//end of the module.
//This module is duplicated 4 times to create 4 swerve modules. Each one runs the same but does different movements based on the inputs
//given by the operator
