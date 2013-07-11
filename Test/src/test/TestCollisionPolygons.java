/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

/**
 *
 * @author CLV
 */
public class TestCollisionPolygons extends JFrame implements MouseListener, MouseMotionListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TestCollisionPolygons();
    }
    private JButton swich = new JButton("...");
    private JButton stat = new JButton("...");
    private JRadioButton un = new JRadioButton("Externe", true);
    private JRadioButton deux = new JRadioButton("Interne", false);
    private static JCheckBox strict = new JCheckBox("Strictement", false);
    private JButton reset = new JButton("Reset");
    private Zone z;
    public ArrayList<Point> outPolygon = new ArrayList<>();
    public ArrayList<Point> inPolygon = new ArrayList<>();

    public TestCollisionPolygons() {
        super("test algo");
        // JPanel p= new JPanel();
        ButtonGroup b = new ButtonGroup();
        b.add(un);
        b.add(deux);
        z = new Zone(this);

        JPanel but = new JPanel();
        but.setLayout(new BoxLayout(but, BoxLayout.X_AXIS));
        but.add(un);
        but.add(deux);
        but.add(strict);
        but.add(reset);


        setUndecorated(true);
        setLocationRelativeTo(null);
        // addMouseListener(new MouseAdapter() {
        //    @Override
        //    public void mouseClicked(MouseEvent e) {
        //        dispose();            }        });


        getContentPane().add(but, BorderLayout.SOUTH);
        getContentPane().add(z, BorderLayout.CENTER);


        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        bp.add(swich);
        bp.add(stat);

        getContentPane().add(bp, BorderLayout.NORTH);
        pack();
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 64, 64));
        setOpacity(0.90f);
        z.addMouseListener(this);
        z.addMouseMotionListener(this);
        setVisible(true);

        Action quitActionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        };

        InputMap inputMap = ((JPanel) getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE");
        ((JPanel) getContentPane()).getActionMap().put("CLOSE", quitActionListener);


        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outPolygon.clear();
                inPolygon.clear();
                repaint();
            }
        });



    }

    public void testPolygon() {
        if (UtilsPolygon.polygonInPolygon(inPolygon, outPolygon, strict.isSelected())) {
            swich.setText("Polygone dedans");
            swich.setBackground(Color.green);
        } else {
            swich.setText("Polygone dehors");
            swich.setBackground(Color.red);
        }
    }

    public void test(int x, int y) {
        if (UtilsPolygon.pointInsidePolygon(outPolygon, new Point(x, y))) {
            swich.setText("souris dedans");
            swich.setBackground(Color.green);
        } else {
            swich.setText("souris dehors");
            swich.setBackground(Color.red);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //  if (e.isControlDown()) {

        if (un.isSelected()) {
            outPolygon.add(new Point(e.getX(), e.getY()));
            System.out.println("nbr out=" + outPolygon.size());
        }
        if (deux.isSelected()) {
            inPolygon.add(new Point(e.getX(), e.getY()));
            System.out.println("nbr in=" + inPolygon.size());
        }
        repaint();
        //  } else {
        //     test(e.getX(), e.getY());
        //  }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        stat.setText("x=" + e.getX() + " y=" + e.getY());
        if (e.isControlDown()) {
            test(e.getX(), e.getY());
        }
    }
}
