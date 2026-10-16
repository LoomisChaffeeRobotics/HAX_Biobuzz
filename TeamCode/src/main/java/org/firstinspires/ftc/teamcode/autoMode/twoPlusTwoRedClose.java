package org.firstinspires.ftc.teamcode.autoMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class twoPlusTwoRedClose extends OpMode {
    //--------------ROBOT IMPORTS-----------------------
    Shooter shooterPollen;
    Shooter shooterNectar;
    Intake intake;
    //---------------------ROBOT FUNCTIONS-----------------------
    private void spinBoth(){
        shooterPollen.toggleSpinUp();
        shooterNectar.toggleSpinUp();
    }
    private Command waitForSpin(){
        // Build a scheduler command so this condition is checked after init(),
        // instead of blocking while the OpMode's fields are being initialized.
        return waitUntil(() -> shooterPollen.getSpinUp() && shooterNectar.getSpinUp());
    }
    private void setRPMS(double rpmP, double rpmN){
        shooterPollen.setRPMByHand(rpmP);
        shooterNectar.setRPMByHand(rpmN);
    }
    //-------------------PEDROPATHING CONSTANTS----------------------
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees(); // add this

    private final Pose startPose = p.of(56, 8, 90);
    private final Pose flower1Pose = p.of(17, 48, 180);
    private final Pose flower1INPose = p.of(10, 48, 180);
    private final Pose shootTwo = p.of(57, 123, 270);
    private final Pose shootTwoCnt1 = p.of(42, 45, 0);
    private final Pose shootTwoCnt2 = p.of(9, 102, 0);
    private final Pose flower2Pose = p.of(46, 123, 90);
    private final Pose flower2INPose = p.of(46, 132, 90);
    private final Pose flower2OUTPose = p.of(46, 123, 90);
    private final Pose parkPose = p.of(12, 111, 270);
    private final double delayMs = 0;
    //---------------------INDIVIDUAL PATHS--------------------
    private final Path toFlowerOne = Paths.line(startPose, flower1Pose).linear(startPose, flower1Pose);
    private final Path flower1In = Paths.line(flower1Pose, flower1INPose).constant(flower1INPose);
    private final Path shootTwoPt = Paths.curve(flower1INPose, shootTwoCnt1, shootTwoCnt2, shootTwo).linear(flower1INPose, shootTwo);
    private final Path toFlowerTwo = Paths.line(shootTwo, flower2Pose).linear(shootTwo, flower2Pose);
    private final Path flower2In = Paths.line(flower2Pose, flower2INPose).constant(flower2INPose);
    private final Path flower2Out = Paths.line(flower2INPose, flower2OUTPose).constant(flower2OUTPose);
    private final Path shootThreePt = Paths.line(flower2OUTPose, shootTwo).linear(flower2OUTPose, shootTwo);
    private final Path parking = Paths.line(shootTwo, parkPose).constant(parkPose);
    //----------------------AUTO ROUTE---------------------------

    private Command autoRoutine() {
        return sequential(
            instant(() -> setRPMS(1000, 1000)),
            instant(this::spinBoth),
            waitMs(delayMs),

            //waitForSpin(),
            instant(() -> {
                shooterPollen.shoot();
                shooterNectar.shoot();
            }),
            waitMs(4000),

            parallel(
                    follow(follower, toFlowerOne),
                    instant(() -> {
                        shooterPollen.setBlock();
                        shooterNectar.setBlock();
                    }),
                    instant(() -> intake.spinIn())
            ),

            instant(() -> intake.toggleHinge()),
            follow(follower, flower1In),
            waitMs(3000),
            instant(() -> intake.stopSpin()),

            parallel(
                    follow(follower, shootTwoPt),
                    instant(() -> setRPMS(1000, 1000))
            ),

            //waitForSpin(),
            instant(() -> {
                shooterPollen.shoot();
                shooterNectar.shoot();
            }),
            waitMs(4000),

            parallel(
                    follow(follower, toFlowerTwo),
                    instant(() -> {
                        shooterPollen.setBlock();
                        shooterNectar.setBlock();
                    }),
                    instant(() -> intake.spinIn())
            ),

            instant(() -> intake.toggleHinge()),
            follow(follower, flower2In),
            waitMs(3000),
            instant(() -> intake.stopSpin()),
                instant(() -> intake.toggleHinge()),

                parallel(
                        follow(follower, flower2Out),
                        instant(() -> setRPMS(1000, 1000))
                ),
                follow(follower, shootThreePt),
                //waitForSpin(),
                waitMs(4000),
                parallel(
                        instant(() -> {
                    shooterPollen.setBlock();
                    shooterNectar.setBlock();
                        }),
                        instant(() -> setRPMS(0, 0)),
                        follow(follower, parking)
                )
        );
    }

    @Override
    public void init() {
        //-----------------ROBOT ACTIONS-------------------
        shooterPollen = new Shooter();
        shooterNectar = new Shooter();
        intake = new Intake();

        shooterPollen.init(hardwareMap, "flickerP", "flywheelP");
        shooterNectar.init(hardwareMap, "flickerN", "flywheelN");
        intake.init(hardwareMap);

        //--------------------PEDRO ACTIONS-------------------------
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
        shooterPollen.update();
        shooterNectar.update();
        Scheduler.execute();
    }
}
