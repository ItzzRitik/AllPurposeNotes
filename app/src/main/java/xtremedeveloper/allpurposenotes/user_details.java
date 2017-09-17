package xtremedeveloper.allpurposenotes;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class user_details {

    public String fname,lname,gender,dob;
    public user_details() {}
    public user_details(String fname, String lname,String gender,String dob)
    {
        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.dob = dob;
    }
    public String getfname() {return this.fname;}
    public String getlname() {return this.lname;}
    public String getgender() {return this.gender;}
    public String getdob() {return this.dob;}
}