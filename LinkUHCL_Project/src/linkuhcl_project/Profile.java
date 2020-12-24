/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asini
 */
public abstract class Profile {

    private String id;
    private String name;
    private String type;
    private String company;
    
    DataStorage data=new SQL_Database();
    private ArrayList<Profile> inCompanyProfile
            = new ArrayList<Profile>();
    private ArrayList<Profile> outCompanyProfile
            = new ArrayList<Profile>();
    private ArrayList<JobAd> jobAds
            = new ArrayList<JobAd>();
    
    public Profile(String id,String name, String type, String company){
        this.id=id;
        this.name=name;
        this.type=type;
        this.company=company;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    
    
    public void displayProfilePage(){
        //Notification for new requests
        displayNotification();
        //Display new connection recommendations within the company
        System.out.println("New Connection Recommendations within your company : "+getCompany());
        displayNewRecommWithinCompany();
        //Display new connection recommendations outside the user's company
        System.out.println("New Connection Recommendations Outside your company : ");
        displayNewRecommOutsideCompany();
        //Display the JobAds shared by the user's connections.
        //get the user's connection
        System.out.println("Please find the below job Ads");
        displaySharedJobAds();
        //Display the menu:
        displayMenu();
    }
    
    
    public void displayNewRecommWithinCompany(){
        //Get the new connection Recommendations within user's company
        inCompanyProfile=data.getNewConnRecomWithinCompany(id, company);
        if(inCompanyProfile.size()==0){
            System.out.println("No new Recommendations within your company!!");
        }
        for(int i=0;i<inCompanyProfile.size();i++){
            System.out.println((i+1)+": "+inCompanyProfile.get(i).getId()+"\t\t"+inCompanyProfile.get(i).getName()+"\t\t"+inCompanyProfile.get(i).getType());              
        }
        System.out.println();
    }
    public void displayNewRecommOutsideCompany(){
        //Get the new connection Recommendations Outside user's company
        outCompanyProfile=data.getNewConnRecomOutsideCompany(id, company);
        if(outCompanyProfile.size()==0){
            System.out.println("No new Recommendations within your company!!");
        }
        for(int i=0;i<outCompanyProfile.size();i++){
            System.out.println((i+1)+": "+outCompanyProfile.get(i).getId()+"\t\t"+outCompanyProfile.get(i).getName()+"\t\t"+outCompanyProfile.get(i).getType()+"\t\t"+outCompanyProfile.get(i).getCompany());              
        }
        System.out.println();
    }
    public void displaySharedJobAds(){
        //Find the connections of the user to display the jobAds shared by user's connection 
        ArrayList<String> userConnections=data.findConnections(id);
        //Get the JobAds shared by the user's connections
        jobAds=data.findJobShares(id, userConnections);
        if(jobAds.size()<=0 ){
            System.out.println("No Job Ads!!");
        }
        for(int i=0;i<jobAds.size();i++){
            System.out.println((i+1)+": "+jobAds.get(i).getJobId()+"\t\t"+jobAds.get(i).getCompany()+"\t\t\t"+jobAds.get(i).getDescription()+"\t\t\t"+jobAds.get(i).getDate());              
        }
        System.out.println();
    }
    public void displayMenu(){
        
        String userInput="";
        Scanner input=new Scanner(System.in);          
        while(!userInput.equals("x"))
        {
            System.out.println("Please make your selection from the below Menu");
            System.out.println("1: See My Connections");
            System.out.println("2: Send Connection Request");
            System.out.println("3: Approve or Deny Request");
            System.out.println("4: Share a Job");
            if(getType().equals("Recruiter")){
                System.out.println("5: Create a Job"); 
            }
            System.out.println("x : Exit");
            System.out.println("");
            // get  the input from the user
            userInput=input.nextLine();
            if(userInput.equals("1")){
                seeMyConnections();
            }
            else if(userInput.equals("2")){
                sendConnectionRequest();
            }
            else if(userInput.equals("3")){
                approveDenyOrIgnoreRequest();
            }
            else if(userInput.equals("4")){
                shareJobAd();
            }
            else if(userInput.equals("5")){
                new RecruiterProfile(getId(), getName(), getType(), getCompany()).createJobAd(getId());
            }
        }     
    }
    public void seeMyConnections(){
        //Get the user's Connection
        ArrayList<String> userConnections=data.findConnections(id);
        String userInput="";
        if(userConnections.size()==0){
            System.out.println("You have no connections");
            return ;
        }
        ArrayList<Profile> profile=new ArrayList<>();
        Scanner input=new Scanner(System.in);  
        System.out.println("Below are your connections");
        for(int i=0;i<userConnections.size();i++){
           System.out.println("*\t"+userConnections.get(i));
        }
        System.out.println();
        System.out.println("Do you want to view your connection's profile (Yes/No)):");
        userInput=input.nextLine();
        //Get the user confirmation, to display the profile of user's connection
        if(userInput.toLowerCase().equals("yes")){
            System.out.println("Enter a person ID to view their profile");
            userInput=input.nextLine();
            for(int i=0;i<userConnections.size();i++){
               if((userConnections.get(i)).trim().contains(userInput)){
                   profile=data.getProfile(userInput);
                   break;
               } 
               if(i==userConnections.size()-1 && !(userConnections.get(i)).trim().contains(userInput)){
                    System.out.println("Invalid Input. Please verify your input");
                    System.out.println();
                    return;
               }
            }
            System.out.println("*\t"+profile.get(0).getId()+"\t\t"+profile.get(0).getName()+"\t\t"+profile.get(0).getCompany()+"\t\t"+profile.get(0).getType());        
            System.out.println();
             //Display the connections of the searched user 
            ArrayList<String> searchedUserConn=data.findConnections(userInput);
            System.out.println("Below are the people "+userInput+" connected with  :");
            for(int i=0;i<searchedUserConn.size();i++){
               System.out.println("*  "+searchedUserConn.get(i));
            }
            System.out.println();   
        }
        
    }
    public void sendConnectionRequest(){
        System.out.println("Enter the id to send connection Request ");
        Scanner input=new Scanner(System.in);   
        String senderId=getId();
        String receiverId=input.nextLine();
        //check if the user inputted id exist
        if(data.isProfileExists(receiverId)){
            //check if they are already connected
            if(data.isConnected(senderId, receiverId)){
                System.out.println("You already connected with "+receiverId);
            }
            else{
                System.out.println("Enter your Message ");
                String message=input.nextLine();
                //check if there is already a request from the receipient
                ArrayList<Connections> conn=data.isReqExist(senderId,receiverId);
                if(conn.size()>0){
                    System.out.println("A connection request already exists");
                }
                else{
                //date column is added in insert command in sendRequest method in SQL _Database
                    data.sendRequest(senderId, receiverId,message,"New");
                }
            }
        }
        else{
            System.out.println("User Profile does not exist\n");
        }
    }
    public void approveDenyOrIgnoreRequest(){
         //Display the pending connection requests
        String senderId=getId(); 
        System.out.println("Please find below the pending requests"); 
        ArrayList<Connections> pendingReq=data.getPendingRequest(senderId);
        if(pendingReq.size()==0){
            System.out.println("You have no pending requests");
            return;
        }
        for(int i=0;i<pendingReq.size();i++){
           System.out.println("*  "+pendingReq.get(i).getSenderId());
        }
        System.out.println(); 
        System.out.println("Enter the id to approve the request");
        Scanner input=new Scanner(System.in);
       
        String receiverId=input.nextLine();
        //Check if the connection request has been approved already
        if(data.isConnected(senderId, receiverId)){
            System.out.println("You already approved the connection request from "+receiverId);
        }
        else{
            //Get  the user response for the request
            String userInput="";
            System.out.println("Please make a selection of your response to this connection request");
            System.out.println("1 : Approve");
            System.out.println("2 : Deny");
            System.out.println("3 : Ignore");
            System.out.println("x : Exit");
            // get  the input from the user
            userInput=input.nextLine();
            String response="";
            if(userInput.equals("1")){
                response="Approve";
            }
            else if(userInput.equals("2")){
                response="Deny";
            }
            else if(userInput.equals("3")){
                response="Ignore";
            }
           
            //date column is added in insert command in sendRequest method in SQL _Database
            data.approveDenyOrIgnoreRequest(senderId, receiverId,response);
        }
    }
    public void shareJobAd(){
        //Get the jobAds from Jobad table
        ArrayList<JobAd> jobs=data.findJobAds(getId());
        System.out.println("Please find below latest JobAds");
        for(int i=0;i<jobs.size();i++){
            System.out.println("*\t\t"+jobs.get(i).getJobId()+"\t\t"+jobs.get(i).getCompany()+"\t\t"+jobs.get(i).getDescription()+"\t\t"+jobs.get(i).getCreatorId()+"\t\t"+jobs.get(i).getDate());
        }
        System.out.println();
        System.out.println("Enter the JobId to share it with your connections");
        Scanner input=new Scanner(System.in);
        String userInput="";
        userInput=input.nextLine();
        //Check if the user entered job Id exists
        if(data.isJobIdExists(userInput)){
            data.shareJobAd(getId(),userInput);        
        }
        else{
            System.out.println("Invalid Job Id. Please check your input\n");
        }
        
    }
    //To display notification of pending requests.
    public void displayNotification(){
        ArrayList<Connections> jobs=data.getPendingRequest(getId());
        int count=jobs.size();
        if(count>0){
            System.out.println("You have ("+count+") new connection requests");
        }
        else{
            System.out.println("You have no new connection requests");
        }
        
    }
    
    
}

