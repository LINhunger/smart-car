import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by hunger on 2017/8/6.
 */
public class BaiduStar {

    public static void main(String[] args) throws IOException{

        //获取键盘输入
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String str = input.readLine();
            System.out.println("输出信息："+str);
            if ("exit".equals(str)) {
                break;
            }
        }
        input.close();

        int[]  monsters;
        monsters = new int[]{0,1,2,3,4,5,6,7,8,9};

    }
}
