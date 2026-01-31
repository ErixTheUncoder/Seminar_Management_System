import java.awt.Color;
import javax.swing.*;

public class mainWindow extends JFrame
{

  public mainWindow(){
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(Color.WHITE);

    this.add(mainPanel);

    //Label
    JLabel label = new JLabel();
    label.setText("somethingsoemthing");

    mainPanel.add(label); //add the label to the window

    //Button
    JButton button = new JButton();
    button.setBounds(200,100,100,50);
    
    mainPanel.add(button);


  //Setup window
    this.setTitle("Seminar Management System"); //Set Title
    this.setSize(720,640); //Set the window size
    this.setVisible(true); //Make the window visible
    this.setResizable(false); //Make the window not resizable
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close the application instead of hiding
  }
    public static void main(String[] args)
    {  //Create new Jframe called window
        mainWindow window =new mainWindow();

        

    }
}
