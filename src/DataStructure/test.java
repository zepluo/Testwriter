/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructure;

import java.util.ArrayList;

/**
 *
 * @author zepingluo
 */
public class test {
    
    private ArrayList<Questions> testQuestionList;
    private String testName;
    private String testWriter;
    private int time;
    
    public test()
    {
        this.testQuestionList=new ArrayList<Questions>();
    }
    public test(String testName,String testWriter)
    {
        this.testName=testName;
        this.testWriter=testWriter;
        this.testQuestionList=new ArrayList<Questions>();
    }
    
    public int getTime()
    {
        return time;
    }
    public void setTime(int hour)
    {
        time=hour;
    }
   
    public ArrayList<Questions> getTestQuestionList()
    {
        return testQuestionList;
    }       
    
    public void setTestInfo(String tn, String tw)
    {
        this.testName=tn;
        this.testWriter=tw;
    }        
    public String getTestName()
    {
        return testName;
    }
    public String getTestWriter()
    {
        return testWriter;
    }
     public void setList(ArrayList<Questions> list)
    {
        testQuestionList=list;
    }     
    
    
    
    
}
