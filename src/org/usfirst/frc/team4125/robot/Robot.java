package org.usfirst.frc.team4125.robot;

import com.ni.vision.NIVision.Image;

import java.util.ArrayDeque;

import com.ni.vision.NIVision;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
	
public class Robot extends IterativeRobot {
	final String withArm = "With Arm";
	final String withoutArm = "Without Arm";
	final String lowGoal = "Experimental Low Goal";
	final String backAndForth = "Over and Back";
	final String backAndForthShort = "Over and Back Shorter";
	String autoSelected;
	SendableChooser chooser;
	public static final double kDefaultMaxOutput = 1.0;
	protected double m_maxOutput;
    public static final double wheelDiameter = 8;
    public static final double pulsePerRevolution = 360;
    public static final double encoderGearRatio = 1;
    public static final double gearRatio = 50.0/24.0;
    SmartDashboard smart = new SmartDashboard();
    Joystick controller;
    Joystick joystick;
    TalonSRX topLeft, backLeft, topRight, backRight;
    int autoLoopCounter, currSession, sessionFront, sessionBack;
    Image frame;
    Relay flashlight;
    TalonSRX shooterTop = new TalonSRX(0);
    TalonSRX shooterBottom = new TalonSRX(2);
    TalonSRX intake = new TalonSRX(1);
    TalonSRX arm = new TalonSRX(7);
//    TalonSRX hook = new TalonSRX(8);
   // TalonSRX winch = new TalonSRX(9);
//    Servo exampleServo = new Servo(9);;
//    CameraServer cams = CameraServer.getInstance();
//    CameraServer camb = CameraServer.getInstance();
//    Solenoid exampleSolenoid = new Solenoid(0);
    public static Timer time = new Timer();
    public static Timer intakeTimer = new Timer();
    public static Timer shooterTimer = new Timer();
    public static Timer shooterTimer2 = new Timer();
    public static Timer intakeShooterTimer = new Timer();
    ArrayDeque<Boolean> intakeInTrigger = new ArrayDeque<>(2);
    ArrayDeque<Boolean> shooterTrigger = new ArrayDeque<>(2);
    
	Encoder r1;
	Encoder l1;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	chooser = new SendableChooser();
    	chooser.addDefault("With Arm", withArm);
    	chooser.addObject("Without Arm", withoutArm);
    	chooser.addObject("Experimental Low Goal", lowGoal);
    	chooser.addObject("Over and Back", backAndForth);
    	chooser.addObject("Over and Back Shorter", backAndForthShort);
    	SmartDashboard.putData("Auto Choices", chooser);
        controller = new Joystick(0);
        flashlight = new Relay(0);
        joystick = new Joystick(1);
        topLeft = new TalonSRX(5);
        backLeft = new TalonSRX(6);
        topRight = new TalonSRX (3);
        backRight = new TalonSRX (4);
        m_maxOutput = kDefaultMaxOutput;
        r1 = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
        l1 = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
//        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMASGE_RGB, 0);
//        sessionFront = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);      
//        sessionBack = NIVision.IMAQdxOpenCamera("cam2", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
//        currSession = sessionFront;
//        NIVision.IMAQdxConfigureGrab(currSession);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */

    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
    	System.out.println("Auto Selected: " + autoSelected);
    	
    	final double distanceperpulse = Math.PI*wheelDiameter/pulsePerRevolution/
        		encoderGearRatio/gearRatio/-1;
    	r1.setDistancePerPulse(distanceperpulse);
    	l1.setDistancePerPulse(distanceperpulse);
    	r1.reset();
    	l1.reset();
    	time.start();
    	}
    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
//    	double encoderDistanceReadingR = r1.getDistance();
//    	double encoderDistanceReadingL = l1.getDistance();
//		SmartDashboard.putNumber("Right Encoder Reading", encoderDistanceReadingR);
//		SmartDashboard.putNumber("Left Encoder Reading", encoderDistanceReadingL);
		
		
		switch(autoSelected) {
		case withoutArm:
			while(time.get() < 4){
				topLeft.set(.5);
				topRight.set(-.5);
				backLeft.set(.5);
				backRight.set(-.5);
			}
			while(time.get() <= 15 && time.get() > 4) {
				arm.set(0);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
			}
			break;
		case backAndForthShort:
			while(time.get() < 4){
				topLeft.set(.5);
				topRight.set(-.5);
				backLeft.set(.5);
				backRight.set(-.5);
			}
			while(time.get() < 5 && time.get() > 3){
				shooterTop.set(-1);
				shooterBottom.set(1);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
				intake.set(0);
			}
			while(time.get() < 5.5 && time.get() > 5){
				shooterTop.set(-1);
				shooterBottom.set(1);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
				intake.set(.8);
			}
			while(time.get() < 9 && time.get() > 5.5){
				topLeft.set(-.5);
				topRight.set(.5);
				backLeft.set(-.5);
				backRight.set(.5);
				shooterTop.set(0);
				shooterBottom.set(0);
				intake.set(0);
				
			}
			while(time.get() <= 15 && time.get() > 9){
				arm.set(0);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
				shooterTop.set(0);
				shooterBottom.set(0);
				intake.set(0);
			}
			break;
		case backAndForth:
			while(time.get() < 4){
				topLeft.set(.5);
				topRight.set(-.5);
				backLeft.set(.5);
				backRight.set(-.5);
			}
			while(time.get() < 5 && time.get() > 3){
				shooterTop.set(-1);
				shooterBottom.set(1);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
			}
			while(time.get() < 5.5 && time.get() > 5){
				shooterTop.set(-1);
				shooterBottom.set(1);
				intake.set(.8);
			}
			while(time.get() < 9 && time.get() > 5.5){
				topLeft.set(-.6);
				topRight.set(.6);
				backLeft.set(-.6);
				backRight.set(.6);
				shooterTop.set(0);
				shooterBottom.set(0);
				intake.set(0);
				
			}
			while(time.get() <= 15 && time.get() > 9){
				arm.set(0);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
				shooterTop.set(0);
				shooterBottom.set(0);
				intake.set(0);
			}
			break;
		case lowGoal:
			//Put arm down
			while(time.get() < 2) {
				arm.set(.5);
			}
			//Drive Forward
			while(time.get() > 2 && time.get() < 5){
				topLeft.set(.5);
				topRight.set(-.5);
				backLeft.set(.5);
				backRight.set(-.5);
			}
			//Turn 180
			while(time.get() > 5 && time.get() < 6){
				topLeft.set(-.5);
				topRight.set(-.5);
				backLeft.set(-.5);
				backRight.set(-.5);
			}
			//Drive Forward
			while(time.get() > 6 && time.get() < 7){
				topLeft.set(.5);
				topRight.set(-.5);
				backLeft.set(.5);
				backRight.set(-.5);
			}
			//Wait until autonomous is done
			while(time.get() <= 15 && time.get() > 7) {
				arm.set(0);
				topLeft.set(0);
				topRight.set(0);
				backLeft.set(0);
				backRight.set(0);
			}
			break;
		case withArm:
			default:
				//Put arm down
				while(time.get() < 2) {
					arm.set(.5);
				}
				//Drive Forward
				while(time.get() > 2 && time.get() < 5){
					topLeft.set(.5);
					topRight.set(-.5);
					backLeft.set(.5);
					backRight.set(-.5);
				}
				//Wait until autonomous is done
				while(time.get() <= 15 && time.get() > 5) {
					arm.set(0);
					topLeft.set(0);
					topRight.set(0);
					backLeft.set(0);
					backRight.set(0);
				}
				break;
		}
//    	if (encoderDistanceReadingR > -.05) {
//    		myRobot.drive(.45, 0);
//    		}
//    		else {
//    			myRobot.drive(0, 0);
//    		}
  }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
        shooterTop.set(0);
        shooterBottom.set(0);
        intake.set(0);
        intakeTimer.reset();
        shooterTimer.reset();
        intakeShooterTimer.reset();
//        cams.startAutomaticCapture("cam1");
//        camb.startAutomaticCapture("cam2");
//        NIVision.IMAQdxGrab(currSession, frame, 1);
//        CameraServer.getInstance().setImage(frame);
        
        
    }
    public void teleopPeriodic(){
//    	flashlight.set(Relay.Value.kForward);
        boolean intakeOut = joystick.getRawButton(3);
        boolean intakeIn = joystick.getRawButton(2);
        boolean shooter = joystick.getRawButton(1);
        boolean shooterOut = joystick.getRawButton(4);
        boolean outakeFast = joystick.getRawButton(6);
        boolean armUp = controller.getRawButton(6);
        boolean armDown = controller.getRawButton(5);
        boolean armUp2 = joystick.getRawButton(8);
        boolean armDown2 = joystick.getRawButton(9);
//        boolean cameraAim = controller.getRawButton(1);
        arcadeDrive(-controller.getY(),-controller.getX());
        
            Timer.delay(0.005);        // wait for a motor update time
            
            boolean last = false;
            
            if (!intakeInTrigger.isEmpty()) {
            	last = intakeInTrigger.remove();
            }
            intakeInTrigger.add(intakeIn);
            
            if(!last && intakeIn) {
            	intakeTimer.reset();
            	intakeTimer.start();
                intake.set(-1);
            } else if (!(intakeTimer.get() > 0.22)) {
            	
            }
            
//            else if (!shooterTrigger.isEmpty()) {
//    			last2 = shooterTrigger.remove();
//    		}
//    		shooterTrigger.add(shooter);
//    		
//    		 if (!last2 && shooter) {
//    			shooterTimer.reset();
//    			shooterTimer.start();
//    			shooterTop.set(-1);
//    			shooterBottom.set(1);
//    		} else if (!(shooterTimer.get() > 2)) {
//    			//shooterTimer2.reset();
//    			//shooterTimer2.start();
//    			shooterTop.set(-1);
//    			shooterBottom.set(1);
//    			intake.set(.8);
//    			shooterTimer.stop();
//    			//while (shooterTimer2.get() <= 2.5) {
//    				//shooterTop.set(-1);
//    				//shooterBottom.set(1);
//    				//intake.set(.8);
//    			//}
//    		}
//    		else if (!(shooterTimer.get() > 2)){
//    				
//    		}
    		
            
            
            
            else if(intakeOut == true) {
                intake.set(.8);
            }
            else if(shooter == true) {
                shooterTop.set(-1);
                shooterBottom.set(1);
            }
            else if(shooterOut == true){
                shooterTop.set(1);
                shooterBottom.set(-1);
            }
            else if(outakeFast == true){
            	intake.set(-1);
            }
            else if(armUp == true) {
            	arm.set(.5);
            }
            else if(armDown == true) {
            	arm.set(-.5);
            }
            else if(armUp2 == true) {
            	arm.set(.5);
            }
            else if(armDown2 == true) {
            	arm.set(-.5);
            }
//            else if(cameraAim == true){
//                    if(currSession == sessionFront){
//                   		  NIVision.IMAQdxStopAcquisition(currSession);
//             		  currSession = sessionBack;
//            	          NIVision.IMAQdxConfigureGrab(currSession);
//             	} else if(currSession == sessionBack){
//                  		  NIVision.IMAQdxStopAcquisition(currSession);
//                   		  currSession = sessionFront;
//                   		  NIVision.IMAQdxConfigureGrab(currSession);
//                    }
//            }
            else {
                shooterTop.set(0);
                shooterBottom.set(0);
                intake.set(0);
                arm.set(0);
            }
//            NIVision.IMAQdxGrab(currSession, frame, 1);
//            CameraServer.getInstance().setImage(frame);
        }
    
    /**
     * This function is called periodically during test mode
     */
    public void arcadeDrive(double moveValue, double rotateValue) {
        this.arcadeDrive(moveValue, rotateValue, true);
      }
    public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {
        // local variables to hold the computed PWM values for the motors

        double leftMotorSpeed;
        double rightMotorSpeed;

        moveValue = limit(moveValue);
        rotateValue = limit(rotateValue);

        if (squaredInputs) {
          // square the inputs (while preserving the sign) to increase fine control
          // while permitting full power
          if (moveValue >= 0.0) {
            moveValue = (moveValue * moveValue);
          } else {
            moveValue = -(moveValue * moveValue);
          }
          if (rotateValue >= 0.0) {
            rotateValue = (rotateValue * rotateValue);
          } else {
            rotateValue = -(rotateValue * rotateValue);
          }
        }

        if (moveValue > 0.0) {
          if (rotateValue > 0.0) {
            leftMotorSpeed = moveValue - rotateValue;
            rightMotorSpeed = Math.max(moveValue, rotateValue);
          } else {
            leftMotorSpeed = Math.max(moveValue, -rotateValue);
            rightMotorSpeed = moveValue + rotateValue;
          }
        } else {
          if (rotateValue > 0.0) {
            leftMotorSpeed = -Math.max(-moveValue, rotateValue);
            rightMotorSpeed = moveValue + rotateValue;
          } else {
            leftMotorSpeed = moveValue - rotateValue;
            rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
          }
        }

        setLeftRightMotorOutputs(leftMotorSpeed, rightMotorSpeed);
      }
    public void setLeftRightMotorOutputs(double leftOutput, double rightOutput) {
        if (backLeft == null || backRight == null) {
          throw new NullPointerException("Null motor provided");
        }

        if (topLeft != null) {
          topLeft.set(limit(leftOutput) * m_maxOutput);
        }y
        backLeft.set(limit(leftOutput) * m_maxOutput);

        if (topRight != null) {
          topRight.set(-limit(rightOutput) * m_maxOutput);
        }
        backRight.set(-limit(rightOutput) * m_maxOutput);
      }
    protected static double limit(double num) {
        if (num > 1.0) {
          return 1.0;
        }
        if (num < -1.0) {
          return -1.0;
        }
        return num;
      }
    public void testPeriodic() {
        LiveWindow.run();
    }
    
}
