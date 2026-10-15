package org.firstinspires.ftc.teamcode.subsysOrig;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class ShooterOld {
    DcMotorEx flyWheel;

    Servo flicker;

    public double rpm = 0;

    public boolean spinUp = false;

    public boolean flickerBlocking = true;

    static double RPMtoTicksPerSecond = (double) 28 / 60;

    public double flickerBlockPos = 0;
    public double flickerFreePos = 1;

    public double rpmTol = 300;

    /// -------------------functions------------------

    public void init(HardwareMap hardwareMap, String flickerName, String flyWheelName) {
        flicker = hardwareMap.get(Servo.class, flickerName);
        flyWheel = hardwareMap.get(DcMotorEx.class, flyWheelName);
        rpm = 0;
    }

    public void setRPMBasedOnPosition(Pose2D odomPos) {
        ///FIND RPM USING Lookuptable?
        // Yes. Use odomPos to look up the correct RPM of the motor.
        rpm = 0;
    }

    public void setRPMByHand(double RPM){
        rpm = RPM;
    }
    public void toggleSpinUp(){
        spinUp = !spinUp;
        if (!spinUp){
            flyWheel.setVelocity(RPMtoTicksPerSecond * rpm);
        }
        else{
            flyWheel.setVelocity(0);
        }

    }
    public boolean getSpinUp(){
        if (abs(rpm - getFlyWheelVel()) < rpmTol){
            return true;
        }
        return false;
    }
    public double getFlyWheelVel(){return flyWheel.getVelocity() / RPMtoTicksPerSecond;}

    public void shoot(){
        if (abs(rpm - getFlyWheelVel()) > rpmTol){
            return;
        }
        flickerBlocking = false;
    }

    public void setBlock(){
        flickerBlocking = true;
    }
    public void update(){
        if (flickerBlocking){
            flicker.setPosition(flickerBlockPos);
        }
        else{
            flicker.setPosition(flickerFreePos);
        }
    }
}
