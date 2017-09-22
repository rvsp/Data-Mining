package com;
import java.io.*;
import java.util.*;
import java.sql.*;
public class InformationGain {
		//CALCULATE ENTROPY FOR GIVEN POSITIVE AND NEGATIVE SAMPLES
		public double Entropy(double Sn,double Sp)
		{	
			if(Sn==Sp)
				return 1;
			else if(Sn==0 || Sp==0)
				return 0;
			else
			{
			double a=Sn/(Sn+Sp),b=Sp/(Sn+Sp);double entropy;	
			entropy=(((-a)*(Math.log(a)/Math.log(2)))+((-b)*(Math.log(b)/Math.log(2))));
		return entropy;
		}
		}
		
		//CALCULATE INFORMATION GAIN FOR EACH ATTRIBUTE PASSED
		public double gain(double E,String att,Statement st,String target) throws SQLException
		{
			int i=0;double sum=0;double S[]=null;
			double g=1;double tmp;
			
			
			String que="Select COUNT(DISTINCT "+att+" )from info_gain";
			ResultSet rs=st.executeQuery(que);
	        if(rs.next()){
	        	S=new double[rs.getInt(1)];
	     }
	         
	 que="Select "+att+" ,COUNT(*) from info_gain GROUP BY "+att;
	 rs=st.executeQuery(que);
	        while(rs.next())
	         {
	         	S[i]=rs.getDouble(2);
	          	i++;
	         }
	        
	 //CALCULATE ENTROPY FOR EACH VALUE OF THE ATTRIBUTE     
	 que="Select "+att+" ,COUNT(*) from info_gain where "+target+"='yes' GROUP BY "+att;
	 rs=st.executeQuery(que); i=0;
	        while(rs.next()){
	            	 tmp=rs.getDouble(2);
	            	 sum=sum+(((double)S[i]/14 )*Entropy(rs.getDouble(2),S[i]-tmp));
	                 i++;
	        }
	        
	        //INFO GAIN
	        g=E-sum;
		    return g;	
		}
		
		
		public static void main(String args[])
		{
			Scanner obj=new Scanner(System.in);int count=0,i=0,j=0;
			String target=new String();
			double E;String root=null;
			InformationGain in=new InformationGain();
			double tmp;String temp;
			double max=0;
			
			try
			{
				//Database Connectivity
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = null;
			    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/machine_learning?autoReconnect=true&useSSL=false","root","");
			    Statement st=conn.createStatement();
			
			//Display Attributes Names  
			System.out.println("The Attributes are:");
	        String que="SELECT column_name"
	        		+" FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'info_gain' and TABLE_SCHEMA='machine_learning' ";
	        ResultSet rs=st.executeQuery(que);
	        while(rs.next())
	        {
	        	System.out.print(rs.getString(1)+" ");
	        	count++;
	        }
	       System.out.print("\n");
	       rs.close();
	        
	        //Get Target Attribute
	        System.out.println("Enter the target attribute");
	        target=obj.nextLine();
	        
	        //Calculate Entropy for Target Attribute
	        que="SELECT "+target+" , COUNT(*) FROM info_gain GROUP BY "+target;
	        rs=st.executeQuery(que);
	        float S[]=new float[2];
	        while(rs.next())
	        {
	        	S[i]=Float.parseFloat(rs.getString(2));
	        i++;
	        }
	        rs.close();
	        E=in.Entropy(S[0],S[1]);
			System.out.println("Entropy :"+E);
			
			
			//Get Attribute names
			String col[]=new String[count-1];
		
			que="SELECT column_name FROM information_schema.columns WHERE table_name='info_gain'";
	        ResultSet rst=st.executeQuery(que);
	        i=0;boolean t;
	        while(i<count-1)
	        {
	        	rst.next();
	        	if(rst.getString(1)!=target)
	        	{
	        		col[i]=rst.getString(1);
	        	}
	        	else
	        		continue;
	        	
	        	i++;
	        }
	        rst.close();
	        
	        
	        //Calculate Information Gain
	        while(j<i)
	        {
				temp=col[j];
				System.out.println("Attribute :"+temp);
				tmp=in.gain(E,temp,st,target);
				System.out.println("Information Gain:"+tmp+"\n");
			
			//Compare Information gain of previous attribute	
		     if(max<tmp)
			 {
				 max=tmp;
				 root=temp;
			 }
			 j++;
			}
			
			//Display Root Node
			System.out.println("Root:"+root);
			}
			
			
		    catch(Exception e)
			{
			}
			
		}
}
