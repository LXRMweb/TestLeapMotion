import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

/**
 * @author lxrm
 * @date 20161115
 * @description 
 * 		��ʼ��̣�����leap motion ��JAVA API docs ����Frame Object�����л��뷴���л�demo����д�˳���
 * 			������,
 * 				1) public void writeFile()
 * 					��leap motion����application��Frame��������л���byte[]��
 * 					 ���ֽ��������ʽ��������ļ��У�
 * 					����ÿ��Frame�����ʵ�ʳ��Ȳ�ȷ��������Ҫ�ڴ洢Frame���ݵ�ͬʱҲ�洢��֡���ݵĳ��ȣ�
 * 					ֻ������������ȷ�ط����л���
 * 					һ���ļ��п��Դ������Frame���л�����
 * 				2) public List<Frame> readFile(String path)
 * 					���洢���л�Frame���ݵ��ļ������л������List<Frame>����
 * 					*/
public class LeapMotionFrameFileOperation {
	//������
	private Controller controller;
	//���캯��
	/**
	 * @param controller:Controller ����leapMotion�Ŀ��������󣬳�ʼ��ʱ����ö�����Ϊ�˺������ʹ�øö����ȡFrame data*/
	public LeapMotionFrameFileOperation(Controller controller){
		this.controller=controller;
	}
	public LeapMotionFrameFileOperation(){
	}
	//��Ա����
	/**
	 * @function �������������������ļ��д�����л����Frame����
	 * 			   ����Ӽ�����������������Ϊ�ļ���
	 * 			   �洢�����ݵ��ļ��������ݸ�ʽ�������ģ�4 byte��serializedFrame.length
	 * 									   length byte��serializedFrame����
	 * 									   4�ֽڵ�֡����
	 * 									   length�ֽڵ�Frame����
	 * 									   ...
	 * 			     ÿ���������ļ�������洢����֡���л�֮���Frame���ݣ��Ҿ���֡���������ƣ���Ϊ�����Ӧ�ļ��Ѿ����ڣ������ԭ�ļ���ĩβ��׷�ӡ��µ����ݶ��Ǹ���ԭ���ļ���
	 * 		�����Ľ�����ÿ����һ�θú������ʹ����ļ����������󣬴洢����һ֡���ݼ���ǰ���framesNum֡��ʷ���ݣ�����framsNum�Ǳ������Ĳ�����,Ȼ��ر��ļ���������ͷ��ļ����
	 * @param framesNum:int ÿ����һ�θú�����������Ӧ���ļ����������һ֡���ݼ���ǰ���framesNum֡��ʷ����,Ȼ��ر��ļ���������ͷ��ļ����
	 * @return absolutePath:String ����frame�������л�����洢�ɵ��ļ��ľ���·������D:\workspaceOfJavaEclipse\TestLeapMotion/LeapMotionData/FrameData/gesture1.txt
	 * @throws IOException
	 * @�޸�
	 * 		�޸�һ��20161124������ȡϵͳ��Ч�̷���Ϊ�����ļ���ŵĸ�Ŀ¼��Ϊ
	 * 						��ȡ���������ļ�����Ϊ�����ļ���ŵĸ�Ŀ¼
	 * 			 ԭ�룬File[] roots=File.listRoots();//��ȡ���ػ�������Ч�̷�c:/  d:/  ...
	 *				 String rootPath=roots[1].getAbsolutePath();
	 *				 String path=rootPath+"/LeapMotionData/FrameData";//���D:/LeapMotionData/FrameData
	 *			�޸�Ϊ,
	 *				File root=new File(".");//��õ�ǰ�ļ��У�������Ŀ¼�������D:\workspaceOfJavaEclipse\TestLeapMotion
					String rootPath=root.getCanonicalPath();
					String path=rootPath+"/LeapMotionData/FrameData";//���D:\workspaceOfJavaEclipse\TestLeapMotion/LeapMotionData/FrameData
	 * 
	 * 		�޸Ķ���20161124��Ϊ�˽���ļ�����������⣬���ļ����Ķ�ȡ����������Ӧ�޸�
	 * 			ԭ�룬String fileName;//�������ƶ�Ӧ��frame���ݴ�ŵ��Ը������������ļ���
					byte[] buffer=new byte[50];//�����������ڴ�ű�׼����·��������ļ�����Ҳ���������ƣ�
					System.out.println("�������������ƣ���Ӧ���ƶ�Ӧ��Frame���ݽ��ᱻ������Ը����������������ļ��У����磬����Ϊgesture1ʱ�����ݽ��ᱻ�����gesture1.txt�ļ���");
					System.in.read(buffer);
					fileName=buffer.toString()+".txt";
	 * 			�޸�Ϊ��
	 * 				System.out.println("�������������ƣ���Ӧ���ƶ�Ӧ��Frame���ݽ��ᱻ������Ը����������������ļ��У����磬����Ϊgesture1ʱ�����ݽ��ᱻ�����gesture1.txt�ļ���");
					BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
					fileName = buf.readLine()+".txt";
	 * */
	public String writeFile(int framesNum) throws IOException{
		//File[] roots=File.listRoots();//��ȡ���ػ�������Ч�̷�c:/  d:/  ...
		//String rootPath=roots[1].getAbsolutePath();
		File root=new File(".");//��õ�ǰ�ļ��У�������Ŀ¼�������D:\workspaceOfJavaEclipse\TestLeapMotion
		String rootPath=root.getCanonicalPath();
		//System.out.println(rootPath);
		String path=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData";//Frame�������ݵĴ��Ŀ¼
		System.out.println("LeapMotion�����������ݽ��ᱻ����������Ŀ¼�У�\n"+path);
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		System.out.println(path+"�Ƿ���ڣ�"+dir.exists());
		String fileName;//�������ƶ�Ӧ��frame���ݴ�ŵ��Ը������������ļ���
		//byte[] buffer=new byte[50];//�����������ڴ�ű�׼����·��������ļ�����Ҳ���������ƣ�
		System.out.println("�������������ƣ���Ӧ���ƶ�Ӧ��Frame���ݽ��ᱻ������Ը����������������ļ��У����磬����Ϊgesture1ʱ�����ݽ��ᱻ�����gesture1.txt�ļ���");
		//System.in.read(buffer);
		//fileName=buffer.toString();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		fileName = buf.readLine()+".txt";
		System.out.println("�ղŶ�ȡ�����ļ�����"+fileName);
		//fileName=fileName+".txt";
		buf.close();
		String absolutePath=path+File.separator+fileName;//���ڴ������Frame���� ���ļ��ľ���·��
		File file=new File(absolutePath);
		//�����Ӧ��Ŀ¼�Լ��ļ��Ƿ���ڣ���������ھͽ�����Ӧ��Ŀ¼�Լ��ļ�
		System.out.println(absolutePath+"�Ƿ���ڣ�"+file.exists());
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath()+"�Ƿ���ڣ�"+file.exists());
		System.out.println("�����������ļ���д���л����Frame����...");
		//ȷ��������ݵ��ļ��Ѿ�����֮������Ϳ��Դ������������Frame�����������л�֮��д����Ӧ���ļ���
		FileOutputStream out=new FileOutputStream(absolutePath,true);//���õڶ�������Ϊtrue������ÿ��д�ļ�ʱ���ǡ���ԭ�ļ�ĩβ׷�������ݡ����ǡ�����ԭ���ļ�
		//ÿ�ε���writeFile()�����������ļ���дframesSum֡����,����framesNum���ڵ��ñ�������ʱ���ɺ���������ָ����
		for (int f = framesNum; f >= 0; f--) {
		      Frame frame = controller.frame(f);//ͨ��Controller�����ȡFrame Object
		      System.out.println("������֡���ݽ���д��"+absolutePath+"�ļ��У�");
		      this.diaplayFrameInfo(frame);
		      byte[] serializedFrame = frame.serialize();//��Frame Object���л���byte����
		      out.write( ByteBuffer.allocate(4).putInt(serializedFrame.length).array() );//Frame��ʵ�ʳ���
		      out.write(serializedFrame);//���л����Frame����
		}
		out.flush();
		out.close();
		buf.close();
		System.out.println("�Ѿ���"+absolutePath+"�������"+framesNum+"֡���ݣ��ļ�������رգ�����ļ�����Ѿ����ͷ�");
		return absolutePath;
	}//end writeFile()
	
	/**
	 * @function read the frame data from the saved file
	 * @���˼· 
	 * 		To read the frame data from the saved file,
	 *  	you can then read the first 4 bytes of the file to determine
	 *   	how much data to read to get an entire frame. 
	 * 		Simply repeat the process until you reach the end of the file.
	 * @param path:String ����ȡ�ļ��ľ���·��   ��d:/LeapMotionData/FrameData/gesture1.txt 
	 * @throws IOException */
	@SuppressWarnings("null")
	public List<Frame> readFile(String path) throws IOException{
		//System.out.println("����readFile()�����ڲ�");
		List<Frame> frames=new LinkedList<Frame>();//���ڴ洢���ļ������л����ݽ���������Frame����
		//List<Frame> frames=null;
		File file=new File(path);
		if(!file.exists()&&file.isFile()){
			System.out.println("��Ӧ���ļ��������ڣ��������������ļ�·���Ƿ���ȷ��"+path);
			return null;
		}else{//Ҳ������Ӧ�ļ�һ�����ڵ������
			System.out.println(path+"�ļ��Ǵ��ڵģ�����׼��������з����л�...");
			Path filePath=Paths.get(path);
			byte[] data=Files.readAllBytes(filePath);//���ļ�������һ���Զ�����һ��byte�����У����ļ�����ʱ��Ҫע���ڴ��������
			//System.out.println("�������ļ��е�����ȫ���������������һ��byte[]������,���鳤��="+data.length);
			//System.out.print("byte[] data="+data+"\n");
			int c=0;//data:byte[]�������±꣬�൱���ļ�ָ��ĵ�ǰλ��
			int f=0;
			int nextBlockSize=0;//��ʾ������һ֡���ݵĳ��ȣ���λΪbyte��
			if(data.length>4){//��ó�ʼ��serializedFrame.length:int
				/*ԭ���洢���л����Frame����ʱ��Ϊ�˽������з����л�Ҳ�洢��ÿ�����л�Frame����֡�����ݳ���(��λΪbyte)
				 * ���л�֡���ݳ�����һ��4 byte ��intֵ
				 * ���Է����л�ʱ��Ҫ��ȡ4 byte�Ķ�����ֵ��Ȼ��ʹ������ķ�������ת����ʮ��������
				 */
				nextBlockSize=(data[c++] & 0x000000ff) << 24 |
                        	  (data[c++] & 0x000000ff) << 16 |
                        	  (data[c++] & 0x000000ff) <<  8 |
                        	  (data[c++] & 0x000000ff);
				//System.out.println("nextBlockSize="+nextBlockSize+"\tc="+c);
			}
			while (c + nextBlockSize <= data.length){
				  byte[] frameData = Arrays.copyOfRange(data, c, c + nextBlockSize);//ȡ��һ֡����
				 // System.out.println("frameData:byte[]   length="+frameData.length);
			      c += nextBlockSize;//ʹ�������±�����һ֡����  ���ݳ���  λ��
			      Frame newFrame = new Frame();
			      System.out.println("������з����л�...");
			      newFrame.deserialize(frameData);//�����л�����byte������ʽ����ת����Frame Object
			     // System.out.println("�����л��ɹ������һ��Frame����,frame.isvalid()="+newFrame.isValid());
			     // this.diaplayFrameInfo(newFrame);
			     // System.out.println("������Frame�����ŵ�List<Frame>������..\nlist.size="+frames.size());
			      boolean flag=frames.add(newFrame);//�������л�����Frame������ӵ�List<Frame>��
			    //  System.out.println("����Frame�����Ѿ�����ӵ�List<Frame> frames������?"+flag+",frames.size="+frames.size());
			      if(data.length - c > 4){
			    	  nextBlockSize = (data[c++] & 0x000000ff) << 24 |
			    			  		  (data[c++] & 0x000000ff) << 16 |
			    			  		  (data[c++] & 0x000000ff) <<  8 |
			    			  		  (data[c++] & 0x000000ff);  
			      }
			                                              
			}//end while
		}//end if..else..	
		return frames;
	}//end readFile()
	
	//չʾFrame�����ݳ�Ա
	public void diaplayFrameInfo(Frame frame){
		System.out.println("Frame id: " + frame.id()
					        + ", timestamp: " + frame.timestamp()
					        + ", hands: " + frame.hands().count()
					        + ", fingers: " + frame.fingers().count()
					        + ", tools: " + frame.tools().count()
					        + ", gestures " + frame.gestures().count());
		//Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());

            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                }
            }
        }

        // Get tools
        for(Tool tool : frame.tools()) {
            System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }//end switch
        }//end for
        
        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            System.out.println();
        }
        
	}//end displayFrameInfo(Frame frame)
}//end class
