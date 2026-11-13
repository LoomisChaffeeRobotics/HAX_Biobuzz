package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.data.LUT;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static java.lang.Math.abs;

@Config
public class ShooterPollen {
    public static boolean override = false;
    public static double overrideTarget = 1000;
    DcMotorEx flywheel;
    Servo flicker;
    DcMotorEx feeder;
    private final Telemetry telemetry;
    public Drivetrain Drivetrain;
    public double rpm = 0;
    public boolean useCalculatedRPM = false;
    // Updated by the caller; use the same distance unit as rpmLookup.
    public double distanceFromGoal = 0;

    public boolean spinUp = false;

    public boolean flickerBlocking = true;

    static double RPMtoTicksPerSecond = (double) 28 / 60;

    public double flickerBlockPos = 0.8;
    public double flickerFreePos = 0.4;
    public boolean spinning = false;
    public boolean shooting = false;

    public double rpmTolerance = 100;
    public Pose curPos;
    private ElapsedTime timer;
    private boolean starting = true;
    LUT lut;

    public ShooterPollen(Robot robot) {
        flywheel = robot.hardwareMap.get(DcMotorEx.class, "shooterP");
        flicker = robot.hardwareMap.get(Servo.class, "flicker");
        feeder = robot.hardwareMap.get(DcMotorEx.class, "feeder");

        telemetry = robot.telemetry;
        Drivetrain = robot.drive;
        timer = new ElapsedTime();
    }
    /**
     * Updates the target RPM using a distance supplied by the caller.
     * Use the same distance unit as this shooter's calibration table.
     * Call again whenever the distance changes. An empty table returns zero RPM.
     */
    public void setRPMBasedOnDistance(double distanceFromGoal) {
        rpm = lut.pollenValue(distanceFromGoal);
    }

    public void setDistanceFromHive(double distanceFromGoal) {
        this.distanceFromGoal = distanceFromGoal;
    }

    public void setRPMByHand(double RPM){
        rpm = RPM;
    }

    public void spinUp(){spinning = true;}

    public void spinDown(){spinning = false;}
    public boolean getSpinning(){return spinning;}

    public void shoot(){
        if (timer.time() > 2){
            shooting = true;
            starting = false;
            timer.reset();
        }
    }

    public void stopShoot(){shooting = false;}


    public double getFlyWheelVel(){return flywheel.getVelocity() / RPMtoTicksPerSecond;}
    public boolean getSpunUp(){
        if (abs(rpm - getFlyWheelVel()) < rpmTolerance){
            return true;
        }
        return false;
    }


    public Command periodic() {
        return infinite(() -> {

            flywheel.setVelocity(RPMtoTicksPerSecond * rpm);
            feeder.setVelocity(4500 * (rpm == 0? 0:1));

            if (shooting){
                if (timer.time() > 1 && !starting){
                    stopShoot();
                }
                if (getSpunUp()){
                    flicker.setPosition(flickerFreePos);
                }
            }
            else{
                flicker.setPosition(flickerBlockPos);
            }



            telemetry.addData("Poll Flywheel Velocity", getFlyWheelVel());
            telemetry.addData("Poll Flywheel target", rpm);
            telemetry.addData("Poll Flywheel Power", flywheel.getPower());
        });
    }
}