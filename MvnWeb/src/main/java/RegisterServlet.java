import SqlTools.SqlHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterServlet extends javax.servlet.http.HttpServlet {


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String user=String.valueOf(request.getParameter("username"));
        String password=String.valueOf(request.getParameter("password"));
        String phoneNumber=String.valueOf(request.getParameter("phoneNumber"));
        try {
            int ret=executeInsert(user,phoneNumber,password);
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            Map<String,Object> map= new HashMap<>();
            map.put("stat",ret);
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

    protected int executeInsert(String user,String telNumber,String password) throws SQLException {
        int result=0;
        SqlHelper sqlHelper=new SqlHelper();
        try {
            sqlHelper.ConnectData();
            String sqlCommand= "insert into customer (cu_Name,passwword,telephone)values('"+user+"','"+password+"','"+telNumber+"');";
            System.out.println(sqlCommand);
            int res=sqlHelper.GetResultSetOfUpdate(sqlCommand);
            if (res!=0){
                result=1;
            }
            else {
                result=0;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            int a=new RegisterServlet().executeInsert("dubaodan2","123421","woshinaotan");
            System.out.println(a);
        }catch (Exception e){};

    }
}
