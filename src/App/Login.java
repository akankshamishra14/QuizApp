package App;

import java.awt.*;
import javax.swing.*;

public class Login extends JFrame {
    Login(){
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/login.png"));
        Image i = i1.getImage().getScaledInstance(550,500,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i);
        JLabel image = new JLabel(i2);
        image.setBounds(450,0,550,500);
        add(image);


       setSize(1000, 500);
       setLocation( 200, 150);
       setVisible(true);
    }
    public static void main(String[] args){
        new Login();
    }
}
