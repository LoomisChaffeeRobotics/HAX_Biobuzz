package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class ShooterNectar {
    public static boolean override = false;
    public static double overrideTarget = 1000;
    DcMotorEx flywheel;
    Servo flicker;
    private final Telemetry telemetry;
    public Drivetrain Drivetrain;
    public double rpm = 0;
    public boolean useCalculatedRPM = false;
    // Updated by the caller; use the same distance unit as rpmLookup.
    public double distanceFromGoal = 0;

    // Add measured {distance, RPM} rows here in strictly increasing distance order.
    // This shooter's calibration is independent of the other shooter's table.
    public final lookUp rpmLookup = new lookUp(new double[][] {
    });

    public boolean spinUp = false;

    public boolean flickerBlocking = true;

    static double RPMtoTicksPerSecond = (double) 28 / 60;

    public double flickerBlockPos = 0;
    public double flickerFreePos = 1;
    public boolean spining = false;
    public boolean shooting = false;

    public double rpmTol = 300;
    public Pose curPos;

    public ShooterNectar(Robot robot) {
        flywheel = robot.hardwareMap.get(DcMotorEx.class, "flywheelN");
        flicker = robot.hardwareMap.get(Servo.class, "flickerN");

        telemetry = robot.telemetry;
        Drivetrain = robot.drive;
    }
    /**
     * Updates the target RPM using a distance supplied by the caller.
     * Use the same distance unit as this shooter's calibration table.
     * Call again whenever the distance changes. An empty table returns zero RPM.
     */
    public void setRPMBasedOnPosition(double distanceFromGoal) {
        rpm = rpmLookup.getRPM(distanceFromGoal);
    }

    public void setRPMByHand(double RPM){
        rpm = RPM;
    }

    public void spinUp(){spining = true;}

    public void spinDown(){spining = false;}

    public boolean getSpining(){return spining;}
    public void shoot(){shooting = true;}

    public void stopShoot(){shooting = false;}


    public double getFlyWheelVel(){return flywheel.getVelocity() / RPMtoTicksPerSecond;}
    public boolean getSpunUp(){
        if (abs(rpm - getFlyWheelVel()) < rpmTol){
            return true;
        }
        return false;
    }


    public Command periodic() {
        return infinite(() -> {
            if (useCalculatedRPM) {
                setRPMBasedOnPosition(distanceFromGoal);
            }

            if (spining){
                flywheel.setVelocity(RPMtoTicksPerSecond * rpm);
            }
            else {
                flywheel.setVelocity(0);
            }

            if (shooting){
                if (getSpunUp()){ flicker.setPosition(flickerFreePos); }
                else{ spinUp(); } //Turn On flywheel if we try to shoot while off
            }
            else{
                flicker.setPosition(flickerBlockPos);
            }



            telemetry.addData("Nec Flywheel Velocity", getFlyWheelVel());
            telemetry.addData("Nec Flywheel target", rpm);
            telemetry.addData("Nec Flywheel Power", flywheel.getPower());
        });
    }
}