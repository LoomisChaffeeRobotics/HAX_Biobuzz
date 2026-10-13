package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class pedroOdom {
    private Follower follower;
    private double robotX;
    private double robotY;
    private double robotHeading;
    private Telemetry telemetry;

    public void init(HardwareMap hardwareMap, Telemetry telemetryNew) {
        follower = Constants.create(hardwareMap);
        telemetry = telemetryNew;
    }

    public void loop() {
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object

        robotX = robotPose.x();
        robotY = robotPose.y();
        robotHeading = robotPose.heading();
//
//        telemetry.addData("Robot X", robotPose.x());
//        telemetry.addData("Robot Y", robotPose.y());
//        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }

    public void overridePosition(Pose newPose) {
        follower.setPose(newPose);
        robotX = newPose.x();
        robotY = newPose.y();
        robotHeading = newPose.heading();
    }

    public double[] getPosition() {
        return new double[]{robotX, robotY, robotHeading};
    }
}
