package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class pedroOdom {
    private Follower follower;
    private double robotX;
    private double robotY;
    private double robotHeading;
    private Telemetry telemetry;

    public void init(OpMode opMode) {
        follower = Constants.create(opMode.hardwareMap);
        telemetry = opMode.telemetry;
    }

    public void loop() {
        follower.update();
    }

    public void overridePosition(Pose2D newPose) {
        follower.setPose(new Pose(
                newPose.getX(DistanceUnit.INCH),
                newPose.getY(DistanceUnit.INCH),
                newPose.getHeading(AngleUnit.RADIANS))); // pedro stores heading in radians
        // see https://pedropathing.com/docs/pathing/reference/coordinates for coordinates in pedro
    }

    public Pose2D getPosition() {
        Pose followerPose = follower.pose();

        return new Pose2D(
                DistanceUnit.INCH,
                followerPose.x(),
                followerPose.y(),
                AngleUnit.RADIANS,
                followerPose.heading());
    }
}
