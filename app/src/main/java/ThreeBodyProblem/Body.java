package ThreeBodyProblem;


public class Body  {

    //G the gravitational constant
    public static final double G = 6.674 * Math.pow(10, -11);

    //Fields needed. Earth mass is the mass before being scaled by 10^14. To get in kilos, multiply by 10^14
    private double massInKilos;
    private double earthMass;
    private Coordinates initialPosition;
    private Coordinates currentPosition = new Coordinates();
    private Coordinates initialVelocity;
    private Coordinates currentVelocity = new Coordinates();
    private String name;

    //radius of each body
    private static final double RADIUS = 1;
    
    //boundaries of simulation    
    private static final double BOUNDS = 400;

    //constructor. sets the name
    public Body(String name) {

        this.initialPosition = new Coordinates();
        this.initialVelocity = new Coordinates();
        this.massInKilos = 0;
        this.name = name;
        
    }

    private double[] getForceByOtherBody(Body otherBody) throws CollisionException {
        
        //The total distance
        double distance = Math.sqrt(Math.pow(otherBody.getCurrentPosition().getX() - currentPosition.getX(), 2) + 
                Math.pow(otherBody.getCurrentPosition().getY() - currentPosition.getY(), 2) + 
                Math.pow(otherBody.getCurrentPosition().getZ() - currentPosition.getZ(), 2));

        //The directional components of the gravitational forces
        double forceX = ((G * massInKilos * otherBody.getMassInKilos()) * (otherBody.getCurrentPosition().getX() - currentPosition.getX())) / (Math.pow(distance, 3));
        
        double forceY = ((G * massInKilos * otherBody.getMassInKilos()) * (otherBody.getCurrentPosition().getY() - currentPosition.getY())) / (Math.pow(distance, 3));
        
        double forceZ = ((G * massInKilos * otherBody.getMassInKilos()) * (otherBody.getCurrentPosition().getZ() - currentPosition.getZ())) / (Math.pow(distance, 3));
        
        double[] forces = {forceX, forceY, forceZ};

        //Detecting a collision
        if (Math.abs(distance) < 2*RADIUS) {
            throw new CollisionException();
        }
        
        return forces;
    }

    //method to set the position of Body object based on force exerted by two other bodies

    public void setPositionFromGravity(Body bodyA, Body bodyB, double timeLapse) throws CollisionException, OutOfBoundsException {
            
            //two arrays of forces for other two bodies
            double[] forcesBodyA = getForceByOtherBody(bodyA);
            double[] forcesBodyB = getForceByOtherBody(bodyB);
            
            //temporary initial position
            double tempPosX = this.currentPosition.getX();
            double tempPosY = this.currentPosition.getY();
            double tempPosZ = this.currentPosition.getZ();
            
            //acceleration of body
            double accX = (forcesBodyA[0] + forcesBodyB[0]) / this.massInKilos;
            double accY = (forcesBodyA[1] + forcesBodyB[1]) / this.massInKilos;
            double accZ = (forcesBodyA[2] + forcesBodyB[2]) / this.massInKilos;

            //Updating the position
            double posX = currentPosition.getX() + (currentVelocity.getX() * timeLapse) + (0.5 * accX * Math.pow(timeLapse, 2));
            double posY = currentPosition.getY() + (currentVelocity.getY() * timeLapse) + (0.5 * accY * Math.pow(timeLapse, 2));
            double posZ = currentPosition.getZ() + (currentVelocity.getZ() * timeLapse) + (0.5 * accZ * Math.pow(timeLapse, 2));

            this.currentPosition.setCoordinates(posX, posY, posZ);

            //Updating the velocity
            this.currentVelocity.setCoordinates((this.currentPosition.getX()-tempPosX)/timeLapse,
                  (this.currentPosition.getY()-tempPosY)/timeLapse, (this.currentPosition.getZ()-tempPosZ)/timeLapse);


            //Detecting out of bounds
            if(Math.abs(this.currentPosition.getX()) > BOUNDS || Math.abs(this.currentPosition.getY()) > BOUNDS || Math.abs(this.currentPosition.getZ()) > BOUNDS){
                ThreeBodyProblemController.setPrompt(this.name + " HAS GONE OUT OF BOUNDS \n PLEASE PRESS 'R' TO RESET THE SIMULATION");
                throw new OutOfBoundsException();
            }
        
    }    
    
   
    //setters and getters
    
    public void setMass(double earthMass) {
        this.earthMass = earthMass;
        this.massInKilos = earthMass * Math.pow(10, 12);
    }
    
    public void setInitialPosition(double x, double y, double z) {
        this.initialPosition.setCoordinates(x, y, z);
        this.currentPosition.setCoordinates(this.initialPosition.getX(), this.initialPosition.getY(), this.initialPosition.getZ());
    }

    public void setInitialVelocity(double x, double y, double z) {
        this.initialVelocity.setCoordinates(x, y, z);
        this.currentVelocity.setCoordinates(this.initialVelocity.getX(), this.initialVelocity.getY(), this.initialVelocity.getZ());
    }

    public void setCurrentPosition(double x, double y, double z) {
        this.currentPosition.setCoordinates(x, y, z);
    }

    public void setCurrentVelocity(double x, double y, double z) {
        this.currentVelocity.setCoordinates(x, y, z);
    }

    public Coordinates getInitialPosition() {
        return this.initialPosition;
    }

    public Coordinates getInitialVelocity() {
        return this.initialVelocity;
    }

    public Coordinates getCurrentPosition() {
        return this.currentPosition;
    }

    public Coordinates getCurrentVelocity() {
        return this.currentVelocity;
    }

    public String getName() {
        return this.name;
    }

    public double getEarthMass() {
        return this.earthMass;
    }

    public double getMassInKilos() {
        return this.massInKilos;
    }
    
    public static double getBounds(){
        return BOUNDS;
    }

}
