/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl;

import javax.swing.*;  
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.Cell;

class Methods{

	String sid[]= new String[30];
        String sname[]=new String[30];
        String sadd[]=new String[30];
        String ssal[]=new String[30];
	String sgender[]=new String[30];
	int i=0;
	Scanner sc=new Scanner(System.in);

         public void extractmysql() {
        try {
            //System.out.println("abc");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etl_sql","root","");
                System.out.println("Connecton established");
            Statement st = conn.createStatement();
            String query1 = "Select * from trial1";
            ResultSet rs = st.executeQuery(query1);
            System.out.println("query executed");
            while (rs.next()) 
            {
                    sid[i]=rs.getString("SID");
                    sname[i] = rs.getString("SNAME");
                    sadd[i]=rs.getString("SADD");
                    ssal[i]=rs.getString("SSAL");
                    sgender[i] = rs.getString("SGENDER");
                    //System.out.println(sname[i]+" "+sgender[i]);
                    i++; 
                
            }
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //i++;
        System.out.println("MySQL Extracted");
    }
        
        public void extractexcel(){
            try{
            File f=new File("C:\\Users\\Akshita\\Documents\\NetBeansProjects\\ETL\\Book1.xls");
            Workbook wb=Workbook.getWorkbook(f);
            String id;
            Sheet s=wb.getSheet(0);
            int row=s.getRows();
            int col=s.getColumns();
            for(int x=0;x<row;x++)
            {
                int w=0;
                    Cell c=s.getCell(w,x);
                    sid[i]=c.getContents();
                    w++;
                    c=s.getCell(w,x);
                    sname[i]=c.getContents();
                    w++;
                    c=s.getCell(w,x);
                    sadd[i]=c.getContents();
                    w++;
                    c=s.getCell(w,x);
                    ssal[i]=c.getContents();
                    w++;
                    c=s.getCell(w,x);
                    sgender[i]=c.getContents();
                    c=s.getCell(w,x); 
                //System.out.println(sname[i]+" "+sgender[i]);
                i++;
            }
        }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            //i++;
            System.out.println("Excel Extracted");
        }
        
       void extracttext() {
        try {
            String words[]=new String[5];
            FileInputStream fstream = new FileInputStream("C:\\Users\\Akshita\\Documents\\NetBeansProjects\\ETL\\data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            //String line;
            while(true)
            {
                String line=br.readLine();
                if(line==null)
                    break;
                words=(line.split(" "));
                int j=0;
                sid[i]=words[j];
                j++;
                sname[i]=words[j];
                j++;
                sadd[i]=words[j];
                j++;
                ssal[i]=words[j];
                j++;
                sgender[i]=words[j];
                //System.out.println(sname[i]+" "+sadd[i]+" "+sgender[i]);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         /*for(int k=0;k<sgender.length;k++)
           {
               System.out.println(sgender[k]);
           }*/
         System.out.println("Text file extracted");
    }
       void transform()
       {
           //Uniform Gender Format
           for(int k=0;k<sgender.length;k++)
           {
               //System.out.println(k+" "+sgender[k]);
               
               if(sgender[k].equals("MALE") || sgender[k].equals("m") || sgender[k].equals("male"))
               {
                   sgender[k]="MALE";
               }
                if(sgender[k].equals("FEMALE") || sgender[k].equals("f") || sgender[k].equals("female"))
               {
                   sgender[k]="FEMALE";
               }  
           }
           //Serial Number Correction
           int i=0;
           for(int k=1;k<=sid.length;k++)
           {
               sid[i]=Integer.toString(k);
               i++;
           }
           //Salary Grouping
           int j=0;
           for(int k=0;k<ssal.length;k++)
           {
              int sal=Integer.parseInt(ssal[k]);
               if(sal<=15000)
               {
                   ssal[k]="Class C";
               }
               else if(sal>15000 && sal<=30000)
               {
                   ssal[k]="Class B";
               }
               else
               {
                   ssal[k]="Class A";
               }
           }
           
           System.out.println("Transformation performed");
       }
       
       void load()
       {
           try {
            System.out.println("abc");
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etl_sql","root","");
                System.out.println("Connecton established");
            //Statement st = conn.createStatement();
            for(int i=0;i<30;i++)
            {
            String query="insert into etl_final(FID,FNAME,FADD,FSAL,FGENDER) values(?,?,?,?,?)";
            PreparedStatement stmt=conn.prepareStatement(query);
            stmt.setString(1,sid[i]);
            stmt.setString(2,sname[i]);
            stmt.setString(3,sadd[i]);
            stmt.setString(4,ssal[i]);
            stmt.setString(5,sgender[i]);
            stmt.executeUpdate();
           }
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }
           System.out.println("Records Inserted");
       }
}

public class ETL {
    static Methods m;
    public static void main(String[] args) {
        JFrame f=new JFrame();//creating instance of JFrame  
          
		JButton b1=new JButton("Extract");//creating instance of JButton  
		b1.setBounds(130,100,100, 50);//x axis, y axis, width, height  
		f.add(b1);//adding button in JFrame  

		JButton b2=new JButton("Transform");//creating instance of JButton  
		b2.setBounds(130,200,100, 50);//x axis, y axis, width, height  
		f.add(b2);

		JButton b3=new JButton("Load");//creating instance of JButton  
		b3.setBounds(130,300,100, 50);//x axis, y axis, width, height  
		f.add(b3);
		          
		f.setSize(400,500);//400 width and 500 height  
		f.setLayout(null);//using no layout managers  
		f.setVisible(true);//making the frame visible 

		b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               ETL.m=new Methods();
               m.extractmysql();
               m.extractexcel();
               m.extracttext();
            }
        });

        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
//               Methods m=new Methods();
               ETL.m.transform();
            }
        });

        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
              ETL.m.load();
            }
        });
    }
}
