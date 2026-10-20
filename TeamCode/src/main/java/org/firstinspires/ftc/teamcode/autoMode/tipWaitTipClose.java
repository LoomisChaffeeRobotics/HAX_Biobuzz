package org.firstinspires.ftc.teamcode.autoMode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import org.firstinspires.ftc.teamcode.pedro.Constants;


@Autonomous(name = "Tip Wait Tip Close", group = "Competition")
public class tipWaitTipClose extends RobotOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose shootPose = poseFactory.of(57, 20, 180);
    private final Pose startPose = poseFactory.of(56, 8, 180);
    private final Pose prepGardenPose = poseFactory.of(27, 9, 185);
    private final Pose inGardenPose = poseFactory.of(9, 8, 185);
    private final Pose flower1Pose = poseFactory.of(17, 48, 180);
    private final Pose flower1InPose = poseFactory.of(10, 48, 180);


    private final Path toShootOne = Paths.line(startPose, shootPose).constant(startPose);
    private final Path toGardenPrep = Paths.line(shootPose, prepGardenPose).linear(shootPose, prepGardenPose);
    private final Path intoGarden = Paths.line(prepGardenPose, inGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.4));
    private final Path backOffGarden = Paths.line(inGardenPose, prepGardenPose).constant(inGardenPose)
            .with(Constants.foresightConfig.)
    private final Path toFlowerOne = Paths.line(startPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flowerOneIn = Paths.line(flower1Pose, flower1InPose).constant(flower1InPose)
            .with(Constants.foresightConfig.maxPathSpeed.at(0.5));
    private final Path toShootTwo = Paths.line(flower1InPose, shootPose).constant(shootPose);

    private final double delayMs = 0;
    private final double teammateMs = 1000;
    private final double forceParkTime = 26_000;

    @Override
    public void init() {
        super.init();
        robot.drive.setPose(startPose);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    private Command autoRoutine() {
        return sequential(
                ///FIRST SHOOTING
                deadline(
                waitMs(forceParkTime),
                ///SET DEADLINE FOR PARKING
                        sequential(
                instant(() -> {
                    robot.shooterP.setRPMByHand(1000);
                    robot.shooterN.setRPMByHand(1000);
                }),
                instant(() -> {
                    robot.shooterP.spinUp();
                    robot.shooterN.spinUp();
                }),
                robot.drive.followPath(toShootOne),
                waitMs(delayMs),
                //waitUntil(() -> robot.shooterP.getSpunUp() && robot.shooterN.getSpunUp()),
                instant(robot.shooterP::shoot),
                waitMs(4000),

                ///DRIVE TO GARDEN
                parallel(
                        robot.drive.followPath(toFlowerOne),
                        instant(robot.shooterP::stopShoot),
                        robot.intake.on()
                ),


                ))
                ///GO PARK

        );
    }
}