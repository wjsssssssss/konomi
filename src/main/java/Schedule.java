import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//Schedule used hashmap to store all the inserted jobs
public class Schedule {
    public HashMap<Integer, Job> map = new HashMap<>(10000);
    public PriorityQueue<Integer> waitingJobs = new PriorityQueue<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return (int) (map.get(o1).interval - map.get(o2).interval);
        }
    });

    //public BlockingQueue runningJobs = new LinkedBlockingDeque(2);
    public Schedule(HashMap<Integer, Job> map) {
        this.map = map;
    }


    public void addJob(HashMap<Integer, Job> map, Integer index, Job job) {
        this.map = map;
        map.put(index, job);
    }

    public Job getJob(HashMap<Integer, Job> map, Integer index) {
        return map.get(index);
    }

    public void updateJob(HashMap<Integer, Job> map, Integer index, Job job) {
        map.put(index, job);
    }

    public void deleteJob(HashMap<Integer, Job> map, Integer index) {
        map.remove(index);
    }
/*

    public void oneTime(Integer index, HashMap<Integer,Job> map) throws InterruptedException {
        if(runningJobs.size()>2){
            waitingJobs.add(index);
        }else {
            Job job = map.get(index);
            runningJobs.add(map.get(index));
            job.oneTime();
            runningJobs.remove(map.get(index));
        }
        }


    public void repeat(Integer index,HashMap<Integer,Job> map, Integer times) throws InterruptedException {
        if (runningJobs.size() > 2) {
            waitingJobs.add(index);
        } else {
            Job job = map.get(index);
            runningJobs.add(job);
            int counter = 0;
            while (counter < times) {
                counter++;
                job.oneTime();
                System.out.println(System.currentTimeMillis());
                synchronized (job) {
                    job.wait(job.interval);
                }
                synchronized (job) {
                    job.notify();
                }
            }

            runningJobs.remove(job);
        }
    }

    public void runJob(Integer index,HashMap<Integer,Job> map) throws InterruptedException {
        if(map.get(index).times==1){
            oneTime(index,map);
        }else{
            repeat(index,map,map.get(index).times);
        }
    }
    public void retry(){
        Timer t = new Timer();
        t.scheduleAtFixedRate(execute(),10,10);
    }
    public TimerTask execute() {
        if(waitingJobs.isEmpty()){
            return new TimerTask() {
                @Override
                public void run() {

                }
            };
        }else{
            System.out.println("retry");
            return new TimerTask() {
                @Override
                public void run() {
                    //job content
                    if(waitingJobs!=null){
                        if(!runningJobs.contains(map.get(waitingJobs.peek()))){
                            try {
                                runJob(waitingJobs.poll(),map);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
        }
    }

*/


    public static void main(String args[]) throws InterruptedException {
        HashMap<Integer, Job> testMap = new HashMap<>(1000);
        Schedule s = new Schedule(testMap);
        Queue<Job> runningJobF = new LinkedList<>();
        PriorityQueue<Job> waitingJobs = new PriorityQueue<>(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return (int) (o1.interval - o2.interval);
            }
        });
        Job job0 = new Job("job0", 20L, 2, 0, 0, runningJobF, waitingJobs);
        Job job1 = new Job("job1", 25L, 1, 0, 1, runningJobF, waitingJobs);
        Job job2 = new Job("job2", 20L, 1, 20, 2, runningJobF, waitingJobs);
        Job job3 = new Job("job3", 20L, 1, 20, 3, runningJobF, waitingJobs);
        s.addJob(testMap, 0, job0);
        s.addJob(testMap, 1, job1);
        s.addJob(testMap, 2, job2);
        s.addJob(testMap, 3, job3);

        Worker worker1 = new Worker(runningJobF, waitingJobs);
        Worker worker2 = new Worker(runningJobF, waitingJobs);
        Worker worker3 = new Worker(runningJobF, waitingJobs);

        job0.start();
        job1.start();
        job2.start();
        job3.start();

        worker1.start();
        worker2.start();
        worker3.start();
        s.updateJob(testMap, 1, job3);
        s.deleteJob(testMap, 1);
        s.getJob(testMap, 2);
    }


}

