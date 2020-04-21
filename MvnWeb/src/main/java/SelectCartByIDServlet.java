import SqlTools.SqlHelper;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SelectCartByIDServlet")
public class SelectCartByIDServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=String.valueOf(request.getParameter("userId"));
        try {
            List<Map<String,Object>>list =getResult(id);
            String jsonString= JSON.toJSONString(list);
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

    protected List<Map<String,Object>> getResult(String id) throws SQLException {
        //List<Map<String,Object>> outerList= new ArrayList<>();
        List<Map<String,Object>> list=new ArrayList<>();
        SqlHelper sqlHelper=new SqlHelper();
        try {
            sqlHelper.ConnectData();
            String sqlCommand="select * from shopingCart where cu_Id = '"+id+"'";
            System.out.println(sqlCommand);
            ResultSet resultSet=sqlHelper.GetResultSet(sqlCommand);
            if (resultSet.next()){
                do {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("id",resultSet.getInt("item_ID"));
                    map.put("name",""+resultSet.getString("name"));
                    map.put("quantity",resultSet.getInt("quantity"));
                    map.put("img",""+resultSet.getString("img"));
                    map.put("price",resultSet.getInt("price"));
                    list.add(map);
                }while (resultSet.next());
                resultSet.close();
                sqlHelper.CloseConnect();
            } else{

            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            List<Map<String,Object>>list=new SelectCartByIDServlet().getResult("1");
            String jsonString= JSON.toJSONString(list);
            System.out.println(jsonString);
        }catch (Exception e){}
    }
}
