import java.io.Serializable;
abstract class CampusEntity implements Serializable {
    protected String entityID;
    protected String name;
    protected String location;
    public CampusEntity() {
        entityID=null;
        name=null;
        location=null;
    }
    public CampusEntity(String entityID, String name, String location) {
        this.entityID=entityID;
        this.name=name;
        this.location=location;
    }
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
    public String getEntityID() {
        return entityID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }
    public String toString() {
        return String.format("Entity ID: %s%nEntity Name:%s%nLocation:%s%n",entityID,name,location);
    }
public abstract double calculateOperationalCost();
}

