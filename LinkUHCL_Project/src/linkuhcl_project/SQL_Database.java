/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author asini
 */
public class SQL_Database implements DataStorage{
     
        
        final String DATABASE_URL="jdbc:mysql://127.0.0.1:3306/annamalaishai70?useSSL=false";
        final String db_id="root";
        final String db_pwd="root123";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

    @Override
    public boolean signUp(String id, String password, String name, String type, String company) {
        try{
            connection=DriverManager.getConnection(DATABASE_URL,db_id,db_pwd);
            connection.setAutoCommit(false);
            statement=connection.createStatement();
             resultSet = statement.executeQuery("Select * from profile "
                    + "where id = '" + id + "'");
            
            if(resultSet.next()){
                System.out.println("A profile exists with this ID : "+id);
                return false;
            }
            else{
                int r=statement.executeUpdate("insert into profile values('"+ id+"','"+password+"','"+name+"','"+type+"','"+company+"')");
                System.out.println("Profile created successfully!!!");
                System.out.println("");
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        }
        catch(SQLException e){
            System.out.println("Something went wrong during profile creation");
            e.printStackTrace();
            return false;
        }
        finally{
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public String signIn(String id, String password) {
        try{
            connection=DriverManager.getConnection(DATABASE_URL,db_id,db_pwd);
            statement=connection.createStatement();
            resultSet=statement.executeQuery("select * from profile where id='"+id+"'");
            if(resultSet.next()){
                
                if(password.equals(resultSet.getString(2))){
                    return resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3)+","+resultSet.getString(4)+","+resultSet.getString(5);
                }
                else{
                    System.out.println("The password is not correct");
                    return "The password is not correct";
                }
            }
            else{
                System.out.println("The ID :"+id+" is not found");
                return("the ID :"+id+" is not found");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return "Internal Error";
        }
        finally{
            try{
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<Profile> getNewConnRecomWithinCompany(String senderId,String company) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //search the profiles within the company
            //select * from profile p where p.id<>'J101#' and p.company='UHCL' and p.id not in(select c.receiverId from connection c where c.senderId='J101#')
            resultSet=statement.executeQuery("select * from profile p where p.id<>'" + senderId + "' and p.company = '" + company + "' and p.id not in(select c.receiverId from connection c where c.senderId='" + senderId + "')");
            //PreparedStatement statement = connection.prepareStatement("Select * from profile p,connection c where p.id<>c.senderId and p.company = '" + company + "' and c.receiverId <>p.id");
            //resultSet=statement.executeQuery();
            
            ArrayList<Profile> aList = new ArrayList<Profile>();
            //Get the user profiles in an array list
            int count=0;
            while(resultSet.next() && count<3)
            {                                    
                if(!(resultSet.getString(1).equals(senderId))){
                    RegularProfile aProfile = new RegularProfile(resultSet.getString(1), 
                    resultSet.getString(3), 
                    resultSet.getString(4), resultSet.getString(5));
                    aList.add(aProfile);count++;
                }
            }
            return aList;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
    @Override
    public ArrayList<Profile> getNewConnRecomOutsideCompany(String senderId,String company) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //search the profiles Outside the company
            //select * from profile p where p.id<>'J101#' and p.company='UHCL' and p.id not in(select c.receiverId from connection c where c.senderId='J101#')
            resultSet=statement.executeQuery("select * from profile p where p.id<>'" + senderId + "' and p.company <> '" + company + "' and p.id not in(select c.receiverId from connection c where c.senderId='" + senderId + "')");    
            ArrayList<Profile> aList = new ArrayList<Profile>();
            //Get the profiles in an arrayList
            int count=0;
            while(resultSet.next() && count<3)
            {                                    
                if(!(resultSet.getString(1).equals(senderId))){
                    RegularProfile aProfile = new RegularProfile(resultSet.getString(1), 
                    resultSet.getString(3), 
                    resultSet.getString(4), resultSet.getString(5));
                    aList.add(aProfile);
                    count++;
                }
            }
            return aList;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public ArrayList<String> findConnections(String id) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //find the connections for a user
            //select * from connection where (senderId='J101#' or receiverId='J102#') and status='Approve'
            resultSet=statement.executeQuery("select * from connection where (senderId='" + id + "' or receiverId='" + id + "') and status='Approve' ");    
            ArrayList<String> aList = new ArrayList<>();
            //Fetch the connections in ArrayList
            int count=0;
            while(resultSet.next())
            {                                    
                if(resultSet.getString(1).equals(id)){
                    aList.add(resultSet.getString("receiverId"));
                }
                if(resultSet.getString(2).equals(id)){
                    aList.add(resultSet.getString("senderId"));
                }
                
            }
            return aList;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public ArrayList<JobAd> findJobShares(String id,ArrayList<String> connectedPeople) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            ArrayList<JobAd> aList = new ArrayList<>();
            //search for the jobs shared by connected people
            String getConnections="";
             String getConn="";
             int count=0;
            if(connectedPeople.size()>0){
                for(int i=0;i<connectedPeople.size() && i<5;i++){
                    //Get the connected people in a string to pass in query
                    getConnections+="'"+connectedPeople.get(i)+"',";
                    count++;
                }   
                getConn=getConnections.substring(0,getConnections.length()-1);
                getConn="("+getConn.trim()+")";
            }
            else{
                return aList;
            }
            count=0;
            //select j.job_id,j.company,j.description,j.creator_id,j.date from jobad j,jobshare js where j.job_id=js.job_id and js.profile_id in('J102#','J103#','J104#') and js.job_id not in(select job_id from jobshare where profile_id='J101#')
            resultSet=statement.executeQuery("select distinct j.job_id,j.company,j.description,j.creator_id,j.date from jobad j,jobshare js where j.job_id=js.job_id and js.profile_id in" + getConn + "and js.job_id not in(select job_id from jobshare where profile_id='" + id + "')order by j.date desc ");    
            while(resultSet.next() && count<3)
            {                                    
                JobAd ajobAd = new JobAd(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getString(3),resultSet.getString(4), resultSet.getString(5));
                aList.add(ajobAd);
                count++;
            }
            return aList;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public ArrayList<Profile> getProfile(String id) {
         try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //Get the complete profile based on id
            //select id,name,type,company from profile where id='J101#' 
            resultSet=statement.executeQuery("select id,name,type,company from profile where id='" + id + "' ");    
            ArrayList<Profile> aList = new ArrayList<>();
            //Store the profile data in arrayList
            while(resultSet.next())
            {                                    
                RegularProfile aProfile = new RegularProfile(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getString(3), resultSet.getString(4));
                aList.add(aProfile);
            }
            return aList;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public boolean isConnected(String senderId,String receiverId) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //Check if the users are connected
            //select * from connection where (senderId='J102#' and receiverId='J101#') or (senderId='J101#' or receiverId='J102#') 
            resultSet=statement.executeQuery("select * from connection where ((senderId='" + senderId + "' and receiverId='" + receiverId + "')or (senderId='" + receiverId + "' and receiverId='" + senderId + "')) and status='Approve'");    
            while(resultSet.next())
            {                                    
               return true;
            }
            return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public void sendRequest(String senderId, String receiverId, String message, String status) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            //insert a record in connections table
            int r=statement.executeUpdate("insert into connection values"
                        + "('" + senderId + "', '" + receiverId + "', '" + message + "', '"
                        + status+ "',now())");
            System.out.println("Connection request sent successfully!!!");
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong while sending the connection request");
            e.printStackTrace();
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public void approveDenyOrIgnoreRequest(String senderId, String receiverId, String response) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            //Update the status of the connection Request
            int r=statement.executeUpdate("Update connection set status ='" + response + "'where senderId='" + receiverId + "' and receiverId='" + senderId + "'");
            if(response.equals("Approve")){
                System.out.println("Connection request from "+receiverId+" approved");
            }
            else if(response.equals("Deny")){
                System.out.println("Connection request from "+receiverId+" denied");
            }
            else if(response.equals("Ignore")){
                System.out.println("Connection request from "+receiverId+" ignored");
            }
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong while Approve/Deny/Ignore requests");
            e.printStackTrace();
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
      @Override
    public ArrayList<Connections> isReqExist(String senderId,String receiverId) {
         try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //search if there is a connection request already exist
            //select * from connection where senderId='J102#' or  receiverId='J102#'
            resultSet=statement.executeQuery("select * from connection where((senderId='" + senderId + "' and receiverId='" + receiverId + "')or (senderId='" + receiverId + "' and receiverId='" + senderId + "')) and status='New' ");    
             ArrayList<Connections> aList = new ArrayList<>();
            while(resultSet.next())
            {                                    
                Connections aConn = new Connections(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
                aList.add(aConn);
            }
            return aList;
            
            
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong while fethcing request");
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public ArrayList<Connections> getPendingRequest(String senderId) {
         try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //Get the connection requests with status New 
            //select * from connection where senderId='J102#' or  receiverId='J102# and status='New' '
            resultSet=statement.executeQuery("select * from connection where receiverId='" + senderId + "' and status='New' ");    
             ArrayList<Connections> aList = new ArrayList<>();
            while(resultSet.next())
            {                                    
                Connections aConn = new Connections(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
                aList.add(aConn);
            }
            return aList;
            
            
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong while fethcing the pending request");
            e.printStackTrace();
            return null;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public ArrayList<JobAd> findJobAds(String senderId) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //Get the jobAds which the user has not shared before
            //select * from jobAd j where j.job_id not in(select job_id from jobshare where profile_id='J101#')
            resultSet=statement.executeQuery("select * from jobAd j where j.job_id not in(select job_id from jobshare where profile_id='" + senderId + "') order by j.date desc");    
            ArrayList<JobAd> aList = new ArrayList<JobAd>();
            //check if the users are connected
            int count=0;
            while(resultSet.next() && count<5)
            {                                    
                JobAd aJob = new JobAd(resultSet.getString(1), 
                resultSet.getString(2), 
                resultSet.getString(3), resultSet.getString(4),resultSet.getString(5));
                aList.add(aJob);
                count++;

            }
            return aList;
        }
        catch (SQLException e)
       {
           System.out.println("Something went wrong while finding job Ads");
           e.printStackTrace();
           return null;
       }
       finally
       {
           //close the database
           try
           {
               connection.close();
               statement.close();
               resultSet.close();
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }

        }
    }

    @Override
    public void shareJobAd(String id, String jobId) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            //insert a record in jobShare table
            int r=statement.executeUpdate("insert into jobshare values('" + id + "','" + jobId + "')");
            System.out.println("Job "+jobId+" shared successfully with your connections");
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong during jobAd sharing");
            e.printStackTrace();
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                //resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public void createJobAd(String jobId, String company, String description, String id) {
          try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            //insert a record in jobAd table to create a new job
            int r=statement.executeUpdate("insert into jobad values('" + jobId + "','" + company + "','" + description + "','" + id + "',now())");
            if(r>0){
                
            }
            System.out.println(" A Job with Id: "+jobId+" created successfully");
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException e)
        {
            System.out.println("Something went wrong during Job creation");
            e.printStackTrace();
        }
        finally
        {
            //close the database
                try
            {
                connection.close();
                statement.close();
               // resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isProfileExists(String receiverId) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //search the profiles within the company
            //select * from profile where  id='J101#'
            resultSet=statement.executeQuery("select * from profile where  id='" + receiverId + "'");    
            while(resultSet.next())
            {                                    
               return true;
            }
            return false;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }

    @Override
    public boolean isJobIdExists(String jobId) {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_pwd);
            statement = connection.createStatement();
            
            //search if the JobId exists in Jobad table
            //select * from profile where  id='J101#'
            resultSet=statement.executeQuery("select * from jobad where  job_id='" + jobId + "'");    
            while(resultSet.next())
            {                                    
               return true;
            }
            return false;
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
            
}
