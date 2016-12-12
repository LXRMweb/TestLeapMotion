/**
 * @author lxrm
 * @date 2016128
 * @description ���Բ���onFrame()�����е���д�ļ��ĺ���
 * */
import java.io.IOException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;


/**
 * ���ڸ����¼���onInit()�ȣ���鿴LeapMotion API--class Listener
 * */
class CollecteFrameDataListener extends Listener {
    public void onInit(Controller controller) {
    	//System.out.println("ִ��Listener.onInit()");
        System.out.println("Initialized");
        System.out.println("\tThe Controller object is initialized.New Listener is added to the Controller Object");
    }
    
    //Called when the Leap Motion daemon/service connects to your application
    public void onServiceConnect(Controller controller){
    	System.out.println("LeapMotion Service connected");
    }
    
    public void onConnect(Controller controller) {
    	//System.out.println("controller�����Ѿ�������Leap Motionϵͳ������ִ��Listener�����onConnect()����");
        System.out.println("Connected");
        System.out.println("\tThe Controller connects to the Leap Motion service/daemon and the Leap Motion hardware is attached.");
        //System.out.println("��onConnect()�����е���controller.enableGesture()����leapMotionϵͳ���ض����Ƶ�ʶ����");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected��leap motionϵͳ����رջ���Ӳ���豸�Ͽ�����");
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
	    	System.out.println("ִ��TestLeapMotionFrameFileOperation���main");
	        // Create a sample listener and controller
	    	CollecteFrameDataListener listener = new CollecteFrameDataListener();
	        Controller controller = new Controller();//When you create a Controller object,
	        							//it automatically connects to the Leap Motion service 

	        // Have the sample listener receive events from the controller
	        //Each time a Controller event occurs,one function in the listener will be called
	        /*���磬��controller���������һ��Listener����ʱ����listener�����onInit()�ͻᱻ����ִ��
	         *		�������ʱcontroller�����Ѿ���������leap motionϵͳ����ôִ����onInit()֮����������ִ��listener�����onConnect()
	         *		������listener����ʱcontroller����û��������leap motion ϵͳ����ôִ����onInit()֮���ȴ�
	         *		�ȴ���ֱ��controller����������leap motionϵͳ֮�󣬴���ִ��onConnect()
	         */
	        controller.addListener(listener);
	     
	        /**
	         * bug1:BufferedReader��ͻ
	         * 		main����һ��BufferedReader���󣬵ȴ���׼����Ϊ���еġ�exit��ʱ������main�е�whileѭ����main�߳�Ҳ����ִ���������������
	         * 		LeapMotionFrameFileOperation���е�writeFile()������Ҳ������һ��bufferedReader��������һ����������е�BufferedReader����
	         * 		�ͺ�main�е�BufferedReader�����ͻ�ˣ������ļ���ʱ��writeFile()�е�bufferedReader�����������׼������������ַ���
	         */
	        /*
	        // Keep this process running until "exit" is entered
	        System.out.println("Enter ��exit�� to quit...");
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
	        for(int i=0;i<40000;i++){//������ʱ�䣬ʹ�������ȶ�֮����дFrame���ݵ��ļ�
	    	}
	    	System.out.println("׼����ʼд�ļ�...");
	    	LeapMotionFrameFileOperation frameFileOperation=new LeapMotionFrameFileOperation(controller);
	    	String path=null;
	    	try {
	    		path=frameFileOperation.writeFile(20);//ÿ��onFrame()ʱ������һ�θú�������ȡ����һ֡�Լ���ʷ��9֡���ݣ����л���д����Ӧ�ļ���
	    	} catch (IOException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}   	
	    	
	    }
	
}
