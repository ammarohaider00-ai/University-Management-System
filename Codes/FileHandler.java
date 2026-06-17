import java.io.*;

class FileHandler<T> {
    public void writeToFile(T obj,String filename) {
        try {
            ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(filename));
            ous.writeObject(obj);
            ous.close();
          
        }
        catch(IOException e) {
            System.out.println("Error while saving data");
        }
    }
    public T readFromFile(String filename) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            T obj = (T) ois.readObject();
            ois.close();
            return obj;
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch(EOFException e) {
            System.out.println("File is empty");
        }
        catch(IOException e) {
            System.out.println("Error while reading file");
        }
        catch(ClassNotFoundException e) {
            System.out.println("Class not found");
        }
        return null;
    }
}