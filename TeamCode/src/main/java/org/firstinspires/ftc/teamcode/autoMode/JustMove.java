package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class JustMove extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startPose = new Pose(0.4, 0.4, 1);
    private Pose scorePose;
    private Pose parkPose;

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        assert autoRoutine(new Pose(0.0, 0.0, 0)) != null;
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }

    private Path startToScore(Pose currentPosition) {
        return line(currentPosition, scorePose).linear(currentPosition, scorePose);
    }

    private Path park(Pose currentPosition) {
        return line(currentPosition, parkPose).linear(currentPosition, parkPose);
    }

    private Command autoRoutine(Pose currentPosition) {
        return null;
    }
}
