package listener;

import data.DataSet_self;
import data.House_data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @auther 黄勇 湖南师范大学
 * @data 2019/9/6 13:32
 */
public class LearningLs implements ActionListener, MouseListener {

   JTextField input_house_size,predict_house_price,input_real_price,equation;
   String cmd;
   Graphics g;

   JButton bt_get_price,bt_renew_price;
   //数据准备
   DataSet_self extra_housemap=null,housemap =null;
   double  sum_house_size,sum_house_price;		//面积、价格总和
   double average_size,average_price;	//面积、价格平均值
   int number; //数据个数，
   //求斜率的上下两个分式的值
   double slope_up = 0,slope_down = 0;

   double[] house_price = new double[1000];
   double[] house_size = new double[1000];

   private double intercept = 0.0 ;  //截距
   private double slope = 0.0 ;  //斜率
   int x,y;

   public LearningLs(DataSet_self housemap,JTextField input_house_size, JTextField predict_house_price, JTextField input_real_price, JTextField equation) {
      this.housemap = housemap;
      this.input_house_size = input_house_size;
      this.predict_house_price = predict_house_price;
      this.input_real_price = input_real_price;
      this.equation = equation;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      cmd = e.getActionCommand();
      if("输入完毕".equals(cmd))
      {

         get_data();
         seek_data();
         seek_slope_intercept();

         double input_size,real_price;
         input_size = Double.parseDouble(input_house_size.getText());

         double predict_price = predict(input_size);  //预测的价格
         predict_house_price.setText(Double.toString(predict_price));

         real_price = Double.parseDouble(input_real_price.getText());
         double wucha = calc_error(input_size,real_price);


         renewData(input_size, real_price);

         renewData(input_size, real_price);
      }
      else if("更新数据".equals(cmd))
      {

      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {

   }

   @Override
   public void mousePressed(MouseEvent e) {

   }

   @Override
   public void mouseReleased(MouseEvent e) {

   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

   //预测数据
   public double predict(double x){
      return  intercept+slope*x;
   }


   //
   private void get_data()
   {
      Set<Double> sets = housemap.keySet();
      Iterator<Double> iterator = sets.iterator();
      while(iterator.hasNext())
      {
         Double house_key = iterator.next();
         House_data house_object = housemap.get(house_key);
         double house_size = house_object.getHouse_size();
         double house_price = house_object.getHouse_price();
         this.house_price[number] = house_price;
         this.house_size[number] = house_size;
         number++;

         sum_house_size +=house_size;
         sum_house_price +=house_price;
//    		System.out.println("房子面积："+house_size+"，  房子价格:"+house_price);
      }
   }

   //求出这批数据的平均值
   private void seek_data(){
      average_size = sum_house_size/number;
      average_price = sum_house_price/number;

      for(int i = 0;i<number;i++)
      {
         slope_up +=(house_size[i]-average_size)*(house_price[i]-average_price);
         slope_down +=(house_size[i]-average_size)*(house_size[i]-average_size);
      }
   }



   //求回归方程
   private void seek_slope_intercept() {
      if(slope_up!=0&&slope_down!=0)
      {
         slope = slope_up/slope_down;
      }
      intercept = average_price-average_size*slope;
      if(intercept>0)
         System.out.println("回归方程为:"+"y = "+slope+"x+"+intercept);
      else
         System.out.println("回归方程为:"+"y = "+slope+"x"+intercept);
   }

   //误差分析
   public double calc_error(double x, double y) {
      double wucha =predict(x)-y;
      wucha = Math.abs(wucha);
      return wucha;
   }

   //更新数据
   private void renewData(double size,double price)
   {
      //将新数据放到数组里
      this.house_price[number] = price;
      this.house_size[number] = size;

      sum_house_size +=house_size[number];
      sum_house_price +=house_price[number];
      number++;

      average_size = sum_house_size/number;
      average_price = sum_house_price/number;

      slope_up +=(size-average_size)*(price-average_price);
      slope_down +=(size-average_size)*(size-average_size);

      seek_slope_intercept();

   }

      }

