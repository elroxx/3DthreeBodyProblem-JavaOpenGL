package ThreeBodyProblem;

public class Coordinates {
    //private fields
    private double x, y, z;

    //Constructors
    public Coordinates(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinates(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    //Set methods
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setZ(double z){
        this.z = z;
    }

    public void setCoordinates(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Get methods
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getZ(){
        return this.z;
    }

}