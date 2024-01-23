package ThreeBodyProblem;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;  
import javafx.stage.WindowEvent;

public class ThreeBodyProblem extends Application implements GLEventListener, KeyListener {

    //fields
    private static float rotateX;  
    private static float rotateY;
    private static float rotateZ;
    public static float translateZ = 5;
    private static double currentX=0;
    private static double currentY=0;
    private static double currentZ=1;
    public static double currentAngle=0;
    private static double currentAngleVertical=0;
    private static double time = 0.003;
    public static double fpsTime = 1;
    
    public static final double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();
    public static final double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
    public static Stage s1;
        
    public static Body body_1;    

    public static Body body_2;
    
    public static Body body_3;
    
    public static CircularQueue bodyQueue;
    
    public static Body focusBody;
    
    public static Body originBody;
    
    private GLU glu = new GLU();

    //start method    
    @Override
    public void start(Stage stage) throws java.io.IOException {
        s1=stage;
        Parent root = FXMLLoader.load(getClass().getResource("ThreeBodies.fxml"));
        Scene scene = new Scene(root);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Three-Body Problem Simulator");
        stage.show();
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    //main method
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    //calculate position of three spheres and display them
    @Override
    public void display(GLAutoDrawable draw) {
                
        if (focusBody==body_3){
            originBody.setCurrentPosition(originBody.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), originBody.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), originBody.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_1.setCurrentPosition(body_1.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_1.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_1.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_2.setCurrentPosition(body_2.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_2.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_2.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_3.setCurrentPosition(body_3.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_3.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_3.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
        }
        else if (focusBody==body_2){
            originBody.setCurrentPosition(originBody.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), originBody.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), originBody.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_1.setCurrentPosition(body_1.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_1.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_1.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_3.setCurrentPosition(body_3.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_3.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_3.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_2.setCurrentPosition(body_2.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_2.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_2.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            
        }
        else if (focusBody==body_1){
            originBody.setCurrentPosition(originBody.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), originBody.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), originBody.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_3.setCurrentPosition(body_3.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_3.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_3.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_2.setCurrentPosition(body_2.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_2.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_2.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_1.setCurrentPosition(body_1.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_1.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_1.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            
        }
        else if (focusBody==originBody){
            body_3.setCurrentPosition(body_3.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_3.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_3.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_2.setCurrentPosition(body_2.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_2.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_2.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            body_1.setCurrentPosition(body_1.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), body_1.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), body_1.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            originBody.setCurrentPosition(originBody.getCurrentPosition().getX()-focusBody.getCurrentPosition().getX(), originBody.getCurrentPosition().getY()-focusBody.getCurrentPosition().getY(), originBody.getCurrentPosition().getZ()-focusBody.getCurrentPosition().getZ());
            
        }
        
        try {
            if(!ThreeBodyProblemController.isPaused){
                for (int x = 0; x < fpsTime; x++) {
                    body_1.setPositionFromGravity(body_2, body_3, time);
                    body_2.setPositionFromGravity(body_1, body_3, time);
                    body_3.setPositionFromGravity(body_1, body_2, time);
                }
            }
        } catch (CollisionException e) {
            ThreeBodyProblemController.setPrompt("A COLLISION HAS BEEN DETECTED\nPLEASE PRESS 'R' TO RESET THE SIMULATION");
            return;
        }catch(OutOfBoundsException e){
            return;
        }
        
        GL2 gl = draw.getGL().getGL2();
      
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        //Body 1        
        double xb1 = body_1.getCurrentPosition().getX();
        
        double yb1 = body_1.getCurrentPosition().getY();
        
        double zb1 = body_1.getCurrentPosition().getZ();
 
        gl.glLoadIdentity();
        
        gl.glTranslated(xb1, yb1, zb1);
        
        gl.glColor3d(0d, 1d, 1d);

        this.displaySphere(gl);
        
        gl.glFlush();
        
        //body 2
        double xb2 = body_2.getCurrentPosition().getX();
        
        double yb2 = body_2.getCurrentPosition().getY();
        
        double zb2 = body_2.getCurrentPosition().getZ();

        gl.glLoadIdentity();
        
        gl.glTranslated(xb2, yb2, zb2);
        
        gl.glColor3d(1.0d, 0.0d, 0.0d);

        this.displaySphere(gl);
        
        gl.glFlush();
        
        //Body 3
        double xb3 = body_3.getCurrentPosition().getX();
        
        double yb3 = body_3.getCurrentPosition().getY();
        
        double zb3 = body_3.getCurrentPosition().getZ();
       
        gl.glLoadIdentity();
        
        gl.glTranslated(xb3, yb3, zb3);
        
        gl.glColor3d(0.0d, 1.0d, 0.0d);
        
        this.displaySphere(gl);
        
        gl.glFlush();
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        
        //zoom in and out
        if (translateZ!=0){
            double unitX = Math.sin(currentAngle*Math.PI/180.0);
            double unitZ = Math.cos(currentAngle*Math.PI/180.0);
            double unitY = Math.sin(currentAngleVertical*Math.PI/180);
            double magnitude=Math.sqrt(currentX*currentX+currentY*currentY+currentZ*currentZ);
            double zoomX=-translateZ*unitX;
            double zoomZ=translateZ*unitZ;
            double zoomY=translateZ*unitY;

            currentX=currentX+zoomX;
            currentY=currentY+zoomY;
            currentZ=currentZ+zoomZ;
            
            glu.gluLookAt(zoomX, 0, zoomZ, zoomX,0, zoomZ-1, 0, 1, 0);
            //glu.gluLookAt(zoomX, zoomY, zoomZ, zoomX, zoomY, zoomZ-1, 0, 1, 0);
        }

        if (rotateX!=0||rotateY!=0||rotateZ!=0){
            double angle=rotateY;
            currentAngle=(currentAngle+rotateY)%360.0;

            double verticalAngle=rotateX;
            currentAngleVertical=(currentAngleVertical+rotateY)%360.0;

            gl.glRotated(angle, 0, 1, 0);
            gl.glRotated(verticalAngle, 1, 0, 0);
        }

        translateZ=0;
        rotateY=0;
        rotateX=0;
        rotateZ=0;
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glTranslated(-body_1.getCurrentPosition().getX(), -body_1.getCurrentPosition().getY(), -body_1.getCurrentPosition().getZ());

        
    }

    //overriden methods
    
    @Override
    public void dispose(GLAutoDrawable draw) {
    }

    @Override
    public void init(GLAutoDrawable draw) {
        //The depth test
        GL2 gl = draw.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
    }

    @Override
    public void reshape(GLAutoDrawable draw, int x, int y, int width, int height) {

        
      final GL2 gl = draw.getGL().getGL2();
      
      //Avoid dividing by zero
      if( height <=0 ){
         height =1;
      }
      
      final float h = ( float ) width / ( float ) height;
      
      //Setting the boundaries of the Viewport
      gl.glViewport(0, 0, width, height);
      
      gl.glMatrixMode( GL2.GL_PROJECTION );
      gl.glLoadIdentity();
      
      
      //Setting the perspective proportional to the screen
      glu.gluPerspective( 45.0f, h, 0.1, 1000.0 );
      
      //Revert to the modelview
      gl.glMatrixMode( GL2.GL_MODELVIEW );
      gl.glLoadIdentity();
        
    }
    
    //method to display a sphere (used in display())
    public void displaySphere(GL2 gl){
        for(int i = 0; i <= 30; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / 30.0);
            double z0  = Math.sin(lat0);
            double zr0 =  Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) i / 30.0);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);
            
            gl.glBegin(gl.GL_QUAD_STRIP);
            
            for(int j = 0; j <= 30; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / 30.0;
                double x = Math.cos(lng);
                double y = Math.sin(lng);
                
                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(x * zr0, y * zr0, z0);
                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(x * zr1, y * zr1, z1);
            }

            gl.glEnd();
        }
    }
    
    //method to deal with keys pressed
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
                float rotationAngle=3f;
                float translationConstant=1;
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A)
                    rotateY = rotationAngle;
                else if (key == KeyEvent.VK_D)
                    rotateY = -rotationAngle;
                else if (key == KeyEvent.VK_PAGE_UP)
                    rotateZ += rotationAngle;
                else if (key == KeyEvent.VK_PAGE_DOWN)
                    rotateZ -= rotationAngle;
                else if (key == KeyEvent.VK_HOME)
                    rotateX = rotateY = rotateZ = 0;
                
                if (key==KeyEvent.VK_LEFT){
                    if (fpsTime > 1) {
                        fpsTime /= 2;
                    }
                                    
                    ThreeBodyProblemController.setPrompt(fpsTime + "X");
                }
                if (key==KeyEvent.VK_RIGHT){
                    if (fpsTime < 16) {
                        fpsTime *= 2;
                    }
                    
                    ThreeBodyProblemController.setPrompt(fpsTime + "X");
                }
                if (key==KeyEvent.VK_W){
                    if (currentX*currentX + currentZ*currentZ > 1) {
                        translateZ = -translationConstant;
                    }
                }
                if (key==KeyEvent.VK_S){
                    translateZ = translationConstant;
                }
                
                if (key==KeyEvent.VK_Z){
                    focusBody=bodyQueue.dequeue();
                }               
                

            }


    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub

            }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
            }

}

