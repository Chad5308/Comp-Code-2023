// package frc.robot.subsystems;

// import edu.wpi.first.networktables.NetworkTableInstance;
// import frc.robot.LimelightHelpers;
// import edu.wpi.first.networktables.NetworkTableEntry;

// public class LimeLightTest {
    
//     public static class Limelight {
//         private static NetworkTableInstance table = null;
    
        
//          // Light modes for Limelight.
    
//         public enum LightMode {
//             eOn, eOff, eBlink
//         }
    
//         public static enum CameraMode {
//             eVision, eDriver
//         }
    
        
//          // Gets whether a target is detected by the Limelight.
        
//         public static boolean isTarget() {
//             //return getValue("tv").getDouble(0) == 1;
//             return LimelightHelpers.getTV("Tv");
//         }
    
//         // Horizontal offset from crosshair to target (-27 degrees to 27 degrees).
         
//         public double tx = LimelightHelpers.getTX("");

    
//         //Vertical offset from crosshair to target (-20.5 degrees to 20.5 degrees).
         
//         public double ty = LimelightHelpers.getTY("");

//         //Area that the detected target takes up in total camera FOV (0% to 100%).
        
//         public double ta = LimelightHelpers.getTA("");

//         // Gets target skew or rotation (-90 degrees to 0 degrees).

//         public static double getTs() {
//             return getValue("ts").getDouble(0.00);
//         }
    
// //left off here


//         // Gets target latency (ms).
       
//         public static double getTl() {
//             return getValue("tl").getDouble(0.00);
//         }
    
//         //Sets LED mode of Limelight. 
        
//         public static void setLedMode(LightMode mode) {
//             getValue("ledMode").setNumber(mode.ordinal());
//         }
    
//         //Sets camera mode for Limelight.

//         public static void setCameraMode(CameraMode mode) {
//             getValue("camMode").setNumber(mode.ordinal());
//         }
    
//         //Sets pipeline number (0-9 value).
         
//         public static void setPipeline(int number) {
//             getValue("pipeline").setNumber(number);
//         }
    
//         /**
//          * Helper method to get an entry from the Limelight NetworkTable.
//          * 
//          * @param key
//          *            Key for entry.
//          * @return NetworkTableEntry of given entry.
//          */
//         private static NetworkTableEntry getValue(String key) {
//             if (table == null) {
//                 table = NetworkTableInstance.getDefault();
//             }
    
//             return table.getTable("limelight").getEntry(key);
//         }
//     }


    
// }
