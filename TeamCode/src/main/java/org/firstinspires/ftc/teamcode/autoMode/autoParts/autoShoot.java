package org.firstinspires.ftc.teamcode.autoMode.autoParts;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Limelight;
import org.firstinspires.ftc.teamcode.Shooter;

import java.util.Collections;
import java.util.Set;

public class autoShoot implements Command {

    private final Limelight limelight;
    private final Shooter shooter1;
    private final Shooter shooter2;
    private final double rpm1;
    private final double rpm2;

    private final boolean wait;

    private enum State {WAITING, REVVING, SHOOTING}

    private State state = State.WAITING;

    private ElapsedTime timer = new ElapsedTime();
    private static double secondsToShoot = 5;

    public autoShoot(Shooter shooterPollen, Shooter shooterNectar, double rpmPollen, double rpmNectar, Limelight limelight, boolean wait){
        this.shooter1 = shooterPollen;
        this.shooter2 = shooterNectar;
        this.limelight = limelight; // if were doing that
        this.rpm1 = rpmPollen;
        this.rpm2 = rpmNectar;
        this.wait = wait;
    }

    @Override
    public void start() {
        //TODO: SHOOTER RPM SETTING
        timer.reset();
    }

    @Override
    public void execute() {
        switch(state) {
            case WAITING:
                boolean hiveTippedUp = true; //TODO: actually make this do something with LL
                if (hiveTippedUp || !wait) {
                    state = State.SHOOTING;
                    timer.reset();
                }
                break;
//            case REVVING:
//                boolean spunUp = true; //TODO: maket this actually check
//                if (spunUp){
//                    state = State.SHOOTING;
//                    timer.reset();
//                }
//                break;
            case SHOOTING:
                shooter1.shoot();
                shooter2.shoot();

        }
    }

    @Override
    public boolean done() {
        return (((timer.time() > secondsToShoot) && (state == State.SHOOTING) )||(timer.time() > 10) && (state == State.WAITING));
    }



    @Override
    public void end(EndCondition endCondition) {
        shooter1.setBlock();

        shooter2.setBlock();
    }

    @Override
    public Set<Object> requirements() {
        return Collections.emptySet();
    }

    @Override
    public int priority() {
        return 15;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return InterruptedBehavior.SUSPEND;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return ConflictBehavior.OVERRIDE;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return BlockedBehavior.QUEUE;
    }

}
