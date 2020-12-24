/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkuhcl_project;

import java.util.ArrayList;

/**
 *
 * @author asini
 */
public interface DataStorage {
    boolean signUp(String id, String password,String name,String type, String company);
    String signIn(String id, String password);
    ArrayList<Profile> getNewConnRecomWithinCompany(String id,String company);
    ArrayList<Profile> getNewConnRecomOutsideCompany(String id,String company);
    ArrayList<String> findConnections(String id);
    ArrayList<JobAd> findJobShares(String id,ArrayList<String>connectedPeople);
    ArrayList<Profile> getProfile(String id);
    boolean isConnected(String senderId,String receiverId);
    boolean isProfileExists(String receiverId);
    void sendRequest(String senderId,String receiverId, String message, String status);
    void approveDenyOrIgnoreRequest(String senderId, String receiverId,String response);
    ArrayList<Connections> isReqExist(String senderId,String receiverId);
    ArrayList<Connections> getPendingRequest(String senderId);
    ArrayList<JobAd> findJobAds(String senderId);
    boolean isJobIdExists(String jobId);
    void shareJobAd(String id,String jobId);
    void createJobAd(String jobId,String company,String description,String id);
}
