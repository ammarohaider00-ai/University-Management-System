import java.io.Serializable;
abstract public class User implements Serializable {
    protected String name;
    protected String userType;
    public User() {
        this.name=null;
        this.userType=null;
    }
    public User(String name,String userType) {
        this.name=name;
        this.userType=userType;
    }
    public void setName(String name) {
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setUserType(String userType) {
        this.userType=userType;
    }
    public String getUserType() {
            return userType;
    }
    public String toString() {
        return ("Name:"+name +"\n"+"UserType:"+userType +"\n");
    }

}
