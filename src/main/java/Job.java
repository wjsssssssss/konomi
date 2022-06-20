import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
//if jobs more than 1000, go wait in waitingJobs
public class Job extends Thread {
    public Queue<Integer> runningJobsFactory;
    public HashMap<Integer,Job> map;
    private int count=0;
    public Integer index;
    public long causeTime = 2;
    boolean status = false;
    private String content;
    Integer times;
    Integer interval;
    public Boolean oneTime=true;
    public PriorityQueue<Integer> waitingJobs;
    public Job(String content, long causeTime, Integer times, Integer interval, Integer index, Queue<Integer> runningJobsFactory, PriorityQueue<Integer> waitingJobs){
        this.causeTime = causeTime;
        this.times = times;
        this.content=content;
        this.interval = interval;
        this.runningJobsFactory = runningJobsFactory;
        this.index =index;
        this.waitingJobs = waitingJobs;

    }
    public String getContent(){
        return content;
    }
    public void oneTime() throws InterruptedException {
        System.out.println(getContent());
        synchronized (this) {
            this.wait(10000);
        }
        synchronized (this) {
            this.notify();
        }
    }



    @Override
    public void run() {
        if (runningJobsFactory.size() < 1000) {
            synchronized (runningJobsFactory) {
                if (runningJobsFactory.size() > 0) {
                    try {
                        runningJobsFactory.wait(100);
                    } catch (InterruptedException e) {
                        //
                        e.printStackTrace();
                    }
                }
                runningJobsFactory.add(this.index);
                System.out.println(System.currentTimeMillis()+"produce"+this.index);
                // 生产完毕后 唤醒消费者进行消费
                runningJobsFactory.notify();
            }
        }else{
            synchronized (waitingJobs){
                waitingJobs.add(this.index);
                waitingJobs.notify();
                this.yield();
            }

        }


    }
}
