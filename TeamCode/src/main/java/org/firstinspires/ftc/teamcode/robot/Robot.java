package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class Robot {
    public final HardwareMap hardwareMap;
    public final Telemetry telemetry;
    public final Intaker intake;
    public final drivetrain drive;
    public final ShooterNectar shooterN;
    public final ShooterPollen shooterP;

    public Robot(OpMode opMode) {
        hardwareMap = opMode.hardwareMap;
        telemetry = new MultipleTelemetry(
                opMode.telemetry,
                FtcDashboard.getInstance().getTelemetry()
                //PanelsTelemetry.INSTANCE.getFtcTelemetry()
        );
        intake = new Intaker(this);
        drive = new drivetrain(this);
        shooterN = new ShooterNectar(this);
        shooterP = new ShooterPollen(this);
    }
}