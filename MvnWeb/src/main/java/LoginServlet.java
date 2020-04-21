import SqlTools.SqlHelper;
import com.alibaba.fastjson.JSON;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginServlet extends javax.servlet.http.HttpServlet {



    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String user=String.valueOf(request.getParameter("user"));
        String password=String.valueOf(request.getParameter("password"));
        try {
            int result=getResult(user,password);

            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            Map<String,Object> map= new HashMap<>();
            map.put("stat",result);
            list.add(map);
            String jsonString=JSON.toJSONString(list);
            System.out.println(jsonString);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");// 注意設置為utf-8否則前端接收到的中文為亂碼
            PrintWriter out = response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected int getResult(String user,String password) throws SQLException {
        SqlHelper sqlHelper=new SqlHelper();
        try {
            sqlHelper.ConnectData();
            String sqlCommand="select * from customer where cu_Name = '"+user+"'";
            ResultSet resultSet=sqlHelper.GetResultSet(sqlCommand);
            System.out.println(sqlCommand);
            //user doesnt exist
            if (resultSet.next()){
                do {
                    //ds的龙鸣数据库
                    String psd=resultSet.getString("passwword");
                    int userId = resultSet.getInt("cu_Id");
                    if (password.equals(psd)){
                        resultSet.close();
                        sqlHelper.CloseConnect();
                        return userId;
                    }
                }while (resultSet.next());
                resultSet.close();
                sqlHelper.CloseConnect();
                return 0;
            } else{
                resultSet.close();
                sqlHelper.CloseConnect();
                return 0;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        try {
            int a=new LoginServlet().getResult("dyx","123");
            System.out.println(a);
        }catch (Exception e){};

    }
}
