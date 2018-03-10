import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <P>Description: . </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/2/8</P>
 * <P>UPDATE DATE: 2018/2/8</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        methodA();
        TimeUnit.SECONDS.sleep(1);
        System.out.println();
        methodB();
        TimeUnit.SECONDS.sleep(1);
        System.out.println();
        methodC();
    }

    public static void methodA() {
        methodB();
    }

    public static void methodB() {
        methodC();
    }

    public static void methodC() {

        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            System.out.println(e.getClassName() + "\t" + e.getMethodName());
        }
    }
}
