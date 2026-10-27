package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.math.Pose;

/**
 * Shares the final autonomous pose with teleop in the same Robot Controller process.
 * Defaults to the origin if no autonomous has saved a pose. Restarting the app
 * clears this memory; it is not written to disk.
 */
public final class OpModeStorage {
    public static Pose autonomousEndPose = new Pose(0, 0, 0);

    private OpModeStorage() {
    }
}
