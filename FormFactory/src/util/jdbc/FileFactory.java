package util.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;


import javax.swing.filechooser.FileSystemView;

public class FileFactory {
	public static Map<String, String> getFormData(InputStream fileSource) throws IOException {

		if(fileSource==null) {
			return null;
		}
        //建立随机文件名存储二进制流
		String timestamp=((Long)System.currentTimeMillis()).toString();
		String path=FileSystemView.getFileSystemView().getHomeDirectory().getPath()+"\\tempfile"+timestamp+".txt";
	    System.out.println("桌面二进制文件 : "+path);
        //    新建一个临时文件,用输出流指向这个文件
        //    建一个文件
        File tempFile =new File( path );
        //    用输出流指向这个文件
        FileOutputStream outputStream = new FileOutputStream( tempFile );
        //我们就每次读写10K,我们的文件小，这个就已经够用了
        byte[] bb = new byte[1024*100];
        int n = 0 ;
       //读写文件,-1标识为空
        while( (n =fileSource.read(bb) ) != -1 ) {
            outputStream.write(bb, 0, n);
        }
        // 关闭流
        fileSource.close();
        outputStream.close();
        	/**
        	 * 开始从临时文本解析结果
        	 */
				//获取文件对象
			    File SourceData=new File(path); 
			    //使用randomaccessFile 读入流
				RandomAccessFile raf=new RandomAccessFile(SourceData, "r");
				//第一行是分割符
				String segmentationrow=raf.readLine();
				System.out.println("分隔符:"+segmentationrow+" length: "+segmentationrow.length());
				//所有解析出来的结果都存入map
				Map<String,String> FileMap = new HashMap<String,String>();
				/**
				 * 开始遍历整个二进制流
				 */
				String temp;
				boolean flag=true;
				while(flag) {
					temp=raf.readLine();
					//从第二行开始遍历
					System.out.println("templint:"+temp);
					//第二行是类型  判断是文件类型还是普通类型
					int dataposition=temp.lastIndexOf("filename=\"");
					System.out.println("filenameposition : "+dataposition);
					//判断是普通类型还是 文件类型
					if(dataposition!=-1) {
						System.out.println("===========file_data============");
						//文件类型  
						//获取 [文件名字.类型]
						dataposition+=10;//指针在文件名第一个字节上    10代表filename=\"
						int endposition=temp.lastIndexOf("\"");//最后一个"
						String filename=new String(temp.substring(dataposition, endposition).getBytes("ISO-8859-1"),"UTF-8");
						System.out.println(" [文件名字.类型]  :  "+filename);
						//判断获取文件名字是否为空
						if(filename.length()==0 || filename==null) {
							System.out.println("文件为空(请上传文件!)");
							return null;
						}
						
						/**
						 * 截获文件内容
						 * 
						 * ------WebKitFormBoundarydY2VcyMWiXV7HSqN
						   Content-Disposition: form-data; name="wenjian"; filename="QQ鎴浘20190506163444.png"
						   Content-Type: image/png
								指针在这行
						     塒NG
						 * 
						 * 
						 * 读取两行 指针指向正文开始的上一行
						 */
						raf.readLine();raf.readLine();//文件指针  指向正文上一行
						//设置正文start
						Long start=raf.getFilePointer();
					
						/**
						 * 开始遍历  直到分割符停止
						 */
						String pd=raf.readLine();//判断行
						System.out.println("wj_data_fistline : "+ pd);
						while(true) {
							System.out.println("pd:"+pd+"    length:"+pd.length());
							if(pd.equals(segmentationrow) || pd.equals(segmentationrow+"--")) {
								if(pd.equals(segmentationrow+"--")) {flag=false;}
								break;}
							pd=raf.readLine();
						}
						//现在指针在分割符这行   获取结束符上一行位置 jian
						Long end=raf.getFilePointer()-pd.length()-2;//-2是/r/n
						System.out.println("wj_start:"+start+"  wj_end: "+end +"  end-start:"+(end-start));
						byte[]  b=new byte[(int)(end-start)];
						
						raf.seek(start);
						while(start < end) {
							//b=raf.read();
							raf.read(b);
							start=raf.getFilePointer();
						}
						System.out.println("b_length: "+ b.length);
						//对文件流编码
						Encoder encoder = Base64.getEncoder();
						String filesoure=encoder.encodeToString(b);
						FileMap.put("wenjian", filesoure);
						FileMap.put("filename",filename);
						System.out.println("end_nextline:"+raf.readLine());//文件指向分隔符
						 
					}else {
						
						//普通类型   获取name
						System.out.println("===========text_data============");
						dataposition=temp.lastIndexOf("name=\"")+6;
						int endposition=temp.lastIndexOf("\"");//最后一个"
						System.out.println("dataposition: " +dataposition+" endposition:"+endposition);
						String name=temp.substring(dataposition,endposition);
						if(name==null || name.length()==0) {
							System.out.println("name参数没有定义!");
							return null;
						}
						//获取name内容  
						raf.readLine();//文件指针在正文上一行空白行
						
						Long start=raf.getFilePointer();
						String pd=raf.readLine();//判断行
						System.out.println("data_fistline:"+pd);
						while(true) {
							System.out.println("pd:"+pd+"    length:"+pd.length());
							if(pd.equals(segmentationrow) || pd.equals(segmentationrow+"--")) {
								if(pd.equals(segmentationrow+"--")) {flag=false;System.out.println("over_while");}
								break;
								}
							pd=raf.readLine();
						}
						//现在指针在分割符这行   获取结束符上一行位置
						System.out.println("real_end : "+raf.getFilePointer()+"  -pd.length: "+pd.length());
						
						Long end=raf.getFilePointer()-pd.length()-4;//-2是/r/n
						System.out.println("wj_start:"+start+"  wj_end:"+end +  " end-start:"+(end-start));
						byte[]  b=new byte[(int)(end-start)];
						raf.seek(start);
						while(start < end) {
							raf.read(b);
							start=raf.getFilePointer();
						}

						System.out.println(" name :"+name+"-----b.length : "+b.length);
						String filesoure=new String(b);
						FileMap.put(name, filesoure);
						raf.seek(raf.getFilePointer()+2);//加上/r/n到一行的最后才能 用readline()否则为null
						String aa=raf.readLine();
						System.out.println("end_nextline:"+aa);//文件指针 指向 分隔符
					}
				}
				
				//关闭流
				raf.close();
				//while 循环结束
				return FileMap;
		
	}
	
	public static void main(String[] args)throws IOException {
		InputStream s=null;
		/*
		 * FileMap.put("wenjian", filesoure);
		   FileMap.put("filename",filename);
		 * 
		 */
		Map<String,String> temp=getFormData(s);
		//base解码
		Decoder decoder = Base64.getDecoder();
		byte[] b=decoder.decode(temp.get("wenjian"));
		String filename=temp.get("filename");
		File f=new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath(),"string后"+filename);
		FileOutputStream fos=new FileOutputStream(f);
		fos.write(b, 0, b.length);
		fos.flush();
		fos.close();
		System.out.println("=================================");
		//获取key + value
        for (Object key : temp.keySet()) {
            String value = (String)temp.get(key);
            System.out.println(key + " : " + value);
        }
	
	}

}
