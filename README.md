# konomi

The runningJobFactory is a buffer between generated jobs and workers.
The scalability is reinforced by the waiting queue collecting the timed-out jobs. 
And it's implemented through priority-queue to make sure the fewer time interval the thread is, it could got allocated worker source first.

The job is also stored in hashmap to make sure if it will be re-used / changed, and the modification could be tracked easily.

