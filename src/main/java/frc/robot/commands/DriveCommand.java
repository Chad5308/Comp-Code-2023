package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.filter.SlewRateLimiter;
// could need this import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.OI;
import frc.robot.Constants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.ForkLiftSubsystem;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.subsystems.SwerveSubsystem;

public class DriveCommand extends CommandBase {

    private final SwerveSubsystem swerveSubsystem;
    private final OI driveController;
    public final CommandXboxController opController;
    public final CommandXboxController miscController;
    public PulleySubsystem pulleySubsystem;
    public ForkLiftSubsystem forkLiftSubsystem;

    // private final Joystick driveStick;
    // private final Joystick thetaStick;

    private final SlewRateLimiter xLimiter, yLimiter, turningLimiter;
    private boolean fieldOriented=false;
     DigitalInput retractSwitch;
     public double ySpeed;
     public double xSpeed;
     public double turningSpeed;
     public CameraServer frontCamera;
     public double lightsMode;




    public DriveCommand(SwerveSubsystem swerveSubsystem, OI driveController, CommandXboxController opController, PulleySubsystem pulleySubsystem, ForkLiftSubsystem forkLiftSubsystem, CommandXboxController miscController, OI pulleyStopController) {
                this.swerveSubsystem = swerveSubsystem;
                this.pulleySubsystem = pulleySubsystem;
                this.driveController = driveController;
                this.miscController = miscController;
                this.xLimiter = new SlewRateLimiter(Constants.DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
                this.yLimiter = new SlewRateLimiter(Constants.DriveConstants.kTeleDriveMaxAccelerationUnitsPerSecond);
                this.turningLimiter = new SlewRateLimiter(Constants.DriveConstants.kTeleDriveMaxAngularAccelerationUnitsPerSecond);
                addRequirements(swerveSubsystem);
                // thetaStick = new Joystick(3);
                // driveStick = new Joystick(0);
                this.opController = opController;

            CameraServer.startAutomaticCapture();
            
            

    }

    @Override
    public void initialize() {
     swerveSubsystem.faceAllFoward();


   
    }
   


    @Override
    public void execute() {


        if(driveController.buttonX.getAsBoolean()) {
            pulleySubsystem.resetEncoder();
        }

        

//Xbox joystick init and debugging code. Main drive method
         xSpeed = driveController.controller.getLeftX() / 1.35;
         ySpeed = driveController.controller.getLeftY() / 1.35;
         turningSpeed = driveController.controller.getRightX() / -1.85;
        // SmartDashboard.putNumber("Left Stick X", driveController.controller.getLeftX());
        // SmartDashboard.putNumber("Left Stick Y", driveController.controller.getLeftY());
        // SmartDashboard.putNumber("Right Stick X", driveController.controller.getRightX());
        SmartDashboard.putBoolean("fieldOriented", fieldOriented);
       
       
//flight stick init and debugging code. Alt drive method
    //     xSpeed = driveStick.getX()*-1;
    //     ySpeed = driveStick.getY()*-1;
    //     turningSpeed = thetaStick.getX()*-1;
    //    SmartDashboard.putNumber("Left Stick X", driveStick.getX());
    //     SmartDashboard.putNumber("Left Stick Y", driveStick.getY());
    //     SmartDashboard.putNumber("turningSpeed", turningSpeed);


       

//button mappings assiociated with the drive system or aspects of the drive system

        // if(driveStick.getRawButtonPressed(4)) {
        //         swerveSubsystem.zeroHeading();
        // }
     

// TODO: make this button more reliable. When this button is held down it will turn it on and off multiple times per second.
if(driveController.povWest.getAsBoolean()){
    fieldOriented = !fieldOriented;
    
}

// if(driveStick.getRawButtonPressed(3)) {
//     fieldOriented = !fieldOriented;
// }


        if(driveController.backButton.getAsBoolean())
        {
            swerveSubsystem.faceAllFoward();
            try{
               Thread.sleep(50);
               SwerveSubsystem.frontLeftModule.steerMotorEncoder.setPosition(0);
               SwerveSubsystem.frontRightModule.steerMotorEncoder.setPosition(0);
               SwerveSubsystem.backLeftModule.steerMotorEncoder.setPosition(0);
               SwerveSubsystem.backRightModule.steerMotorEncoder.setPosition(0);
            }catch (Exception i) {
       
            }
        }


        // 2. Apply deadband
         xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
         ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
         turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0.0;

        // 3. Make the driving smoother
 // TO DO: could be causing a problem. Check if this is a problem and ways to fix or implement this differently

        xSpeed = xLimiter.calculate(xSpeed) * Constants.DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
        ySpeed = yLimiter.calculate(ySpeed) * Constants.DriveConstants.kTeleDriveMaxSpeedMetersPerSecond;
        turningSpeed = turningLimiter.calculate(turningSpeed) * Constants.DriveConstants.kTeleDriveMaxAngularSpeedRadiansPerSecond;


        // // 2. Apply deadband
         xSpeed = Math.abs(xSpeed) > OIConstants.kDeadband ? xSpeed : 0.0;
         ySpeed = Math.abs(ySpeed) > OIConstants.kDeadband ? ySpeed : 0.0;
         turningSpeed = Math.abs(turningSpeed) > OIConstants.kDeadband ? turningSpeed : 0.0;

        // 4. Construct desired chassis speeds
        ChassisSpeeds chassisSpeeds;
        if (fieldOriented) {
            // Relative to field
            chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    ySpeed * -1, xSpeed * -1, turningSpeed, swerveSubsystem.geRotation2d());
                    //swerveSubsystem.lights.set(0.57);

                    
        } else {
            // Relative to robot. 
            chassisSpeeds = new ChassisSpeeds(ySpeed * -1, xSpeed * -1, turningSpeed);
            //swerveSubsystem.lights.set(0.59);

        }


        // 5. Convert chassis speeds to individual module states
        SwerveModuleState[] moduleStates = Constants.DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);

        // 6. Output each module states to wheels
        swerveSubsystem.setModuleStates(moduleStates);
    }


    // public void lightToggle() {
    //     lightsMode = lightsMode+1;
    // }


    

    


    @Override
    public void end(boolean interrupted) {
        swerveSubsystem.stopModules();
    }

    @Override
    public boolean isFinished() {
        return false;
    }




}