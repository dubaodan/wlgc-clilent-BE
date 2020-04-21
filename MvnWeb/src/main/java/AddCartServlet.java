import SqlTools.SqlHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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

@WebServlet(name = "GetItemDetailServlet")
public class AddCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID=String.valueOf(request.getParameter("userId"));
        String cart=String.valueOf(request.getParameter("cart"));
        System.out.println("cart是这个哦"+cart);
        try {
            int retString=executeInsert(cart,userID);
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            Map<String,Object> map= new HashMap<>();
            map.put("stat",retString);
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

    protected int executeInsert(String jsonString,String userId) throws SQLException {
        JSONArray jsonArray=(JSONArray)JSONArray.parse(jsonString);
        String cu_id=userId;
        int result = 0;
        try {
            SqlHelper sqlHelperDel = new SqlHelper();
            sqlHelperDel.ConnectData();
            sqlHelperDel.DeleteCart(Integer.parseInt(cu_id));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for (Object jsonObject1:jsonArray){
            JSONObject jsonObject2=(JSONObject)jsonObject1;
            String item_id=jsonObject2.getString("id");
            String name=jsonObject2.getString("name");
            String img=jsonObject2.getString("img");
            String quantity=jsonObject2.getString("quantity");
            String price=jsonObject2.getString("price");
            SqlHelper sqlHelper=new SqlHelper();
            try {
                sqlHelper.ConnectData();
                String sqlCommand2= "insert into shopingcart values" +"("+ item_id+","+cu_id+","+"\'"+name+"\'"+","+quantity+","
                        +"\'"+img+"\'"+","+price+")";
                System.out.println(sqlCommand2);
                int res=sqlHelper.GetResultSetOfUpdate(sqlCommand2);
                if (res!=0){
                    //结果集不为空
                    result=1;
                }
                else {
                    //插入失败
                    result=0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return result;
    }

    public static void main(String[] args) {
        String jsonString="[\n" +
                "      {\n" +
                "        item_id:6,\n" +
                "        name:\"book2\",\n" +
                "        img:\"../../public/img/product/cart-1.jpg\",\n" +
                "        quantity:1,\n" +
                "        price: 100\n" +
                "      },\n" +
                "      {\n" +
                "        item_id:7,\n" +
                "        name:\"book3\",\n" +
                "        img:\"../../public/img/product/2.jpg\",\n" +
                "        quantity:2,\n" +
                "        price: 100\n" +
                "      }\n" +
                "    ]\n";
        try {
            new AddCartServlet().executeInsert(jsonString,"3");
        }catch (Exception e){}

    }

}
