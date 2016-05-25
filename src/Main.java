import API.APIVk;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws AWTException, IOException, ParseException, InterruptedException {
        PopupMenu popup = new PopupMenu();
        MenuItem menuItem = new MenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popup.add(menuItem);
        SystemTray systemTray = SystemTray.getSystemTray();
        URL icon = new URL("*****");
        Image image = Toolkit.getDefaultToolkit().getImage(icon);
        TrayIcon trayIcon = new TrayIcon(image,"VKNotifer", popup);
        trayIcon.setImageAutoSize(true);
        systemTray.add(trayIcon);


        //цикл принятия сообщений
        String OldMsg = APIVk.GetMsg("1");
        for (;;) {
            Thread.sleep(3000);
            String NewMsg = APIVk.GetMsg("1");
            if (!NewMsg.equals(OldMsg)) {
                OldMsg = NewMsg;
                NewMsgDisplay(trayIcon,APIVk.UserName, NewMsg);
            }
        }
    }



    private static void NewMsgDisplay(TrayIcon trayIcon,String head, String text) {
        trayIcon.displayMessage(head, text, TrayIcon.MessageType.INFO);
    }
}
