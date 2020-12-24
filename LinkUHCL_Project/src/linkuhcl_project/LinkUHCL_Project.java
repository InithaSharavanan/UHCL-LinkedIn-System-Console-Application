/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

import java.util.Scanner;

/**
 *
 * @author asini
 */
public class LinkUHCL_Project {

    /**
     * @param args the command line arguments
     */
    DataStorage data = new SQL_Database();
    String id, password, name, type, company;
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input=new Scanner(System.in);
        String userInput="";
        
               
        while(!userInput.equals("x"))
        {
            System.out.println("Please select one of the following options:");
            System.out.println("1 : Sign Up");
            System.out.println("2 : Sign In");
            System.out.println("x : Exit");
            // get  the input from the user
            userInput=input.nextLine();
            if(userInput.equals("1")){
                new LinkUHCL_Project().signUp();
            }
            else if(userInput.equals("2")){
                new LinkUHCL_Project().signIn();
            }
        }
    }
    public  void signUp(){
        Scanner input=new Scanner(System.in);
        //prompt for user input
        System.out.println("Please enter the user id");
        id=input.nextLine();
          //Verify user id 
        if(!(new LinkUHCL_Project().validUserId(id))){
            System.out.println("Invalid User ID. Please follow the below standards to create the Login ID: ");
            System.out.println("Please make sure your ID has atleast one letter, one digit and one special character from {#,?,!,*} ");                
        }
        else{
            System.out.println("Please enter the user password");
            password=input.nextLine();
            if(password.equals(id)){
                System.out.println("Password cannot be same as Login ID");
            }
            else{
                System.out.println("Please enter the user name");
                name=input.nextLine();
                System.out.println("Please enter the profile type");
                type=input.nextLine();
                System.out.println("Please enter the company name");
                company=input.nextLine();
               if(data.signUp(id, password, name, type, company)){
                    if(type.equalsIgnoreCase("regular")){
                        new RegularProfile(id,name,type,company).displayProfilePage();        
                    }else{
                        new RecruiterProfile(id,name,type,company).displayProfilePage();        
                    }   
               }
                
            }
            
        }
        
    }
    public void signIn(){
        String id="";
        String password="";
        Scanner input=new Scanner(System.in);
        System.out.println("Please enter your ID");
        id = input.next();
        System.out.println("Please enter your password");
        password = input.next();
        
        String res = data.signIn(id, password);
        String[] resultdata=res.split(",");
        if(resultdata[3].equals("Regular"))
        {
            new RegularProfile(resultdata[0],resultdata[2],resultdata[3],resultdata[4]).displayProfilePage();
        }
        else{
            new RecruiterProfile(resultdata[0],resultdata[2],resultdata[3],resultdata[4]).displayProfilePage();
        }
        
    }
    public boolean validUserId(String id){
        String alphabets=".*[a-zA-Z].*";
        String digits=".*[0-9].*";
        if(id.length()>=3 && id.length()<=10 && id.matches(alphabets) && id.matches(digits) && (id.contains("#")|| id.contains("?") || id.contains("!")|| id.contains("*"))){
            return true;
        }
        return false;
    }
    
}
