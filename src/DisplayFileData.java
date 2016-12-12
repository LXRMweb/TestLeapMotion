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
 * @description  չʾFrameFile������ݣ������л���Frame����--->չʾFrame�����и������ݳ�Ա��ֵ��*/
public class DisplayFileData {
	public static void main(String[] args) throws IOException{
		/*
    	 * ������Է����л������Ƿ���ȷ���ļ���ȡ-->�����л�-->չʾ�����л���������漰������class�ļ��е�public List<Frame> readFile(String path)
    	 */
		File root=new File(".");//��õ�ǰ�ļ���,��DOSϵͳ��cd����Ŀ¼
		String rootPath=root.getCanonicalPath();
		//System.out.println("����������Ҫ�鿴���ļ����ļ�����ע��һ��Ҫ�Ǿ���·������d:/file.txt");
		System.out.println("����������Ҫ�鿴���ļ����ļ�����");
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String file = buf.readLine();
		//String filePath=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData"+File.separator+file;
		String filePath=rootPath+File.separator+file;
    	System.out.println("�ļ�"+filePath+"�е����ݣ�");
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
