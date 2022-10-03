package blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import bean.Customer;
import jdbc.utils.JDBCUtils;

/**
 * 
 * @Description 测试使用PreparedStatement操作Blob类型的数据
 * @author shkstart  Email:shkstart@126.com
 * @version 
 * @date 下午4:08:58
 *
 */
public class BlobTest {
	
	//向数据表customers中插入Blob类型的字段
	@Test
	public void testInsert() throws Exception{
		Connection conn = JDBCUtils.getConnection();
		String sql = "insert into customers(cust_name,cust_email,photo)values(?,?,?)";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setObject(1,"袁浩");
		ps.setObject(2, "yuan@qq.com");
//		ps.setObject(3,"1992-09-08");
		FileInputStream is = new FileInputStream(new File("src\\blob\\girl.jpg"));
		ps.setBlob(3, is);
		
		ps.execute();
		
		JDBCUtils.closeResource(conn, ps);
		
	}
	
	//查询数据表customers中Blob类型的字段
	@Test
	public void testQuery(){
		Connection conn = null;
		PreparedStatement ps = null;
		InputStream is = null;
		FileOutputStream fos = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select cust_name, cust_email, photo from customers where cust_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 10010);
			rs = ps.executeQuery();
			if(rs.next()){
	//			方式一：
	//			int id = rs.getInt(1);
	//			String name = rs.getString(2);
	//			String email = rs.getString(3);
	//			Date birth = rs.getDate(4);
				//方式二：
				String cust_name = rs.getString("cust_name");
				String cust_email = rs.getString("cust_email");
				
				Customer cust = new Customer();
				cust.setCust_name(cust_name);
				cust.setCust_email(cust_email);
				System.out.println(cust);
				
				//将Blob类型的字段下载下来，以文件的方式保存在本地
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream("src\\blob\\zhangyuhao.jpg");
				byte[] buffer = new byte[1024];
				int len;
				while((len = is.read(buffer)) != -1){
					fos.write(buffer, 0, len);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JDBCUtils.closeResource(conn, ps, rs);
		}
		
		
	}
	
}
