import java.io.Serializable;

abstract class AssignmentObjects implements Serializable {
    protected String objecttype;
    public AssignmentObjects() {
        objecttype=null;
    }
    public AssignmentObjects(String type ) {
        this.objecttype=type;
    }
    public String getObjecttype() {
        return objecttype;
    }
    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }
    public String toString() {
        return ("Object Type:"+objecttype);
    }
}