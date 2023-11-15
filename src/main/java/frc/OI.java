// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public class OI {
  public final CommandXboxController opController;
  public final XboxController controller;
  public final XboxController eStopController;
  public final Joystick driveStick;
  public final Joystick thetastick;
  public final POVButton povNorth, povEast, povSouth, povWest;
  public final JoystickButton buttonA, buttonB, buttonY, buttonX, startButton, backButton, rightBumper, leftBumper, rightStick;

  public OI(int constant){
    opController = new CommandXboxController(constant);
    
  //Drive Controller buttons
    // init buttons
      controller = new XboxController(constant);
      eStopController = new XboxController(constant);
      // init buttons
      buttonA = new JoystickButton(controller, Button.kA.value);
      buttonB = new JoystickButton(controller, Button.kB.value);
      buttonX = new JoystickButton(controller, Button.kX.value);
      buttonY = new JoystickButton(controller, Button.kY.value);
      rightStick = new JoystickButton(controller, Button.kRightStick.value);

      //
      startButton = new JoystickButton(controller, Button.kStart.value);
      backButton = new JoystickButton(controller, Button.kBack.value);
      //
      povNorth = new POVButton(controller, 0);
      povEast = new POVButton(controller, 90);
      povSouth = new POVButton(controller, 180);
      povWest = new POVButton(controller, 270);
      //
      rightBumper = new JoystickButton(controller, Button.kRightBumper.value);
      leftBumper = new JoystickButton(controller, Button.kLeftBumper.value);
      //
      driveStick = new Joystick(constant);
      thetastick = new Joystick(constant);


    }
  


  }



