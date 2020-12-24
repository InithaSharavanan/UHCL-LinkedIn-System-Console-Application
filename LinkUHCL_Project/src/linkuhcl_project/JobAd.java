/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

/**
 *
 * @author asini
 */
public class JobAd {
    private String jobId;
    private String company;
    private String description;
    private String creatorId;
    private String date;

    public JobAd(String jobId, String company, String description, String creatorId, String date) {
        this.jobId = jobId;
        this.company = company;
        this.description = description;
        this.creatorId = creatorId;
        this.date = date;
    }

    
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
