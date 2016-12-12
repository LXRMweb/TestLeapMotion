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
 * 		初始编程：参照leap motion 的JAVA API docs 所述Frame Object的序列化与反序列化demo，编写此程序
 * 			程序功能,
 * 				1) public void writeFile()
 * 					将leap motion传给application的Frame对象给序列化成byte[]，
 * 					 即字节数组的形式，存放至文件中；
 * 					由于每个Frame对象的实际长度不确定，所以要在存储Frame数据的同时也存储该帧数据的长度，
 * 					只有这样才能正确地反序列化；
 * 					一个文件中可以存放若干Frame序列化数据
 * 				2) public List<Frame> readFile(String path)
 * 					将存储序列化Frame数据的文件反序列化，获得List<Frame>集合
 * 					*/
public class LeapMotionFrameFileOperation {
	//数据项
	private Controller controller;
	//构造函数
	/**
	 * @param controller:Controller 这是leapMotion的控制器对象，初始化时传入该对象是为了后面可以使用该对象获取Frame data*/
	public LeapMotionFrameFileOperation(Controller controller){
		this.controller=controller;
	}
	public LeapMotionFrameFileOperation(){
	}
	//成员函数
	/**
	 * @function 向以手势名称命名的文件中存放序列化后的Frame数据
	 * 			   允许从键盘输入手势名称作为文件名
	 * 			   存储了数据的文件，其数据格式是这样的：4 byte的serializedFrame.length
	 * 									   length byte的serializedFrame数据
	 * 									   4字节的帧长度
	 * 									   length字节的Frame数据
	 * 									   ...
	 * 			     每个单独的文件都允许存储若干帧序列化之后的Frame数据，且具体帧数不受限制（因为如果相应文件已经存在，则会在原文件的末尾“追加”新的数据而非覆盖原有文件）
	 * 		（待改进）：每调用一次该函数，就创建文件输入流对象，存储最新一帧数据及其前面的framesNum帧历史数据（其中framsNum是本函数的参数）,然后关闭文件输出流，释放文件句柄
	 * @param framesNum:int 每调用一次该函数，就往相应的文件中添加最新一帧数据及其前面的framesNum帧历史数据,然后关闭文件输出流，释放文件句柄
	 * @return absolutePath:String 返回frame数据序列化结果存储成的文件的绝对路径，如D:\workspaceOfJavaEclipse\TestLeapMotion/LeapMotionData/FrameData/gesture1.txt
	 * @throws IOException
	 * @修改
	 * 		修改一，20161124，将获取系统有效盘符作为数据文件存放的根目录改为
	 * 						获取工程所在文件夹作为数据文件存放的根目录
	 * 			 原码，File[] roots=File.listRoots();//获取本地机所有有效盘符c:/  d:/  ...
	 *				 String rootPath=roots[1].getAbsolutePath();
	 *				 String path=rootPath+"/LeapMotionData/FrameData";//结果D:/LeapMotionData/FrameData
	 *			修改为,
	 *				File root=new File(".");//获得当前文件夹（即工程目录），结果D:\workspaceOfJavaEclipse\TestLeapMotion
					String rootPath=root.getCanonicalPath();
					String path=rootPath+"/LeapMotionData/FrameData";//结果D:\workspaceOfJavaEclipse\TestLeapMotion/LeapMotionData/FrameData
	 * 
	 * 		修改二，20161124，为了解决文件名乱码的问题，将文件名的读取方法作了相应修改
	 * 			原码，String fileName;//具体手势对应的frame数据存放到以该手势命名的文件中
					byte[] buffer=new byte[50];//缓冲区，用于存放标准输入路径输入的文件名（也即手势名称）
					System.out.println("请输入手势名称，相应手势对应的Frame数据将会被存放至以该手势名称命名的文件中：例如，输入为gesture1时，数据将会被存放至gesture1.txt文件中");
					System.in.read(buffer);
					fileName=buffer.toString()+".txt";
	 * 			修改为，
	 * 				System.out.println("请输入手势名称，相应手势对应的Frame数据将会被存放至以该手势名称命名的文件中：例如，输入为gesture1时，数据将会被存放至gesture1.txt文件中");
					BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
					fileName = buf.readLine()+".txt";
	 * */
	public String writeFile(int framesNum) throws IOException{
		//File[] roots=File.listRoots();//获取本地机所有有效盘符c:/  d:/  ...
		//String rootPath=roots[1].getAbsolutePath();
		File root=new File(".");//获得当前文件夹（即工程目录），结果D:\workspaceOfJavaEclipse\TestLeapMotion
		String rootPath=root.getCanonicalPath();
		//System.out.println(rootPath);
		String path=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData";//Frame对象数据的存放目录
		System.out.println("LeapMotion采样所得数据将会被放置在以下目录中：\n"+path);
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		System.out.println(path+"是否存在："+dir.exists());
		String fileName;//具体手势对应的frame数据存放到以该手势命名的文件中
		//byte[] buffer=new byte[50];//缓冲区，用于存放标准输入路径输入的文件名（也即手势名称）
		System.out.println("请输入手势名称，相应手势对应的Frame数据将会被存放至以该手势名称命名的文件中：例如，输入为gesture1时，数据将会被存放至gesture1.txt文件中");
		//System.in.read(buffer);
		//fileName=buffer.toString();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		fileName = buf.readLine()+".txt";
		System.out.println("刚才读取到的文件名称"+fileName);
		//fileName=fileName+".txt";
		buf.close();
		String absolutePath=path+File.separator+fileName;//用于存放手势Frame数据 的文件的绝对路径
		File file=new File(absolutePath);
		//检查相应的目录以及文件是否存在，如果不存在就建立相应的目录以及文件
		System.out.println(absolutePath+"是否存在："+file.exists());
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath()+"是否存在："+file.exists());
		System.out.println("下面往上述文件中写序列化后的Frame数据...");
		//确定存放数据的文件已经存在之后，下面就可以创建输出流，将Frame对象数据序列化之后写到相应的文件中
		FileOutputStream out=new FileOutputStream(absolutePath,true);//设置第二个参数为true，这样每次写文件时都是“在原文件末尾追加新数据”而非“覆盖原有文件
		//每次调用writeFile()函数，就往文件中写framesSum帧数据,其中framesNum是在调用本函数的时候由函数参数来指定的
		for (int f = framesNum; f >= 0; f--) {
		      Frame frame = controller.frame(f);//通过Controller对象获取Frame Object
		      System.out.println("下面这帧数据将被写入"+absolutePath+"文件中：");
		      this.diaplayFrameInfo(frame);
		      byte[] serializedFrame = frame.serialize();//将Frame Object序列化成byte数组
		      out.write( ByteBuffer.allocate(4).putInt(serializedFrame.length).array() );//Frame的实际长度
		      out.write(serializedFrame);//序列化后的Frame数据
		}
		out.flush();
		out.close();
		buf.close();
		System.out.println("已经往"+absolutePath+"中添加了"+framesNum+"帧数据，文件输出流关闭，相关文件句柄已经被释放");
		return absolutePath;
	}//end writeFile()
	
	/**
	 * @function read the frame data from the saved file
	 * @编程思路 
	 * 		To read the frame data from the saved file,
	 *  	you can then read the first 4 bytes of the file to determine
	 *   	how much data to read to get an entire frame. 
	 * 		Simply repeat the process until you reach the end of the file.
	 * @param path:String 待读取文件的绝对路径   如d:/LeapMotionData/FrameData/gesture1.txt 
	 * @throws IOException */
	@SuppressWarnings("null")
	public List<Frame> readFile(String path) throws IOException{
		//System.out.println("进入readFile()函数内部");
		List<Frame> frames=new LinkedList<Frame>();//用于存储从文件中序列化数据解析出来的Frame对象
		//List<Frame> frames=null;
		File file=new File(path);
		if(!file.exists()&&file.isFile()){
			System.out.println("相应的文件并不存在，请检查您给出的文件路径是否正确："+path);
			return null;
		}else{//也就是相应文件一定存在的情况下
			System.out.println(path+"文件是存在的，下面准备对其进行反序列化...");
			Path filePath=Paths.get(path);
			byte[] data=Files.readAllBytes(filePath);//将文件中数据一次性读出到一个byte数组中，当文件过大时，要注意内存溢出问题
			//System.out.println("将上述文件中的内容全部读出，并存放在一个byte[]数组中,数组长度="+data.length);
			//System.out.print("byte[] data="+data+"\n");
			int c=0;//data:byte[]的数组下标，相当于文件指针的当前位置
			int f=0;
			int nextBlockSize=0;//表示接下来一帧数据的长度（单位为byte）
			if(data.length>4){//获得初始的serializedFrame.length:int
				/*原本存储序列化后的Frame数据时，为了将来进行反序列化也存储了每个序列化Frame数据帧的数据长度(单位为byte)
				 * 序列化帧数据长度是一个4 byte 的int值
				 * 所以反序列化时就要读取4 byte的二进制值，然后使用下面的方法将其转换成十进制数字
				 */
				nextBlockSize=(data[c++] & 0x000000ff) << 24 |
                        	  (data[c++] & 0x000000ff) << 16 |
                        	  (data[c++] & 0x000000ff) <<  8 |
                        	  (data[c++] & 0x000000ff);
				//System.out.println("nextBlockSize="+nextBlockSize+"\tc="+c);
			}
			while (c + nextBlockSize <= data.length){
				  byte[] frameData = Arrays.copyOfRange(data, c, c + nextBlockSize);//取出一帧数据
				 // System.out.println("frameData:byte[]   length="+frameData.length);
			      c += nextBlockSize;//使得数组下标编程下一帧数据  数据长度  位置
			      Frame newFrame = new Frame();
			      System.out.println("下面进行反序列化...");
			      newFrame.deserialize(frameData);//反序列化，有byte数组形式数据转化成Frame Object
			     // System.out.println("反序列化成功，获得一个Frame对象,frame.isvalid()="+newFrame.isValid());
			     // this.diaplayFrameInfo(newFrame);
			     // System.out.println("将上述Frame对象存放到List<Frame>集合中..\nlist.size="+frames.size());
			      boolean flag=frames.add(newFrame);//将反序列化所得Frame对象添加到List<Frame>中
			    //  System.out.println("上述Frame对象已经被添加到List<Frame> frames集合中?"+flag+",frames.size="+frames.size());
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
	
	//展示Frame的数据成员
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
