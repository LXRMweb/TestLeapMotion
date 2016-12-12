import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.leapmotion.leap.Frame;

public class Tmp {
	public void testBuf() throws IOException{
		File root=new File(".");//��õ�ǰ�ļ��У�������Ŀ¼�������D:\workspaceOfJavaEclipse\TestLeapMotion
		String rootPath=root.getCanonicalPath();
		//System.out.println(rootPath);
		String path=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData";//Frame�������ݵĴ��Ŀ¼
		System.out.println("LeapMotion�����������ݽ��ᱻ����������Ŀ¼�У�\n"+path);
		File dir=new File(path);
		String fileName;//�������ƶ�Ӧ��frame���ݴ�ŵ��Ը������������ļ���
		//byte[] buffer=new byte[50];//�����������ڴ�ű�׼����·��������ļ�����Ҳ���������ƣ�
		System.out.println("�������������ƣ���Ӧ���ƶ�Ӧ��Frame���ݽ��ᱻ������Ը����������������ļ��У����磬����Ϊgesture1ʱ�����ݽ��ᱻ�����gesture1.txt�ļ���");
		//System.in.read(buffer);
		//fileName=buffer.toString();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		fileName = buf.readLine()+".txt";
		buf.close();
		//fileName=fileName+".txt";
		File file=new File(dir,fileName);
		String absolutePath=path+fileName;//���ڴ������Frame���� ���ļ��ľ���·��
		//�����Ӧ��Ŀ¼�Լ��ļ��Ƿ���ڣ���������ھͽ�����Ӧ��Ŀ¼�Լ��ļ�
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath()+"�Ƿ���ڣ�"+file.exists());
	}
	public static void main(String[] args) throws IOException{
		System.out.println("Enter ��exit�� to quit...");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		String str = buffer.readLine();
		while(!str.equals("exit")){
			System.out.println(str);
			Tmp tmp=new Tmp();
			tmp.testBuf();
			str = buffer.readLine();
		}
		
		 // Keep this process running until "exit" is entered
        
		buffer.close();
	}//end main
}//end class
