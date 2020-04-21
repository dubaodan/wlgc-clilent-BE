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

@WebServlet(name = "GetTopItemServlet")
public class GetTopItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Map<String,Object>>list =getResult();
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

    protected List<Map<String,Object>> getResult() throws SQLException {
        List<Map<String,Object>> listTotal= new ArrayList<>();
        List<Map<String,Object>> list=new ArrayList<>();
        SqlHelper sqlHelper=new SqlHelper();
        try {
            sqlHelper.ConnectData();
            String sqlCommand="select * from item";
            ResultSet resultSet=sqlHelper.GetResultSet(sqlCommand);
            if (resultSet.next()){
                do {
                    Map<String,Object> map = new HashMap<String,Object>();
                    Integer id=resultSet.getInt("item_ID");
                    if(id ==5 || id == 8){
                    map.put("id",id);
                    if(id == 5)
                        map.put("img","http://localhost:8080/MvnWeb_war/imgs/topitem1.png");
                    else
                        map.put("img","http://localhost:8080/MvnWeb_war/imgs/topitem2.png");
                    map.put("name",""+resultSet.getString("item_Name"));
                    map.put("introduction",""+resultSet.getString("introduction"));
                    list.add(map);
                    }
                }while (resultSet.next());
                resultSet.close();
                sqlHelper.CloseConnect();
            } else{

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String,Object> outerMap=new HashMap<>();
        outerMap.put("itemList",list);
        listTotal.add(outerMap);
        return listTotal;
    }

    public static void main(String[] args) {
        try {
            List<Map<String,Object>>list=new GetItemListServlet().getResult(1);
            String jsonString= JSON.toJSONString(list);
            System.out.println(jsonString);
        }catch (Exception e){}
    }
}
