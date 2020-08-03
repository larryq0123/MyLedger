package com.larry.larrylibrary.thread;

import com.larry.larrylibrary.util.BaseLogUtil;

import java.util.concurrent.*;


public class ExecutorPool implements RejectedExecutionHandler {
    private static final String TAG = ExecutorPool.class.getName();
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static ExecutorPool sInstance;

    public static ExecutorPool getInstance() {
        if (sInstance == null) {
            synchronized (ExecutorPool.class) {
                sInstance = new ExecutorPool();
            }
        }

        return sInstance;
    }

//    private final ThreadPoolExecutor mForBackgroundTasks;
    private final ThreadPoolExecutor mForLightWeightBackgroundTasks;
    private final Executor mMainThreadExecutor;



    private ExecutorPool() {
        ThreadFactory backgroundPriorityThreadFactory = new
                PriorityThreadFactory(android.os.Process.THREAD_PRIORITY_BACKGROUND);

//        // setting the thread pool executor for mForBackgroundTasks;
//        mForBackgroundTasks = new ThreadPoolExecutor(
//                NUMBER_OF_CORES * 4,
//                NUMBER_OF_CORES * 16,
//                60L,
//                TimeUnit.SECONDS,
//                new LinkedBlockingQueue<Runnable>(),
//                backgroundPriorityThreadFactory,
//                this
//        );

        // setting the thread pool executor for mForLightWeightBackgroundTasks;
        mForLightWeightBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 8,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory,
                this
        );

        // setting the thread pool executor for mMainThreadExecutor;
        mMainThreadExecutor = new MainThreadExecutor();
    }

//    /*
//    * returns the thread pool executor for background task
//    */
//    public ThreadPoolExecutor forBackgroundTasks() {
//        return mForBackgroundTasks;
//    }

    /*
    * returns the thread pool executor for light weight background task
    */
    public ThreadPoolExecutor forLightWeightBackgroundTasks() {
        return mForLightWeightBackgroundTasks;
    }

    /*
    * returns the thread pool executor for main thread task
    */
    public Executor forMainThreadTasks() {
        return mMainThreadExecutor;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        BaseLogUtil.loge(TAG, "rejectedExecution: ");
    }
}