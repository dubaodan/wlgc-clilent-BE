import SqlTools.SqlHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sun.util.resources.cldr.rw.CurrencyNames_rw;

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

@WebServlet(name = "DeleteCartServlet")
public class DeleteCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId=String.valueOf(request.getParameter("userId"));
            int result = getResult(Integer.parseInt(userId));
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            Map<String,Object> map= new HashMap<>();
            map.put("stat",result);
            list.add(map);
            String jsonString=JSON.toJSONString(list);
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

    protected int getResult(int cu_id) throws SQLException {
        int result = 0;
        try {

            SqlHelper sqlHelperDel = new SqlHelper();
            sqlHelperDel.ConnectData();
            sqlHelperDel.DeleteCart(cu_id);
            result = 1;
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            List<Map<String,Object>>list=new GetItemListServlet().getResult(1);
            String jsonString= JSON.toJSONString(list);
            System.out.println(jsonString);
        }catch (Exception e){}
    }
}
