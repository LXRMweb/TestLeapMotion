import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

/**
 * @author lxrm
 * @date 20161128
 * @description  展示FrameFile里的内容（反序列化成Frame对象--->展示Frame对象中各个数据成员的值）*/
public class DisplayFileData {
	public static void main(String[] args) throws IOException{
		/*
    	 * 下面测试反序列化过程是否正确，文件读取-->反序列化-->展示反序列化结果，这涉及到上述class文件中的public List<Frame> readFile(String path)
    	 */
		File root=new File(".");//获得当前文件夹,如DOS系统下cd到的目录
		String rootPath=root.getCanonicalPath();
		//System.out.println("请输入你想要查看的文件的文件名：注意一定要是绝对路径，如d:/file.txt");
		System.out.println("请输入你想要查看的文件的文件名：");
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String file = buf.readLine();
		//String filePath=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData"+File.separator+file;
		String filePath=rootPath+File.separator+file;
    	System.out.println("文件"+filePath+"中的数据：");
    	Controller controller = new Controller(); //An instance must exist
    	LeapMotionFrameFileOperation frameFileOperation=new LeapMotionFrameFileOperation();
    	try {
			List<Frame> frames=frameFileOperation.readFile(file);
			Iterator<Frame> it=frames.iterator();
			while(it.hasNext()){
				frameFileOperation.diaplayFrameInfo((Frame)it.next());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end main
}
