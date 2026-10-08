package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import static com.pedropathing.api.Paths.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class JustPark extends OpMode {

    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees(); // add this

    private final Pose startPose = p.of(24, 24, 0);
    private final Pose parkPose = p.of(48, 48, 90);
    private final Pose controlPose = p.of(36, 60, 45);

    private final Pose scorePose = p.of(48, 48, 90);

    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }
    private Path park(){
        return line(scorePose, parkPose).linear(scorePose, parkPose);
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),
                // Add mechanism commands here.
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}
