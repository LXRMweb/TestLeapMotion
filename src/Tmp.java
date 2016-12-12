import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import com.leapmotion.leap.Frame;

public class Tmp {
	public void testBuf() throws IOException{
		File root=new File(".");//获得当前文件夹（即工程目录），结果D:\workspaceOfJavaEclipse\TestLeapMotion
		String rootPath=root.getCanonicalPath();
		//System.out.println(rootPath);
		String path=rootPath+File.separator+"LeapMotionData"+File.separator+"FrameData";//Frame对象数据的存放目录
		System.out.println("LeapMotion采样所得数据将会被放置在以下目录中：\n"+path);
		File dir=new File(path);
		String fileName;//具体手势对应的frame数据存放到以该手势命名的文件中
		//byte[] buffer=new byte[50];//缓冲区，用于存放标准输入路径输入的文件名（也即手势名称）
		System.out.println("请输入手势名称，相应手势对应的Frame数据将会被存放至以该手势名称命名的文件中：例如，输入为gesture1时，数据将会被存放至gesture1.txt文件中");
		//System.in.read(buffer);
		//fileName=buffer.toString();
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		fileName = buf.readLine()+".txt";
		buf.close();
		//fileName=fileName+".txt";
		File file=new File(dir,fileName);
		String absolutePath=path+fileName;//用于存放手势Frame数据 的文件的绝对路径
		//检查相应的目录以及文件是否存在，如果不存在就建立相应的目录以及文件
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath()+"是否存在："+file.exists());
	}
	public static void main(String[] args) throws IOException{
		System.out.println("Enter “exit” to quit...");
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
