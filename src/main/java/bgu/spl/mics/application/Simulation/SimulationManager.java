package bgu.spl.mics.application.Simulation;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.services.FusionSlamService;
import bgu.spl.mics.application.services.TimeService;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationManager {

    public static void startSimulation(int tickTime, int duration, List<MicroService> cameraServices, List<MicroService> lidarServices, MicroService poseService) {

        List<Thread> threads = new LinkedList<>();

        MicroService fusionSlamService = new FusionSlamService(FusionSlam.getInstance());
        Thread FusionThread = new Thread(fusionSlamService);
        threads.add(FusionThread);
//        FusionThread.start();

        for (MicroService service : lidarServices) {
            Thread LidarThread = new Thread(service);
            threads.add(LidarThread);
            //LidarThread.start();
        }

        for (MicroService service : cameraServices) {
            Thread cameraThread = new Thread(service);
            threads.add(cameraThread);
            //cameraThread.start();
        }

        Thread poseThread = new Thread(poseService);
        threads.add(poseThread);
        //poseThread.start();

        MicroService timeService = new TimeService(tickTime, duration);
        Thread timeThread = new Thread(timeService);
        threads.add(timeThread);
        //timeThread.start();

        for (Thread t : threads) {
            try {
                t.join();
                System.out.println(t.getName() + " has finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread t : threads){
            t.start();
        }

    }

}
