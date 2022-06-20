import java.util.*;

public class Worker extends Thread {
    //workers are simulated with thread to run job in a producer consumer factory model;
    //waiting jobs make sure if the timed-out task is stored and re-checked when there's a free worker
    private int count = 0;
    public Queue<Job> runningJobsFactory;
    public PriorityQueue<Job> waitingJobs = new PriorityQueue<>(new Comparator<Job>() {
        @Override
        public int compare(Job o1, Job o2) {
            return (int) (o1.interval - o2.interval);
        }
    });
    public Worker(Queue<Job> runningJobsFactory,PriorityQueue<Job> waitingJobs) {
        this.runningJobsFactory = runningJobsFactory;
        this.waitingJobs = waitingJobs;
    }



    @Override
    public void run() {
        while (true) {
            synchronized (runningJobsFactory) {
                if (runningJobsFactory.size() == 0 &&waitingJobs.size()==0) {
                    try {
                        runningJobsFactory.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(waitingJobs.size()!=0){
                    synchronized (waitingJobs){
                        Job job = waitingJobs.poll();
                        System.out.println(System.currentTimeMillis()+"waited");
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

                }
                else{
                    Job job = runningJobsFactory.poll();
                    runningJobsFactory.notify();

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
                }
                //消费

        }
    }

/*
        public void retry(){

            while(!waitingJobs.isEmpty()) {
                Timer t = new Timer();
                t.scheduleAtFixedRate(execute(), 10, 10);
            }
        }
        public TimerTask execute() {
                System.out.println("retry");
                return new TimerTask() {
                    @Override
                    public void run() {
                        //job content
                        Job job = (waitingJobs.poll());
                        if (job.times == 1) {
                            try {
                                job.oneTime();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            int counter = 0;
                            while (counter < job.times) {
                                counter++;
                                try {
                                    job.oneTime();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(System.currentTimeMillis());
                            }
                        }
                    }
            };

    }*/
}
