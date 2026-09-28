package org.firstinspires.ftc.teamcode;

public class FancyPID {
        public double PID_P = 1;
        public double PID_I = 0;
        public double PID_D = 0;
        public double velocity = 0;
        public double errorSum = 0;
        public double errorRateOfChange = 0;
        public double errorCurrent = 0;
        public double errorPrev = 0;
        public double iMax = 0;
        public double iRange = 0;
        public double Kp = 0;
        public double Ki = 0;
        public double Kd = 0;
        public double target = 0;
        public boolean arrived = false;
        public double errorTolerance = 0;
        public double rateOfChangeTolerance = 0;

        public void PID(){

        }

        public void setCoefficients(double p, double i, double d) {
            Kp = p;
            Ki = i;
            Kd = d;
        }
        public void init() {
            errorSum = 0;
            errorRateOfChange = 0;
            errorPrev = 0;
        }

        public void setTarget(double newTarget){
            target = newTarget;
        }

        //sets a cap on how much the integral influences the output of the PID. setting it to 0 (default) deactivates it
        void setMaxIntegral(double max){
            iMax = max / Ki;
        }

        //if the error is outside that radius the integral WONT be used. setting it to 0 (default) deactivates it
        void setIRange(double range){
            iRange = range;
        }
        void setTolerances(double tolerance){
            errorTolerance = tolerance;
        }
        void setTolerances(double tolerance, double speedTolerance){
            errorTolerance = tolerance;
            rateOfChangeTolerance = speedTolerance;
        }
        public void update(double current) {
            errorCurrent = target - current;
            PID_P = errorCurrent * Kp;

            errorRateOfChange = errorCurrent - errorPrev;
            errorPrev = errorCurrent;
            PID_D = errorRateOfChange * Kd;

            if (Math.abs(errorCurrent) >= iRange && iRange != 0) {
                errorSum = 0;
            } else {
                errorSum += errorCurrent;
            }

            if (Math.abs(errorSum) >= iMax && iMax != 0) {
                errorSum = Math.signum(errorSum) * iMax;
            }
            if ((Math.signum(errorCurrent) != Math.signum(errorSum)) || (Math.abs(errorCurrent) < errorTolerance)) {
                errorSum = 0;
            }
            if (Math.abs(errorCurrent) <= errorTolerance && Math.abs(errorRateOfChange) <= rateOfChangeTolerance) {
                arrived = true;
            }
            else{
                arrived = false;
            }
            PID_I = errorSum * Ki;

            velocity = PID_P + PID_I + PID_D;
        }

        public double getVelocity(){
            return velocity;
        }


        public boolean isArrived(){
            return arrived;
        }

        public boolean isWithinTolerance(double tolerance){
            return(Math.abs(errorCurrent) <= tolerance);
        }

        public boolean isWithinTolerance(double tolerance, double speedTolerance){
            return(Math.abs(errorCurrent) <= tolerance && Math.abs(errorRateOfChange) <= speedTolerance);
        }
}
