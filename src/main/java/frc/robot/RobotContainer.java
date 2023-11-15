// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.PneumaticsCommands;
import frc.robot.commands.PulleyCommand;
import frc.robot.subsystems.ForkLiftSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.PulleySubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import java.util.HashMap;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.OI;
import frc.robot.Constants.OIConstants;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private SendableChooser<Command> autonChooser;
  
  // all controllers will be refrenced here

  // init all our controllers and motors
  private final CommandXboxController driveController = new CommandXboxController(OIConstants.kDriverControllerPort);
  private final OI pulleyStopController = new OI(OIConstants.kPulleyStopPort);
  private final OI driveControllerOI = new OI(OIConstants.kDriverControllerPort);
private final CommandXboxController miscController = new CommandXboxController(Constants.OIConstants.kliftController);
private final CommandXboxController eStopController = new CommandXboxController(Constants.OIConstants.kPulleyStopPort);

  private final CommandXboxController opController = new CommandXboxController(OIConstants.kOPControllerPort);
  private CommandJoystick driveStick = new CommandJoystick(0);
  
  public SwerveSubsystem s_Swerve = new SwerveSubsystem(_pulley, pulleyStopController);
  public static PneumaticsSubsystem _pneumatics = new PneumaticsSubsystem();
  public ForkLiftSubsystem s_forkLift = new ForkLiftSubsystem(Constants.ForkLiftConstants.forkLiftMotorNum);
  public DriveCommand d_Command = new DriveCommand(s_Swerve, driveControllerOI, opController, _pulley, s_forkLift, miscController, pulleyStopController); 
  public static PulleySubsystem _pulley = new PulleySubsystem(Constants.PullyConstants.pulleyMotorNum);

  public static Robot robot = new Robot();




  public RobotContainer() {


    autonChooser = new SendableChooser<>();
    SmartDashboard.putData("AutonChooser", autonChooser);  
    configureAutoCommands();
  

//set the swerve drive as a default command for the drive command using the driveController and the flightstick
    s_Swerve.setDefaultCommand(
        new DriveCommand(
            s_Swerve,
            driveControllerOI, opController, _pulley, s_forkLift, miscController, pulleyStopController));


    _pulley.setDefaultCommand(
        new PulleyCommand(
            _pulley));

    _pneumatics.setDefaultCommand(
        new PneumaticsCommands(_pneumatics)
    );

    // Configure the button bindings
    configureButtonBindings();
  }



  //dont think this is really necessary because we are defining them in the file OI.java
  private void configureButtonBindings() {
   

// for palmer days and Forks days
    eStopController.b().onTrue(s_Swerve.fieldOrrientCommand());
    eStopController.povLeft().whileTrue(_pulley.StopCommand());
    eStopController.x().onTrue(_pulley.resetPulley());

    eStopController.povUp().whileTrue(_pulley.liftIntakeCommand());
     eStopController.povUp().whileFalse(_pulley.StopCommand());
     eStopController.povDown().whileTrue(_pulley.dropIntakeCommand());
     eStopController.povDown().whileFalse(_pulley.StopCommand());

        // opController.a().toggleOnTrue(s_Swerve.balanceCommand());


    /* Driver Buttons */
    opController.leftTrigger().toggleOnTrue(_pneumatics.extensionOutCommand());
    opController.leftBumper().toggleOnTrue(_pneumatics.extensionRetractCommand());
    opController.rightTrigger().whileTrue(_pneumatics.intakeOpenCommand());
    opController.rightTrigger().whileFalse(_pneumatics.intakeCloseCommand());

     opController.povUp().whileTrue(_pulley.liftIntakeCommand());
     opController.povUp().whileFalse(_pulley.StopCommand());
     opController.povDown().whileTrue(_pulley.dropIntakeCommand());
     opController.povDown().whileFalse(_pulley.StopCommand());



    opController.povRight().toggleOnTrue(s_Swerve.zeroHeadingCommand());


    opController.b().toggleOnTrue(_pulley.autoGrabUpCommand()
    .andThen(Commands.waitSeconds(1))
    .andThen(_pneumatics.extensionOutCommand())
    .andThen(_pneumatics.intakeOpenCommand())
    .andThen(Commands.waitSeconds(1.5))
    .andThen(_pulley.autoGrabDownCommand()));

    opController.a().toggleOnTrue(_pulley.topPoleCommand()
    .andThen(Commands.waitSeconds(1))
    .andThen(_pneumatics.extensionOutCommand()));

    opController.y().toggleOnTrue(_pulley.topShelfCommand()
    .andThen(Commands.waitSeconds(1))
    .andThen(_pneumatics.extensionOutCommand()));

    //  opController.y().onTrue(s_Swerve.balanceCommand());

    opController.rightBumper().toggleOnTrue(_pneumatics.extensionRetractCommand()
    .andThen(_pneumatics.intakeCloseCommand())
    .andThen(Commands.waitSeconds(0.5))
    .andThen(_pulley.homeCommand()));

    // driveStick.button(5).onTrue(_pneumatics.extensionOutCommand());
    // driveStick.button(6).onTrue(_pneumatics.extensionRetractCommand());
    // driveStick.trigger().whileTrue(_pneumatics.intakeOpenCommand());
    // driveStick.trigger().whileFalse(_pneumatics.intakeOpenCommand());

    // driveStick.axisGreaterThan(0, 1).whileTrue(_pulley.liftIntakeCommand());

    miscController.x().whileTrue(s_forkLift.liftForkCommand());
    miscController.b().whileTrue(s_forkLift.dropForkCommand());
    miscController.b().whileFalse(s_forkLift.stopForkLiftCommand());
    miscController.x().whileFalse(s_forkLift.stopForkLiftCommand());       
    


  }

  public void configureAutoCommands() {
    /*Auto Options Flow chart
 * 
 * START-
 * 1. leave commmunity and hold piece                            KEEP
 * 2. place cone or cube and stay
 * 3. place and leave commmunity                                 KEEP
 * 4. 3 + drive to second piece cone or cube                     KEEP
 * 5. 1-2 + drive back to deposit ce                             KEEP
 * 6. 1-3.5 + place game piece                                   KEEP
 * 7. 1-4 + drive onto charge station but don't level
 * 8. 1-4 + drive onto charge station but auto level             KEEP
 */

  // This will load the file "FullAuto.path" and generate it with a max velocity of 4 m/s and a max acceleration of 3 m/s^2
//RIGHT Side Grid RIGHT POLE paths
List<PathPlannerTrajectory> RRConeFLPath = PathPlanner.loadPathGroup("RR Cone, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> RRConeMLPath = PathPlanner.loadPathGroup("RR Cone, mid left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> RRConeMRPath = PathPlanner.loadPathGroup("RR Cone, mid right", new PathConstraints(2, 1));
List<PathPlannerTrajectory> RRConeFRPath = PathPlanner.loadPathGroup("RR Cone, far right", new PathConstraints(2, 1));
//RIGHT Side Grid MID SHELF paths
// List<PathPlannerTrajectory> RCubeFLPath = PathPlanner.loadPathGroup("R Cube, far left", new PathConstraints(2, 1));
List<PathPlannerTrajectory> RCubeFRPath = PathPlanner.loadPathGroup("R Cube, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> RCubeMLPath = PathPlanner.loadPathGroup("R Cube, mid left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> RCubeMRPath = PathPlanner.loadPathGroup("R Cube, mid right", new PathConstraints(2, 1));
//RIGHT Side Grid LEFT POLE
// List<PathPlannerTrajectory> RLConeFLPath = PathPlanner.loadPathGroup("RL Cone, far left", new PathConstraints(2, 1));
List<PathPlannerTrajectory> RLConeFRPath = PathPlanner.loadPathGroup("RL Cone, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> RLConeMRPath = PathPlanner.loadPathGroup("RL Cone, mid right", new PathConstraints(4, 2));
// List<PathPlannerTrajectory> RLConeMLPath = PathPlanner.loadPathGroup("RL Cone, mid left", new PathConstraints(2, 1));
//MID Grid RIGHT POLE paths
// List<PathPlannerTrajectory> MRConeFRPath = PathPlanner.loadPathGroup("MR Cone, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MRConeMRPath = PathPlanner.loadPathGroup("MR Cone, mid right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MRConeFLPath = PathPlanner.loadPathGroup("MR Cone, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MRConeMLPath = PathPlanner.loadPathGroup("MR Cone, mid left", new PathConstraints(2, 1));
//MID Grid MID SHELF paths
// List<PathPlannerTrajectory> MCubeFRPath = PathPlanner.loadPathGroup("M Cube, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MCubeMRPath = PathPlanner.loadPathGroup("M Cube, mid right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MCubeFLPath = PathPlanner.loadPathGroup("M Cube, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MCubeMLPath = PathPlanner.loadPathGroup("M Cube, mid left", new PathConstraints(2, 1));
//Mid Grid LEFT POLE paths
// List<PathPlannerTrajectory> MLConeFRPath = PathPlanner.loadPathGroup("ML Cone, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MLConeMRPath = PathPlanner.loadPathGroup("ML Cone, mid right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MLConeFLPath = PathPlanner.loadPathGroup("ML Cone, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> MLConeMLPath = PathPlanner.loadPathGroup("ML Cone, mid left", new PathConstraints(2, 1));
//LEFT Grid RIGHT POLE paths
// List<PathPlannerTrajectory> LRConeFRPath = PathPlanner.loadPathGroup("LR Cone, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> LRConeMRPath = PathPlanner.loadPathGroup("LR Cone, mid right", new PathConstraints(2, 1));
List<PathPlannerTrajectory> LRConeFLPath = PathPlanner.loadPathGroup("LR Cone, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> LRConeMLPath = PathPlanner.loadPathGroup("LR Cone, mid left", new PathConstraints(4, 2));
//LEFT Grid MID SHELF paths
// List<PathPlannerTrajectory> LCubeFRPath = PathPlanner.loadPathGroup("L Cube, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> LCubeMRPath = PathPlanner.loadPathGroup("L Cube, mid right", new PathConstraints(2, 1));
List<PathPlannerTrajectory> LCubeFLPath = PathPlanner.loadPathGroup("L Cube, far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> LCubeMLPath = PathPlanner.loadPathGroup("L Cube, mid left", new PathConstraints(2, 1));
//LEFT Grid LEFT POLE paths.
// List<PathPlannerTrajectory> LLConeFRPath = PathPlanner.loadPathGroup("LL Cone, far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> LLConeMRPath = PathPlanner.loadPathGroup("LL Cone, mid right", new PathConstraints(2, 1));
List<PathPlannerTrajectory> LLConeFLPath = PathPlanner.loadPathGroup("LL Cone, far left", new PathConstraints(3, 2));
// List<PathPlannerTrajectory> LLConeMLPath = PathPlanner.loadPathGroup("LL Cone, mid left", new PathConstraints(2, 1));
//Just a few paths back tests
// List<PathPlannerTrajectory> ReturnFL = PathPlanner.loadPathGroup("back from far left", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> ReturnFR = PathPlanner.loadPathGroup("back from far right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> ReturnMR = PathPlanner.loadPathGroup("back from mid right", new PathConstraints(2, 1));
// List<PathPlannerTrajectory> ReturnML = PathPlanner.loadPathGroup("back from mid left", new PathConstraints(2, 1));

// List<PathPlannerTrajectory> RCubeFR2RCube = PathPlanner.loadPathGroup("Complicated test 1", 2, 1);

List<PathPlannerTrajectory> Push = PathPlanner.loadPathGroup("push", 2, 1);

List<PathPlannerTrajectory> tipChargeMid = PathPlanner.loadPathGroup("Tip Charge", 10, 7.5);

List<PathPlannerTrajectory> Balance = PathPlanner.loadPathGroup("Manuel Set", 2, 1.5);

// List<PathPlannerTrajectory> markerTest = PathPlanner.loadPathGroup("Event Path Test", 2, 1);

List<PathPlannerTrajectory> overCharge2 = PathPlanner.loadPathGroup("OverCharge2", 3, 1.5);
List<PathPlannerTrajectory> BackOnCharge = PathPlanner.loadPathGroup("BackOnCharge", 4, 2.5);

List<PathPlannerTrajectory> clubFairAuto = PathPlanner.loadPathGroup("Club Fair Path", 3.5, 1.5);
List<PathPlannerTrajectory> clubfairP2 = PathPlanner.loadPathGroup("Club Fair 2nd Path", 3, 2);





// This is just an example event map. It would be better to have a constant, global event map
// in your code that will be used by all path following commands.
HashMap<String, Command> eventMap = new HashMap<>();
eventMap.put("Arrived at piece", _pneumatics.extensionOutCommand().andThen(_pneumatics.intakeOpenCommand()));


//for marker test
eventMap.put("Test Marker", new PrintCommand("Passed Test Marker").andThen(_pulley.topPoleCommand()));


// Create the AutoBuilder. This only needs to be created once when robot code starts, not every time you want to create an auto command. A good place to put this is in RobotContainer along with your subsystems.
SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(s_Swerve::getPose,
 s_Swerve::resetOdometry,
  Constants.DriveConstants.kDriveKinematics,
   new PIDConstants(0.025, 0, 0),
    new PIDConstants(0.025, 0, 0),
     s_Swerve::setModuleStates,
      eventMap,
       true,
        s_Swerve);

        



SequentialCommandGroup clubFair = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.75))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(0.75))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(autoBuilder.followPathGroup(clubFairAuto))
.andThen(_pulley.autoGrabUpCommand())
.andThen(Commands.waitSeconds(0.75))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(2))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(_pulley.clubFairCommand())
.andThen(Commands.waitSeconds(2))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(.75))
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(2))
.andThen(autoBuilder.followPathGroup(clubfairP2))
.andThen(Commands.waitSeconds(2))
.andThen(_pulley.dropIntakeCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(_pneumatics.extensionRetractCommand())
.andThen(_pulley.homeCommand())
.andThen(_pneumatics.intakeCloseCommand()));


//LOW POINT AUTOS


SequentialCommandGroup LowPointOver = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(2))
.andThen(autoBuilder.followPathGroup(Push))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(autoBuilder.followPathGroup(tipChargeMid)));

SequentialCommandGroup LowPointLeave = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(2))
.andThen(autoBuilder.followPathGroup(Push))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(autoBuilder.followPathGroup(LCubeFLPath)));

SequentialCommandGroup LowPointOverNBal = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(2))
.andThen(autoBuilder.followPathGroup(Push))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(autoBuilder.followPathGroup(tipChargeMid))
.andThen());

SequentialCommandGroup lowPointOverShortBal = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(2))
.andThen(autoBuilder.followPathGroup(Push))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(autoBuilder.followPathGroup(tipChargeMid))
.andThen(autoBuilder.followPathGroup(Balance))
.andThen());

SequentialCommandGroup balanceTest = new SequentialCommandGroup(s_Swerve.balanceCommand());





//TOP POLE COMMANDS

//Straight on with poll and Flush against the board.
SequentialCommandGroup TopPollStay = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.275))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand()));

SequentialCommandGroup LLTopPoll2FL = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.27))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(autoBuilder.followPathGroup(LLConeFLPath)));

SequentialCommandGroup LRTopPoll2FL = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.27))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(autoBuilder.followPathGroup(LRConeFLPath)));

SequentialCommandGroup RRTopPoll2FR = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.27))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(autoBuilder.followPathGroup(RRConeFRPath)));

SequentialCommandGroup RLTopPoll2FR = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.27))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(autoBuilder.followPathGroup(RLConeFRPath)));

SequentialCommandGroup midPolestipChargeMid = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topPoleCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.4))
.andThen(_pulley.topPoleDropCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.27))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(autoBuilder.followPathGroup(tipChargeMid)));






//TOP CUBE AUTOS

//set even with a shelf and a foot width away from the boards
SequentialCommandGroup TopCubeStay = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1.75))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(0.75))
.andThen(_pneumatics.intakeCloseCommand())
);

SequentialCommandGroup topRightCube2FR = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(0.5))
.andThen(autoBuilder.followPathGroup(RCubeFRPath))
);

SequentialCommandGroup topLeftCube2FL = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(1))
.andThen(autoBuilder.followPathGroup(LCubeFLPath))
);

SequentialCommandGroup topCubeOver = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(1))
.andThen(autoBuilder.followPathGroup(tipChargeMid))
);





//MID COMMANDS

SequentialCommandGroup MidConeStay = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.midPoleCommand())
.andThen(Commands.waitSeconds(2))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(3))
.andThen(_pulley.midPoleDownCommand())
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand()));


SequentialCommandGroup MidCubeStay = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.midShelfCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeCloseCommand())
.andThen(_pulley.homeCommand())
);

SequentialCommandGroup HighCubeAutoSet = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(1))
.andThen(autoBuilder.followPathGroup(tipChargeMid))
.andThen(Commands.waitSeconds(0.5))
.andThen(s_Swerve.autoBalanceCommand));

SequentialCommandGroup HighCubeLeaveBal = new SequentialCommandGroup(s_Swerve.faceForwardCommand()
.andThen(_pulley.topShelfCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionOutCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.intakeOpenCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pneumatics.extensionRetractCommand())
.andThen(_pneumatics.intakeCloseCommand())
.andThen(Commands.waitSeconds(1))
.andThen(_pulley.homeCommand())
.andThen(Commands.waitSeconds(1))
.andThen(autoBuilder.followPathGroup(tipChargeMid))
.andThen(autoBuilder.followPathGroup(overCharge2))
.andThen(autoBuilder.followPathGroup(BackOnCharge)));
// .andThen(Commands.waitSeconds(0.75))
// .andThen(s_Swerve.autoBalanceCommand));











SequentialCommandGroup Nothing = new SequentialCommandGroup(_pneumatics.extensionRetractCommand());


//add command options

      //low points
      // autonChooser.addOption("Low Point + Over", LowPointOver);
      // autonChooser.addOption("Low Point + Leave", LowPointLeave);
      // autonChooser.addOption("Low Point + Over + Balance", LowPointOverNBal);
      // autonChooser.addOption("manuel", lowPointOverShortBal);


      //High Commands
          //polls
          autonChooser.addOption("High Poll Stay", TopPollStay);
          // autonChooser.addOption("High Polls over charge station", midPolestipChargeMid);
          autonChooser.addOption("Human side station, Judge side pole, to far right piece", RLTopPoll2FR);
          autonChooser.addOption("Human side station, human side pole, to far right piece", RRTopPoll2FR);
          autonChooser.addOption("Judge side station, judge side poll, to far judge side piece", LLTopPoll2FL);
          autonChooser.addOption("Judge side station, human poll, to far judge side piece", LRTopPoll2FL);



          //Cubes
          autonChooser.addOption("Human Side Cube High Leave 2 FR", topRightCube2FR);
          autonChooser.addOption("Judge side Cube High Leave 2 FL", topLeftCube2FL);
          autonChooser.addOption("High Cube Stay", TopCubeStay);
          autonChooser.addOption("High Cube Over Charge", topCubeOver);
          

          //Mid Piece Autos
          autonChooser.addOption("Mid Cube Stay", MidCubeStay);
          autonChooser.addOption("Mid Cone Stay", MidConeStay);


          //NOTHING
          autonChooser.addOption("Nothing", Nothing);



          // autonChooser.addOption("Auto Balance Test", balanceTest);

          autonChooser.addOption("Score high and Bal", HighCubeAutoSet);
          autonChooser.addOption("Score high, Over Charge, and Bal", HighCubeLeaveBal);


          autonChooser.addOption("Club Fair Auto", clubFair);
}
 



  public Command getAutonomousCommand() {
    return autonChooser.getSelected();
  }


}







