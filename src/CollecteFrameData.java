/**
 * @author lxrm
 * @date 2016128
 * @description 尝试不在onFrame()函数中调用写文件的函数
 * */
import java.io.IOException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;


/**
 * 关于各种事件如onInit()等，请查看LeapMotion API--class Listener
 * */
class CollecteFrameDataListener extends Listener {
    public void onInit(Controller controller) {
    	//System.out.println("执行Listener.onInit()");
        System.out.println("Initialized");
        System.out.println("\tThe Controller object is initialized.New Listener is added to the Controller Object");
    }
    
    //Called when the Leap Motion daemon/service connects to your application
    public void onServiceConnect(Controller controller){
    	System.out.println("LeapMotion Service connected");
    }
    
    public void onConnect(Controller controller) {
    	//System.out.println("controller对象已经连接上Leap Motion系统，所以执行Listener对象的onConnect()函数");
        System.out.println("Connected");
        System.out.println("\tThe Controller connects to the Leap Motion service/daemon and the Leap Motion hardware is attached.");
        //System.out.println("在onConnect()函数中调用controller.enableGesture()开启leapMotion系统对特定手势的识别功能");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected，leap motion系统软件关闭或者硬件设备断开连接");
    }
    
    
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }
    
    /*
     * Called when a new frame of hand and finger tracking data is available.
     * Note, the Controller skips any pending onFrame events while
     *  your onFrame handler executes. If your implementation takes too long to return,
     *   one or more frames can be skipped. The Controller still inserts the skipped frames 
     *   into the frame history. You can access recent frames 
     *   by setting the history parameter when calling the Controller::frame() function. 
     *   You can determine if any pending onFrame events were skipped by comparing 
     *   the ID of the most recent frame with the ID of the last received frame.
     *   */
    public void onFrame(Controller controller) {
    	
    }//end onFrame()
}//end class

public class CollecteFrameData {
	 public static void main(String[] args) throws IOException {
	    	System.out.println("执行TestLeapMotionFrameFileOperation类的main");
	        // Create a sample listener and controller
	    	CollecteFrameDataListener listener = new CollecteFrameDataListener();
	        Controller controller = new Controller();//When you create a Controller object,
	        							//it automatically connects to the Leap Motion service 

	        // Have the sample listener receive events from the controller
	        //Each time a Controller event occurs,one function in the listener will be called
	        /*例如，向controller对象中添加一个Listener对象时，该listener对象的onInit()就会被触发执行
	         *		且如果这时controller对象已经连接上了leap motion系统，那么执行完onInit()之后会继续触发执行listener对象的onConnect()
	         *		如果添加listener对象时controller对象还没有连接上leap motion 系统，那么执行完onInit()之后会等待
	         *		等待，直到controller对象连接上leap motion系统之后，触发执行onConnect()
	         */
	        controller.addListener(listener);
	     
	        /**
	         * bug1:BufferedReader冲突
	         * 		main中有一个BufferedReader对象，等待标准输入为单行的“exit”时，跳出main中的while循环，main线程也会在执行完后续命令后结束
	         * 		LeapMotionFrameFileOperation类中的writeFile()函数中也声明了一个bufferedReader对象，这样一来这个函数中的BufferedReader对象
	         * 		就和main中的BufferedReader对象冲突了，输入文件名时，writeFile()中的bufferedReader对象读不到标准输入流输入的字符串
	         */
	        /*
	        // Keep this process running until "exit" is entered
	        System.out.println("Enter “exit” to quit...");
	        BufferedReader buf2 = new BufferedReader(new InputStreamReader(System.in));
			String str = buf2.readLine();
			while(!str.equals("exit")){
				System.out.println(str);
				str = buf2.readLine();
			}
			buf2.close();
			*/
	       
	        // Remove the sample listener when done
	        //controller.removeListener(listener);
	        for(int i=0;i<40000;i++){//等若干时间，使得数据稳定之后再写Frame数据到文件
	    	}
	    	System.out.println("准备开始写文件...");
	    	LeapMotionFrameFileOperation frameFileOperation=new LeapMotionFrameFileOperation(controller);
	    	String path=null;
	    	try {
	    		path=frameFileOperation.writeFile(20);//每次onFrame()时，调用一次该函数，读取最新一帧以及历史中9帧数据，序列化后写到相应文件中
	    	} catch (IOException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}   	
	    	
	    }
	
}
