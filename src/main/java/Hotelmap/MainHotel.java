package Hotelmap;

import Knop.FileChooser;

public class MainHotel {

    public static void start(){
        Guis.StartGui();
    }

    public static void Choose(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.openFile(); // make sure method name matches!
    }
}
/*package Hotelmap;

import Knop.FileChooser;

public class MainHotel {





    public void MainHotel() {

    }
    public static void start(){
        Guis startgui = new Guis();
        Guis.StartGui();



    }
    public static void Choose(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.filechooser();


    }


}*/