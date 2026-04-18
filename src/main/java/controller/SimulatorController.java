package controller;

public class SimulatorController {

}

/* package controller;

import simulation.Simulation;

public class SimulatorController {

    private final Simulation simulation;
    private Thread thread;

    private volatile boolean running = false;

    public SimulatorController(Simulation simulation) {
        this.simulation = simulation;
    }

    public void start() {

        if (running) return;
        running = true;

        thread = new Thread(() -> {

            simulation.init(); // ONLY ONCE

            while (running) {

                simulation.step();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        thread.start();
    }

    public void pause() {
        running = false;

        if (thread != null) {
            thread.interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }
}*/