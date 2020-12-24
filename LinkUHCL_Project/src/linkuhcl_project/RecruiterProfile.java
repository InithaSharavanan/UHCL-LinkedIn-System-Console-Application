/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author asini
 */
public class RecruiterProfile  extends Profile{

    public RecruiterProfile(String id, String name, String type, String company) {
        super(id,name, type, company);
    }

    public void createJobAd(String id) {
        Scanner input=new Scanner(System.in);
        System.out.println("Enter the JobId");
        String jobid=input.nextLine();
        System.out.println("Enter the job description");
        String description=input.nextLine();
        //creating a new job based on the user input and update the Jobad table.
        data.createJobAd(jobid,getCompany(),description,getId());
        //Share the created job with the conencted people.
        data.shareJobAd(id, jobid);
    }

    
}
