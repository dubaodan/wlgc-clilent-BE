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

@WebServlet(name = "GetItemListServlet")
public class GetItemListServlet extends HttpServlet {
    private final int itemsEachPage=8;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           int pageNum=Integer.parseInt(request.getParameter("pageNum"));
           List<Map<String,Object>>list =getResult(pageNum);
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

    protected List<Map<String,Object>> getResult(int pageNum) throws SQLException {
        int startrow=(pageNum-1)*itemsEachPage;
        List<Map<String,Object>> listTotal= new ArrayList<>();
        List<Map<String,Object>> list=new ArrayList<>();
        SqlHelper sqlHelper=new SqlHelper();
        try {
            sqlHelper.ConnectData();
            String sqlCommand="select * from item limit "+0+" , "+itemsEachPage*pageNum;
            ResultSet resultSet=sqlHelper.GetResultSet(sqlCommand);
            //user doesnt exist
            if (resultSet.next()){
                do {
                    Map<String,Object> map = new HashMap<String,Object>();
                    Integer id=resultSet.getInt("item_ID");
                    map.put("id",id);
                    map.put("name",""+resultSet.getString("item_Name"));
                    map.put("quantity",resultSet.getInt("quantity"));
                    map.put("img",""+resultSet.getString("img"));
                    map.put("oldPrice",resultSet.getInt("oldPrice"));
                    map.put("newPrice",resultSet.getInt("newPrice"));
                    map.put("type",resultSet.getInt("typeInt"));
                    //查询item Rating平均值 以及 rateNumber
                    String sqlCommandOfItem="select * from recoder where item_Id = "+id;
                    //System.out.println(sqlCommandOfItem);
                    ResultSet resultSetTemp=sqlHelper.GetResultSet(sqlCommandOfItem);
                    int ratingnum=0;
                    int ratingTotal=0;
                    float ratingAvg=0.0f;
                    while (resultSetTemp.next()){
                        ratingnum++;
                        int rating=resultSetTemp.getInt("rating");
                        ratingTotal+=rating;
                    }
                    if(ratingnum!=0){
                        ratingTotal=ratingTotal*10/(ratingnum);
                        ratingAvg=ratingTotal*1.0f/10;
                    }
                    map.put("rating",ratingAvg);
                    map.put("ratingNumber",ratingnum);
                    list.add(map);
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
