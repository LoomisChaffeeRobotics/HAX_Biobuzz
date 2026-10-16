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
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

@Autonomous(name = "Two Plus Two Red Close", group = "Competition")
public class twoPlusTwoRedClose extends RobotOpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose startPose = poseFactory.of(56, 8, 90);
    private final Pose flower1Pose = poseFactory.of(17, 48, 180);
    private final Pose flower1InPose = poseFactory.of(10, 48, 180);
    private final Pose shootTwoPose = poseFactory.of(57, 123, 270);
    private final Pose shootTwoControl1 = poseFactory.of(42, 45, 0);
    private final Pose shootTwoControl2 = poseFactory.of(18, 99, 0);
    private final Pose flower2Pose = poseFactory.of(48, 123, 90);
    private final Pose flower2InPose = poseFactory.of(48, 132, 90);
    private final Pose flower2OutPose = poseFactory.of(48, 117, 90);
    private final Pose parkingPose = poseFactory.of(9, 115, 270);
    private final double delayMs = 0;

    private final Path toFlowerOne = Paths.line(startPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flowerOneIn = Paths.line(flower1Pose, flower1InPose).constant(flower1InPose);
    private final Path toSecondShoot = Paths.curve(
            flower1InPose,
            shootTwoControl1,
            shootTwoControl2,
            shootTwoPose
    ).linear(flower1InPose, shootTwoPose);
    private final Path toFlowerTwo = Paths.line(shootTwoPose, flower2Pose).linear(shootTwoPose, flower2Pose);
    private final Path flowerTwoIn = Paths.line(flower2Pose, flower2InPose).constant(flower2InPose);
    private final Path flowerTwoOut = Paths.through(flower2InPose, flower2OutPose, shootTwoPose).tangent();
    private final Path park = Paths.line(shootTwoPose, parkingPose).constant(parkingPose);

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
                instant(() -> {
                    robot.shooterP.setRPMByHand(1000);
                    robot.shooterN.setRPMByHand(1000);
                }),
                instant(() -> {
                    robot.shooterP.spinUp();
                    robot.shooterN.spinUp();
                }),
                waitMs(delayMs),
                waitUntil(() -> robot.shooterP.getSpunUp() && robot.shooterN.getSpunUp()),
                instant(robot.shooterP::shoot),
                waitMs(4000),

                parallel(
                        robot.drive.followPath(toFlowerOne),
                        instant(robot.shooterP::stopShoot),
                        robot.intake.on()
                ),
                robot.intake.hingeDown(),
                robot.drive.followPath(flowerOneIn),
                waitMs(3000),

                parallel(
                        robot.drive.followPath(toSecondShoot),
                        instant(() -> {
                            robot.shooterP.setRPMByHand(1000);
                            robot.shooterN.setRPMByHand(1000);
                        })
                ),
                waitUntil(() -> robot.shooterP.getSpunUp() && robot.shooterN.getSpunUp()),
                instant(robot.shooterP::shoot),
                waitMs(4000),

                parallel(
                        robot.drive.followPath(toFlowerTwo),
                        instant(robot.shooterP::stopShoot),
                        robot.intake.on()
                ),
                robot.intake.hingeUp(),
                robot.drive.followPath(flowerTwoIn),
                waitMs(3000),
                robot.drive.followPath(flowerTwoOut),
                robot.intake.hingeDown(),

                waitUntil(() -> robot.shooterP.getSpunUp() && robot.shooterN.getSpunUp()),
                instant(robot.shooterP::shoot),
                waitMs(4000),
                instant(robot.shooterP::stopShoot),
                robot.drive.followPath(park),
                instant(() -> {
                    robot.shooterP.spinDown();
                    robot.shooterN.spinDown();
                    robot.shooterN.stopShoot();
                }),
                robot.intake.off()
        );
    }
}
