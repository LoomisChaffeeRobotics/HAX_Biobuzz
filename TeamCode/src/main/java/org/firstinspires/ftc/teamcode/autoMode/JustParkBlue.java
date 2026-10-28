package org.firstinspires.ftc.teamcode.autoMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.OpModeStorage;

@Autonomous
public class JustParkBlue extends OpMode {

    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees().mirrorX(70.75).mirrorY(70.75); // add this

    private final Pose startPose = p.of(7.738026124818582, 86.6298984034833, 90);
    private final Pose parkPose = p.of(10.6, 96.2 ,90);

    private Path park(){
        return line(startPose, parkPose).linear(startPose, parkPose);
    }
    private Command autoRoutine() {
        return sequential(
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
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
    @Override
    public void stop() {
        if (follower != null) {
            OpModeStorage.autonomousEndPose = follower.pose();
        }
    }
}
