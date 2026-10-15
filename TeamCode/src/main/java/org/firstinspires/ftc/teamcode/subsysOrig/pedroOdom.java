package org.firstinspires.ftc.teamcode.subsysOrig;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class pedroOdom extends OpMode {
    private Follower follower;
    private double robotX;
    private double robotY;
    private double robotHeading;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object

        robotX = robotPose.x();
        robotY = robotPose.y();
        robotHeading = robotPose.heading();

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }

    public void overridePosition(Pose newPose) {
        follower.setPose(newPose);
        robotX = newPose.x();
        robotY = newPose.y();
        robotHeading = newPose.heading();
    }
}
