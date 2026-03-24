import javax.swing.*;

public class Guis {

    public static void StartGui(){
        JFrame stratframe = new JFrame();
        stratframe.setSize(500, 500);
        stratframe.setLayout(null);
        stratframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton layoutKiezen = new JButton("Layout Kiezen");
        layoutKiezen.setBounds(135, 200, 230, 50); // mooier gecentreerd

        stratframe.add(layoutKiezen);
        stratframe.setVisible(true);
        layoutKiezen.addActionListener(e -> {
            System.out.println("Knop geklikt!");
        });

    }
}