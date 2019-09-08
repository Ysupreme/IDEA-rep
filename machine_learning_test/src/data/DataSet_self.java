package data;

/**
 * @auther 黄勇 湖南师范大学
 * @data 13:34
 */
import java.util.HashMap;

public class DataSet_self extends HashMap<Double, House_data>{
   public static double number = 0;

   public static double getNumber() {
      return number;
   }


   public void addData(double house_size,double house_price)
   {
      House_data housedata = new House_data();
      housedata.setHouse_price(house_price);
      housedata.setHouse_size(house_size);
//		this.put(number++, housedata);
      this.put(housedata.house_size, housedata);
   }

}