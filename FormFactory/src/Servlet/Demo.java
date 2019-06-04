package Servlet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Base64;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;


import util.jdbc.FileFactory;


/**
 * Servlet implementation class t
 */
@WebServlet("/t")
public class Demo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Demo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1、从request当中获取流信息
        InputStream fileSource = request.getInputStream();
        Map<String,String> temp= FileFactory.getFormData(fileSource);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

}
