package main;

/**
 * @auther 黄勇 湖南师范大学
 * @data 13:33
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import data.DataSet_self;
import data.House_data;

import java.text.DecimalFormat;

import javax.swing.*;


public class LinearRegression implements ActionListener, MouseListener {

   private final int frame_width = 800,frame_height = 500;
   MyJFrame jf;
   JLabel jl_input_house_size,jl_predict_house_price,jl_input_real_price,jl_equation,jl_flit_pic;
   JTextField input_house_size,predict_house_price,input_real_price,equation;
   Graphics g;

   JButton bt_get_price,bt_renew_price;   //按钮：输入完毕，更新价格

   DataSet_self extra_housemap=null,housemap =null;//数据准备

   //用于最小二乘法求斜率slope，截距intercept的准备数据
   private double intercept = 0.0 ;  //截距
   private double slope = 0.0 ;  //斜率
   double  sum_house_size,sum_house_price;		//面积、价格总和
   double average_size,average_price;	//面积、价格平均值
   double slope_up = 0,slope_down = 0; //求斜率的上下两个分式的值
   int number; //数据个数，

   //将数据存到这两个数组里面
   double[] house_price = new double[1000];
   double[] house_size = new double[1000];

   //格式化数据的小数点位数
   DecimalFormat df =new DecimalFormat("#.00");

   //继承JFrame的一个类，里面重写paint()方法
   class MyJFrame extends JFrame {
      public void paint(Graphics g)
      {
         super.paint(g);
         drawpic(house_price,house_size);
      }
   }


   private void initData()
   {
      if(housemap==null)
      {
         housemap = new DataSet_self();
         housemap.addData(50, 100);
         housemap.addData(55, 120);
         housemap.addData(60, 130);
         housemap.addData(62, 140);
         housemap.addData(68, 150);
         housemap.addData(75, 190);
         housemap.addData(150, 400);
         housemap.addData(90, 250);
         housemap.addData(100, 280);
         housemap.addData(110, 310);
         housemap.addData(120, 370);
      }
   }

   //预测数据
   public double predict(double x){
      return  intercept+slope*x;
   }

   //将基础数据存放到两个数组里面，并求出求斜率slope需要的x,y变量的总和
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
   //求回归方程的斜率，截距
   private void seek_slope_intercept() {
      if(slope_up!=0&&slope_down!=0)
      {
         slope = slope_up/slope_down;
      }
      intercept = average_price-average_size*slope;
   }
//   //误差分析
//   public double calc_error(double x, double y) {
//      double wucha =predict(x)-y;
//      wucha = Math.abs(wucha);
//      return wucha;
//   }

   //更新数据
   private void renewData(double size,double price)
   {
      //将新数据放到数组里
      this.house_price[number] = price;
      this.house_size[number] = size;

      sum_house_size +=house_size[number];
      sum_house_price +=house_price[number];
      number++;
      //计算出新的平均值
      average_size = sum_house_size/number;
      average_price = sum_house_price/number;

      slope_up +=(size-average_size)*(price-average_price);
      slope_down +=(size-average_size)*(size-average_size);
      seek_slope_intercept();
      JOptionPane.showMessageDialog(jf, "回归方程已更新", "提示",JOptionPane.PLAIN_MESSAGE);
      jf.repaint();  //重绘
   }
   //获取到输入的房子面积
   private double get_house_size()
   {
      double input_size;
      input_size = Double.parseDouble(input_house_size.getText());
      return input_size;
   }

   //获取输入到的真实价格
   private double get_house_price()
   {
      double real_price;
      real_price = Double.parseDouble(input_house_size.getText());
      return real_price;
   }

   //显示回归方程
   private void show_equation(double slope,double intercept)
   {
      //格式化数据,保留到小数点后2\位,但是返回的是string
      String w = df.format(slope);
      String b = df.format(intercept);
      if (intercept<0)
         equation.setText("y = "+w+"x"+b);//将JFrame上的回归方程JTextField显示出来
      else
         equation.setText("y = "+w+"x+"+b);//将JFrame上的回归方程JTextField显示出来
   }

   //画出图形
   private  void drawpic(double[] house_price,double[] house_size)
   {
      //画出框架
      g.setColor(Color.black);
      if(house_price!=null){
      g.drawLine(280,400,700,400);
      g.drawLine(280,400,280,20);
      g.setColor(Color.RED);}
      for (int i = 0;i<number;i++)
      {
         g.fillOval((int)house_size[i]+280,frame_height-(int)house_price[i],8, 8);
      }
      int y1 = (int)predict(0);
      int y2 = (int)predict(400);
      g.setColor(Color.green);
      g.drawLine(280,frame_height-y1,680, frame_height-y2);
   }

   private void initUI()
   {
      jf = new MyJFrame();
      jf.setTitle("一元线性回归————房价分析");
      jf.setSize(800,500);
      jf.setLayout(null);

      jl_input_house_size = new JLabel("面积:");
      jl_input_house_size.setBounds(20,20,60,30);
      jl_predict_house_price = new JLabel("预测价格:");
      jl_predict_house_price.setBounds(20,60,60,30);
      jl_input_real_price = new JLabel("实际价格:");
      jl_input_real_price.setBounds(20,100,60,30);
      jl_equation = new JLabel("回归方程:");
      jl_equation.setBounds(20,140,60,30);


      input_house_size = new JTextField(20);
      input_house_size.setBounds(90,20,100,30);
      predict_house_price = new JTextField(20);
      predict_house_price.setBounds(90,60,100,30);
      input_real_price = new JTextField(20);
      input_real_price.setBounds(90,100,100,30);
      equation = new JTextField(20);
      equation.setBounds(90,140,100,30);

      JButton bt_get_price = new JButton("输入完毕");
      bt_get_price.setBounds(10,175,100,30);
      bt_renew_price = new JButton("更正价格");
      bt_renew_price.setBounds(120,175,100,30);

      jf.add(jl_input_house_size);
      jf.add(jl_input_real_price);
      jf.add(jl_predict_house_price);
      jf.add(jl_equation);

      jf.add(input_house_size);
      jf.add(input_real_price);
      jf.add(predict_house_price);
      jf.add(equation);

      jf.add(bt_get_price);
      jf.add(bt_renew_price);

      bt_get_price.addActionListener(this);
      bt_renew_price.addActionListener(this);

      jf.setResizable(false);
      jf.setVisible(true);
      jf.setLocation(400,250);
      g = jf.getGraphics();   //设置画笔
      jf.addMouseListener(this);
//      JOptionPane.showMessageDialog(jf, "已为您准备好分析出的回归方程", "提示",JOptionPane.PLAIN_MESSAGE);

   }

   //显示预测到的房价到JFrame上
   private void show_price_view(double housesize,JTextField jt_predict_price)
   {
      String st_predict_price = df.format(predict(housesize));
      jt_predict_price.setText(st_predict_price+"万元");
   }
   @Override
   public void actionPerformed(ActionEvent e) {
      String cmd = e.getActionCommand();
      if("输入完毕".equals(cmd))
      {
         double input_size = get_house_size();
         show_price_view(input_size,predict_house_price);
         input_real_price.setText("");
//         jf.repaint();
         drawpic(house_price,house_size);
         show_equation(slope,intercept);
      }
      else if("更正价格".equals(cmd))
      {
         double real_price=Double.parseDouble(input_real_price.getText());
         double input_size = get_house_size();
         System.out.println("房子面积："+input_size+"，  房子价格:"+real_price);
         renewData(input_size,real_price);
         show_equation(slope,intercept);
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

   public static void main(String[] args) throws IOException {
      LinearRegression linearRegression = new LinearRegression() ;
      linearRegression.initData();	//将基础数据准备好
      linearRegression.get_data();
      linearRegression.seek_data();
      linearRegression.seek_slope_intercept();

      linearRegression.initUI();
   }
}



