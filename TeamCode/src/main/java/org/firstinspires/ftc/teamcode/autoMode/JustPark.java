package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class JustPark extends OpMode {

    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees(); // add this

    private final Pose startPose = p.of(24, 24, 0);
    private final Pose park = p.of(48, 48, 90);


    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

    }
}
