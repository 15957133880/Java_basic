package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 例子：创建三个窗口卖票，总票数为100张.使用实现Runnable接口的方式
 * 存在线程的安全问题，待解决。
 *
 * @author shkstart
 * @create 2019-02-13 下午 4:47
 */
class Window1 implements Runnable{

    private int ticket = 100;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition;

    @Override
    public void  run() {
        condition = lock.newCondition();
        while(true){
            lock.lock();
            try {
                if (ticket > 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    while (ticket > 10) {
//                        try {
//                            condition.await();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    System.out.println(Thread.currentThread().getName() + ":卖票，票号为：" + ticket);
                    ticket--;
//                    condition.signalAll();
                } else {
                    break;
                }
            }
            finally {
                lock.unlock();
            }
        }

    }
}


public class WindowTest1 {
    public static void main(String[] args) {
        Window1 w = new Window1();

        Thread t1 = new Thread(w);
        Thread t2 = new Thread(w);
        Thread t3 = new Thread(w);

        t1.setName("窗口1");
        t2.setName("窗口2");
        t3.setName("窗口3");

        t1.start();
        System.out.println(t1.getState());
        t2.start();
        t3.start();

    }

}
