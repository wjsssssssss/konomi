import java.util.*;

//Schedule used hashmap to store all the inserted jobs
public class Schedule {
    public HashMap<Integer, Job> map;

    public Queue<Integer> runningJobsFactory;
    public Schedule(HashMap<Integer, Job> map,Queue<Integer> runningJobsFactory) {
        this.map = map;
        this.runningJobsFactory = runningJobsFactory;
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

    public void deleteJob(HashMap<Integer, Job> map, Integer index,Queue<Integer> runningJobsFactory) {
        map.remove(index);
    }



    public static void main(String args[]) throws InterruptedException {
        HashMap<Integer, Job> testMap = new HashMap<>(1000);
        Queue<Integer> runningJobF = new LinkedList<>();
        Schedule s = new Schedule(testMap,runningJobF);

        PriorityQueue<Integer> waitingJobs = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int) (testMap.get(o1).interval - testMap.get(o2).interval);
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

        Worker worker1 = new Worker(runningJobF, waitingJobs,testMap);
        Worker worker2 = new Worker(runningJobF, waitingJobs,testMap);
        Worker worker3 = new Worker(runningJobF, waitingJobs,testMap);
        s.updateJob(testMap, 1, job3);
        s.deleteJob(testMap, 1,runningJobF);
        s.getJob(testMap, 2);
        job0.start();
        job1.start();
        job2.start();
        job3.start();

        worker1.start();
        worker2.start();
        worker3.start();

    }


}

