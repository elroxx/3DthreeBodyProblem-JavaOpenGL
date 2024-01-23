package ThreeBodyProblem;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ThreeBodyProblemController {

    //fields
    private static Text prompt;
    private final static FPSAnimator FPSANIM = new FPSAnimator(300, true);
    private static Body[] bodies = {new Body("First Body (Blue)"), new Body("Second Body (Red)"), new Body("Third Body (Green)")};
    private Body selectedBody;
    
    //method to get the three bodies in an array
    public static Body[] getBodies() {
        return bodies;
    }

    //Fields (from FXML file)
    private @FXML TextField tfMass;
    private @FXML TextField tfPosX;
    private @FXML TextField tfPosY;
    private @FXML TextField tfPosZ;
    private @FXML TextField tfVelX;
    private @FXML TextField tfVelY;
    private @FXML TextField tfVelZ;
    private @FXML Text tfWarningMass;
    private @FXML Text tfWarningStart;
    private @FXML Text tfWarningPosition;
    private @FXML Text tfWarningVelocity;
    private @FXML Button btnSaveValues;
    private @FXML Button btnStartSimulation;
    private @FXML ComboBox<String> comboBoxBodies;
    private @FXML Text tfInfoMass;
    private @FXML StackPane helpPane;
    private @FXML Button btnHelp;
    private @FXML Button btnClose;

    private int clickCount;
    private double minMass = Math.pow(10, 6);
    private double maxMass = Math.pow(10, 16);
    private double posBounds = 400;
    private double maxSpeed = 15000;
    public static boolean isPaused = false;


    //when the ComboBox is clicked:
    public void handleOnClickCombo() {

        if (clickCount == 0) {
            String[] bodiesNames = new String[bodies.length];
            for (int x = 0; x < bodies.length; x++) {
                bodiesNames[x] = bodies[x].getName();
            }
            comboBoxBodies.getItems().addAll(bodiesNames);
        }

        clickCount++;

    }

    //when an item of the ComboBox is selected:
    public void handleOnActionCombo() {
        
        if (comboBoxBodies.getValue() != null) {

            for (int x = 0; x < bodies.length; x++) {
                if (comboBoxBodies.getValue() == bodies[x].getName()) {
                    selectedBody = bodies[x];
                    break;
                }
            }

        }
        tfMass.setText(String.valueOf(selectedBody.getEarthMass()));
        tfPosX.setText(String.valueOf(selectedBody.getInitialPosition().getX()));
        tfPosY.setText(String.valueOf(selectedBody.getInitialPosition().getY()));
        tfPosZ.setText(String.valueOf(selectedBody.getInitialPosition().getZ()));
        tfVelX.setText(String.valueOf(selectedBody.getInitialVelocity().getX()));
        tfVelY.setText(String.valueOf(selectedBody.getInitialVelocity().getY()));
        tfVelZ.setText(String.valueOf(selectedBody.getInitialVelocity().getZ()));
        
        selectedBody.setCurrentPosition(0, 0, 0);


    }

    //when the Save values button is clicked:
    public void handleOnActionBtnSave() {

        if (selectedBody != null) {

            if (Double.valueOf(tfMass.getText())* Math.pow(10, 12) < minMass) {
                tfInfoMass.setVisible(false);
                tfWarningMass.setText("This mass is too small. Please enter a bigger value.");
                tfWarningMass.setVisible(true);
            } else if (Double.valueOf(tfMass.getText())* Math.pow(10, 12) > maxMass) {
                tfInfoMass.setVisible(false);
                tfWarningMass.setText("This mass is too big. Please enter a smaller value.");
                tfWarningMass.setVisible(true);
            } else {
                selectedBody.setMass(Double.valueOf(tfMass.getText()));
                tfWarningMass.setVisible(false);
                tfInfoMass.setVisible(true);
            }
            
            if (Math.abs(Double.valueOf(tfPosX.getText()).doubleValue()) > posBounds || Math.abs(Double.valueOf(tfPosY.getText()).doubleValue()) > posBounds || Math.abs(Double.valueOf(tfPosZ.getText()).doubleValue()) > posBounds) {
                tfWarningPosition.setText("Body is out of bounds (max 400).");
                tfWarningPosition.setVisible(true);
            } else {
                tfWarningPosition.setVisible(false);
                selectedBody.setInitialPosition(Double.valueOf(tfPosX.getText()), Double.valueOf(tfPosY.getText()), Double.valueOf(tfPosZ.getText()));
            }
            
            if (Math.abs(Double.valueOf(tfVelX.getText()).doubleValue()) > maxSpeed || Math.abs(Double.valueOf(tfVelY.getText()).doubleValue()) > maxSpeed || Math.abs(Double.valueOf(tfVelZ.getText()).doubleValue()) > maxSpeed) {
                tfWarningVelocity.setText("Velocity too big (max 15000)");
                tfWarningVelocity.setVisible(true);
            } else {
                tfWarningVelocity.setVisible(false);
                selectedBody.setInitialVelocity(Double.valueOf(tfVelX.getText()), Double.valueOf(tfVelY.getText()), Double.valueOf(tfVelZ.getText()));
            }
            
            selectedBody.setInitialVelocity(Double.valueOf(tfVelX.getText()), Double.valueOf(tfVelY.getText()), Double.valueOf(tfVelZ.getText()));
        }

    }

    //when the Start simulation button is clicked:
    public void handleOnActionBtnStart() {
        ThreeBodyProblem.body_1 = bodies[0];
        ThreeBodyProblem.body_2 = bodies[1];
        ThreeBodyProblem.body_3 = bodies[2];
        ThreeBodyProblem.originBody = new Body("Origin Body");
        ThreeBodyProblem.originBody.setCurrentPosition(0, 0, 0);
        ThreeBodyProblem.focusBody = ThreeBodyProblem.originBody;
        ThreeBodyProblem.bodyQueue = new CircularQueue(ThreeBodyProblem.body_1, ThreeBodyProblem.body_2, ThreeBodyProblem.body_3, ThreeBodyProblem.originBody);
        if(checkBodiesOk(bodies[0], bodies[1], bodies[2])){
            startSimulation(ThreeBodyProblem.s1);
        }

    }
    
    //when button help is clicked
    public void handleOnActionBtnHelp() {
        helpPane.setVisible(true);
    }
    
    //when button to close help menu is clicked
    public void handleOnActionBtnClose() {
        helpPane.setVisible(false);
    }
    
    //method to start the simulation (used in handleOnActionBtnStart())
    public void startSimulation(Stage stage) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
                
        GLCapabilities caps = new GLCapabilities(profile);
        
        GLJPanel canvas = new GLJPanel(caps);
        
        StackPane frame = new StackPane();
        
        canvas.addGLEventListener(new ThreeBodyProblem());
        canvas.addKeyListener(new ThreeBodyProblem());
        canvas.setPreferredSize(new Dimension((Toolkit.getDefaultToolkit().getScreenSize().width-100), (Toolkit.getDefaultToolkit().getScreenSize().height)-100));
                
        SwingNode node = new SwingNode();
        node.setContent(canvas);
        
        prompt = new Text("1X");
        prompt.setFill(Color.WHITE);
        prompt.setFont(new Font("Arial", 30));
        prompt.setStyle("-fx-font-weight: bold; -fx-text-alignment: center");
        
        HBox hBox = new HBox(prompt);
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setPadding(new Insets(20, 0, 0, 0));
        
        frame.getChildren().addAll(node, hBox);
        
        frame.setOnKeyPressed(event -> {
            try{
                
            //Reset
            if(event.getCode() == KeyCode.R){
                reset(stage);  
            }
            
            //pause
            if(event.getCode() == KeyCode.SPACE && isPaused) { 
                isPaused = false;
            } else {
                if(event.getCode() == KeyCode.SPACE) isPaused = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(0);
        }});
        
        frame.setVisible(true);
        
        Scene scene2 = new Scene(frame);
        stage.hide();
        stage.setScene(scene2);
        stage.setMaximized(true);
        stage.setTitle("Three-Body Problem Simulator");
        stage.show();
        
        FPSANIM.add(canvas);
        FPSANIM.start();
        
        
    }
    
    //method to reset the simulation (go back to home page)
    public void reset(Stage stage) throws IOException{
        FPSANIM.stop();
        ThreeBodyProblem.fpsTime = 1;
        ThreeBodyProblem.translateZ = 5;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("ThreeBodies.fxml")));
        stage.hide();
        ThreeBodyProblem.currentAngle = 0;
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    
    //method to display text on the screen (during simulation)
    public static void setPrompt(String text){
        prompt.setText(text);
    }
    
    private boolean checkBodiesOk(Body body1, Body body2, Body body3) {
        boolean firstBodyOk = (body1.getMassInKilos() > minMass) && (body1.getMassInKilos() < maxMass) && 
                (body1.getInitialPosition().getX() < posBounds) && (body1.getInitialPosition().getY() < posBounds) && 
                (body1.getInitialPosition().getZ() < posBounds) && (body1.getInitialVelocity().getX() < maxSpeed) && 
                (body1.getInitialVelocity().getY() < maxSpeed) && (body1.getInitialVelocity().getZ() < maxSpeed);
        boolean secondBodyOk = (body2.getMassInKilos() > minMass) && (body2.getMassInKilos() < maxMass) && 
                (body2.getInitialPosition().getX() < posBounds) && (body2.getInitialPosition().getY() < posBounds) && 
                (body2.getInitialPosition().getZ() < posBounds) && (body2.getInitialVelocity().getX() < maxSpeed) && 
                (body2.getInitialVelocity().getY() < maxSpeed) && (body2.getInitialVelocity().getZ() < maxSpeed);
        boolean thirdBodyOk = (body1.getMassInKilos() > minMass) && (body1.getMassInKilos() < maxMass) && 
                (body3.getInitialPosition().getX() < posBounds) && (body3.getInitialPosition().getY() < posBounds) && 
                (body3.getInitialPosition().getZ() < posBounds) && (body3.getInitialVelocity().getX() < maxSpeed) && 
                (body3.getInitialVelocity().getY() < maxSpeed) && (body3.getInitialVelocity().getZ() < maxSpeed);
        if (firstBodyOk && secondBodyOk && thirdBodyOk) {
            return true;
        } else {
            return false;
        }
    }

}