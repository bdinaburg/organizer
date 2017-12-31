package mongodbtester;

/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly 
*/
// PopupMenuExample.java
// A simple example of JPopupMenu. 
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

public class PopupMenuExample extends JPanel {

  public JPopupMenu popup;

  public PopupMenuExample() {
    popup = new JPopupMenu();
    JMenuItem item;
    item = new JMenuItem("Delete");
    item.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			System.out.print("dete");
		}
	});

    popup.add(item);
    popup.add(item = new JMenuItem("Upate"));
    popup.setBorder(new BevelBorder(BevelBorder.RAISED));

    addMouseListener(new MousePopupListener());
  }

  // An inner class to check whether mouse events are the popup trigger
  class MousePopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      checkPopup(e);
    }

    public void mouseClicked(MouseEvent e) {
      checkPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
      checkPopup(e);
    }

    private void checkPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        popup.show(PopupMenuExample.this, e.getX(), e.getY());
      }
    }

  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Popup Menu Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new PopupMenuExample());
    frame.setSize(300, 300);
    frame.setVisible(true);
  }
}

           