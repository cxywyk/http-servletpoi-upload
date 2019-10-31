package com.hill.imp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hill.imp.db.GetConnection;
import com.hill.imp.pojo.Read;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import oracle.net.aso.e;

/**
 * Servlet implementation class GwUser
 */
@WebServlet("/gwUser")
public class GwUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ResourceBundle resource = ResourceBundle.getBundle("config");
	private String fileDir = resource.getString("upload.dir");
	private String MaxSizeStr = resource.getString("MaxSize");
	Integer MaxSize = Integer.parseInt(MaxSizeStr);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GwUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//PrintWriter p = response.getWriter();
		String proid=request.getParameter("type");
		System.out.println("proid======="+proid);
		try {
			doUpload(request);
			response.sendRedirect("/nwMyd/UploadExcel");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//p.write("保存数据成功,可以进行下载"); //成功
		//p.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void doUpload(HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException {
		String thisname = "excel" + System.currentTimeMillis(); // 文件名称
		request.getSession().setAttribute("rest", thisname);
		String realPath = request.getSession().getServletContext().getRealPath("/upload"); // 文件路径
		request.getSession().setAttribute("path", realPath);
		File  filepath =new File(realPath);
		if(!filepath.exists())
		{
			filepath.mkdirs();
		}
		Connection con = (Connection) new GetConnection().getCon(); // 获取连接
		String sql = "select  item_项目名称,item_需求类型,item_需求标题,item_需求优先级  ,statelabel ,item_现状,item_需求描述 ,item_预计工作量,id     from `tlk_所有需求_创建需求`";
		PreparedStatement stm = (PreparedStatement) con.prepareStatement(sql);// 执行sql语句预编译放注入
		ResultSet executeQuery = stm.executeQuery(); // 执行
		List<Read> list = new ArrayList<Read>(); // read类的集合不存储需求评论
		FileOutputStream file = new FileOutputStream(realPath + File.separator + thisname + ".xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(); // 状态工作簿
		XSSFSheet sheet = workbook.createSheet(); // 创建工作表单
		XSSFRow row = sheet.createRow(0); // 创建HSSFRow对象 （行）
		// 创建多个单元格充当列头
		XSSFCell rowCell = row.createCell(0); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("系统");
		rowCell = row.createCell(1); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("系统2");
		rowCell = row.createCell(1); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("需求类别");
		rowCell = row.createCell(2); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("需求标题");
		rowCell = row.createCell(3); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("紧急程度");
		rowCell = row.createCell(4); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("需求描述");
		rowCell = row.createCell(5); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("跟进内容");
		rowCell = row.createCell(6); // 创建XSSFCell对象(单元格)
		rowCell.setCellValue("状态");
		// 赋值阶段
		while (executeQuery.next()) {
			Read read = new Read();
			read.setProname(executeQuery.getString(1));
			read.setType(executeQuery.getString(2));
			read.setTitle(executeQuery.getString(3));
			read.setPrecedence(executeQuery.getString(4));
			read.setStatus(executeQuery.getString(5));
			read.setXianzhuang(executeQuery.getString(6));
			read.setDescription(executeQuery.getString(7));
			read.setYujidate(executeQuery.getInt(8));
			read.setId(executeQuery.getString(9));
			list.add(read); // 添加内容
		}
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < 1; j++) {
				row = sheet.createRow(i + 1);// 每次都在+1
				rowCell = row.createCell(0);
				rowCell.setCellValue(list.get(i).getProname()); // 系统
				rowCell = row.createCell(1);
				rowCell.setCellValue(list.get(i).getType()); // 类型
				rowCell = row.createCell(2);
				rowCell.setCellValue(list.get(i).getTitle()); // 标题
				rowCell = row.createCell(3);
				rowCell.setCellValue(list.get(i).getPrecedence()); // 紧急度
				rowCell = row.createCell(4);
				rowCell.setCellValue("现状:" + list.get(i).getXianzhuang() + "\n需求:" + list.get(i).getDescription()
						+ "\n计划时间:" + list.get(i).getYujidate()); // 需求描述
				rowCell = row.createCell(5);
				// 取评论阶段
				String newsql = "select item_评论内容 from `tlk_所有需求_评论区`  where  item_需求id like '" + list.get(i).getId()
						+ "'"; // 通过需求id取评论
				stm = (PreparedStatement) con.prepareStatement(newsql);// 执行sql语句预编译放注入
				executeQuery = stm.executeQuery(); // 执行
				StringBuffer stu = new StringBuffer();
				int number = 1;
				while (executeQuery.next()) {
					stu.append(number + "." + executeQuery.getString(1) + "\n");
					number++;
				}
				// 赋值评论
				rowCell.setCellValue(stu.toString()); // 需求跟进
				rowCell = row.createCell(6);
				rowCell.setCellValue(list.get(i).getStatus()); // 需求状态
			}
		}
		workbook.write(file);
		file.close();
	}
}
