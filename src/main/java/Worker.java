import java.util.*;

public class Worker extends Thread {
    //workers are simulated with thread to run job in a producer consumer factory model;
    //waiting jobs make sure if the timed-out task is stored and re-checked when there's a free worker
    private int count = 0;
    public Queue<Integer> runningJobsFactory;
    public HashMap<Integer, Job> map;

    public PriorityQueue<Integer> waitingJobs = new PriorityQueue<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return (int) (map.get(o1).interval - map.get(o2).interval);
        }
    });

    public Worker(Queue<Integer> runningJobsFactory, PriorityQueue<Integer> waitingJobs, HashMap<Integer, Job> map) {
        this.runningJobsFactory = runningJobsFactory;
        this.waitingJobs = waitingJobs;
        this.map = map;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (runningJobsFactory) {
                if (runningJobsFactory.size() == 0 && waitingJobs.size() == 0) {
                    try {
                        runningJobsFactory.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (waitingJobs.size() != 0) {
                    synchronized (waitingJobs) {
                        Job job = map.get(waitingJobs.poll());
                        System.out.println(System.currentTimeMillis() + "waited");
                        waitingJobs.notify();
                        try {
                            if (job.times == 1) {
                                job.oneTime();
                            } else {
                                int counter = 0;
                                while (counter < job.times) {
                                    counter++;
                                    job.oneTime();
                                    System.out.println(System.currentTimeMillis());
                                }
                            }
                            // 唤醒生产线程
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else {
                    int i = runningJobsFactory.poll();
                    Job job = map.get(i);
                    if (job == null) {
                        runningJobsFactory.remove(i);
                        runningJobsFactory.notify();
                    } else {
                        runningJobsFactory.notify();
                        try {
                            if (job.times == 1) {
                                job.oneTime();
                            } else {
                                int counter = 0;
                                while (counter < job.times) {
                                    counter++;
                                    job.oneTime();
                                    job.sleep(job.interval);
                                    System.out.println(System.currentTimeMillis());
                                }
                            }
                            // 唤醒生产线程
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
                //消费

            }
        }
    }
}
