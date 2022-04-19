package com.xxxx.seckill.utils;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 *
 * @author: LC
 * @date 2022/3/4 3:29 下午
 * @ClassName: UserUtil
 */
public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<TUser> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setRegisterDate(new Date());
            user.setLoginCount(1);
            users.add(user);
        }
        System.out.println("create user");


        // // //插入数据库
         Connection conn = getConn();
         String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         for (int i = 0; i < users.size(); i++) {
         	TUser user = users.get(i);
         	pstmt.setInt(1, user.getLoginCount());
         	pstmt.setString(2, user.getNickname());
         	pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
         	pstmt.setString(4, user.getSalt());
         	pstmt.setString(5, user.getPassword());
         	pstmt.setLong(6, user.getId());
         	pstmt.addBatch();
         }
         pstmt.executeBatch();
         pstmt.close();
         conn.close();
         System.out.println("insert to db");
        //登录，生成userTicket


        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\24215\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            TUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObject());
            System.out.println("create userTicket : " + user.getId());

            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }




    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useSSL=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        String userName = "root";
        String password = "Winky@666";
        String driver = "com.mysql.cj.jdbc.Driver";

        Class.forName(driver);
        return DriverManager.getConnection(url,userName,password);
    }


    public static void main(String[] args) throws Exception {
        createUser(5000);
    }


}
