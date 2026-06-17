import java.io.Serializable;
import java.util.ArrayList;

class CampusRepository <T> implements Serializable { 
    private ArrayList<T> campusRepository;
    public CampusRepository() {
        campusRepository = new ArrayList<>();
    }
    public CampusRepository(ArrayList<T> campusRepository) {
        this.campusRepository = campusRepository;
    }
    public ArrayList<T> getCampusRepo() {
        return campusRepository;
    }
    public void setCampusRepo(ArrayList<T> campusRepo) {
        this.campusRepository = campusRepo;
    }
    public void AddItem(T item){
        if(!campusRepository.contains(item)) {
            campusRepository.add(item);
            System.out.printf("%s Added to Campus Repository\n", item.getClass());
        }
        else {
            System.out.printf("%s already exists in Campus Repository\n", item.getClass());

        }
    }
    public void RemoveItem(T item){
        if(campusRepository.contains(item)) {
            campusRepository.remove(item);
            System.out.printf("%s Removed from Campus Repository\n", item.getClass());
        }
        else {
            System.out.printf("%s does not exist in Campus Repository\n", item.getClass());
        }
    }
    public double calculateOperationalCost() {
        if(campusRepository.isEmpty()) {
            System.out.printf("Campus Repository is empty");
            return 0;
        }
        if (!(campusRepository.get(0) instanceof CampusEntity)) {
            System.out.println("calculateOperationalCost() is not supported for this repository type.");
                return 0;
            }
            double total = 0;
            for (T item : campusRepository) {
                total += ((CampusEntity) item).calculateOperationalCost();
            }
            return total;
        }
    public void DisplayRepository() {
        if(campusRepository.isEmpty()) {
            System.out.printf("No campus repository has been found%n");
            return;
        }
        for(T item:campusRepository) {
            System.out.println(item.toString());
            System.out.println();

        }
    }
    }
