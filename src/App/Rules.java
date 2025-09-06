package App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Rules extends JFrame implements ActionListener {

  JButton start,back;
  String userName;
  Rules(String name){
    userName = name;

    getContentPane().setBackground(Color.WHITE);
    
JLabel heading = new JLabel("Welcome "+ name+" to QUIZ TEST");
heading.setBounds(150,100,700,30);
heading.setFont(new Font("viner Hand ITC",Font.BOLD,28));
heading.setForeground (new Color (22,99,54));
add(heading);

JLabel rules = new JLabel();
rules.setBounds(70,150,700,350);
rules.setFont(new Font("Tahoma",Font.PLAIN,16));
rules.setForeground (new Color (22,99,54));
rules.setText(
  "<html>"+
  "1. Participation in the Quiz is free and open to all." + "<br><br>" +
  "2. There are total 10 questions." + "<br><br>" +
  "3. You will get 5 minutes to answer Quiz question." + "<br><br>" +
  "4. No electronic device is allowed." + "<br><br>" +
  "5. No talking." + "<br><br>" +
    "<div style='margin-left:140px;'>Best of Luck" + "<br><br><br>" +
  "</html>"
);
add(rules);

    start = new JButton("Start");
    start.setBounds(450,500,100,30);
    start.setBackground(new Color(22,99,54));
    start.setForeground(Color.WHITE);
    start.addActionListener(this);
    add(start);

    back = new JButton("Back");
    back.setBounds(300,500,100,30);
    back.setBackground(new Color(22,99,54));
    back.setForeground(Color.WHITE);   
    back.addActionListener(this); 
    add(back);

    ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
    Image i = i1.getImage().getScaledInstance(800, 650, Image.SCALE_DEFAULT);
    ImageIcon i2 = new ImageIcon(i);
    JLabel image = new JLabel(i2);
    image.setBounds(0, 0, 800, 650);
    add(image);



    setSize(800,650);
    setLocation(350,100);
    setLayout(null);
    setVisible(true);
    
  }

  @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == start) {
        setVisible(false);
        new Quiz(userName); // Start the quiz when Start button is clicked
    } else if (e.getSource() == back) {
        setVisible(false);
        new Login();
    }
}

}
