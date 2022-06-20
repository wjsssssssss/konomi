import java.util.*;

public class Test {
    public static void main(String args[]) throws InterruptedException {
        HashMap<Integer,Job> testMap = new HashMap<>(1000);
        Schedule s = new Schedule(testMap);
        Queue<Job> runningJobF = new LinkedList<>();
        PriorityQueue<Job> waitingJobs = new PriorityQueue<>(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                return (int) (o1.interval - o2.interval);
            }
        });
        Job job0 = new Job("job0", 20L,2,0,0,runningJobF,waitingJobs);
        Job job1 = new Job("job1", 25L,1,0,1,runningJobF,waitingJobs);
        Job job2 = new Job("job2", 20L,1,20,2,runningJobF,waitingJobs);
        Job job3 = new Job("job3",20L,1,20,3,runningJobF,waitingJobs);
        s.addJob(testMap,0,job0);
        s.addJob(testMap,1,job1);
        s.addJob(testMap,2,job2);
        s.addJob(testMap,3,job3);

        Worker worker1 = new Worker(runningJobF,waitingJobs);
        Worker worker2 = new Worker(runningJobF,waitingJobs);
        Worker worker3 = new Worker(runningJobF,waitingJobs);

        job0.start();
        job1.start();
        job2.start();
        job3.start();

        worker1.start();
        worker2.start();
        worker3.start();
        s.updateJob(testMap,1,job3);
        s.deleteJob(testMap,1);
        s.getJob(testMap,2);
    }
}