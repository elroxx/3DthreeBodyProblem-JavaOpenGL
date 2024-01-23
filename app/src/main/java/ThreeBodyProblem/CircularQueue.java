package ThreeBodyProblem;

public class CircularQueue {
    private Body[] elements;
    private int size;
    
    public CircularQueue() {
        elements = new Body[8];
    }
    
    public CircularQueue(Body body1, Body body2, Body body3, Body originBody){
        elements = new Body[8];
        this.enqueue(body1);
        this.enqueue(body2);
        this.enqueue(body3);
        this.enqueue(originBody);

    }
    
    public void enqueue (Body value){
        if (size >= elements.length){
            Body[]temp = new Body[elements.length*2];
            System.arraycopy(elements, 0, temp, 0, elements.length);
            elements = temp;
        }
        
        elements[size++] = value;
    }
    
    public Body dequeue()
    {
       Body value = elements[0];
       for(int i = 1; i<size; i++)
       {
       elements[i-1] = elements[i]; 
       }
        size--;
        this.enqueue(value);
        return value;
    }
    
    public boolean empty(){
        return size == 0;
    }
    
    public int getSize(){
        return size;
    }
    
}