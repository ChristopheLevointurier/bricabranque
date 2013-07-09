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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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


        this.getContentPane().add(but, BorderLayout.SOUTH);
        this.getContentPane().add(z, BorderLayout.CENTER);


        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        bp.add(swich);
        bp.add(stat);

        this.getContentPane().add(bp, BorderLayout.NORTH);
        z.addMouseListener(this);
        z.addMouseMotionListener(this);
        pack();
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
        if (polygonInPolygon(inPolygon, outPolygon)) {
            swich.setText("Polygone dedans");
            swich.setBackground(Color.green);
        } else {
            swich.setText("Polygone dehors");
            swich.setBackground(Color.red);
        }
    }

    public void test(int x, int y) {
        if (pointInsidePolygon(outPolygon, new Point(x, y))) {
            swich.setText("souris dedans");
            swich.setBackground(Color.green);
        } else {
            swich.setText("souris dehors");
            swich.setBackground(Color.red);
        }
    }

    private static boolean pointInsidePolygon(ArrayList<Point> p, Point mp) {
        //Polygon.contains(point)
        boolean inPoly = false;
        int j = p.size() - 1;
        for (int i = 0; i < p.size(); i++) {
            Point polyI = new Point((int) Math.round(p.get(i).getX()), (int) Math.round(p.get(i).getY()));
            Point polyJ = new Point((int) Math.round(p.get(j).getX()), (int) Math.round(p.get(j).getY()));

            if ((polyI.y < mp.getY() && polyJ.y >= mp.getY() || polyJ.y < mp.getY() && polyI.y >= mp.getY())
                    && (polyI.x <= mp.getX() || polyJ.x <= mp.getX())) {
                if (polyI.x + (mp.getY() - polyI.y) / (polyJ.y - polyI.y) * (polyJ.x - polyI.x) < mp.getX()) {
                    inPoly = !inPoly;
                }
            }
            j = i;
        }

        return inPoly;
    }

    /**
     * test si un point c est sur une droite ab
     */
    private static boolean intersectionPtLine(Point pa, Point pb, Point pc) {

        if (pa.x == pb.x && pa.x == pc.x && ((pc.y < pa.y && pc.y > pb.y) || (pc.y > pa.y && pc.y < pb.y))) {
            return true;
        }
        if (pa.y == pb.y && pa.y == pc.y && ((pc.x < pa.x && pc.x > pb.x) || (pc.x > pa.x && pc.x < pb.x))) {
            return true;
        }
        //ajout droite pentues
        return false;
    }

    /**
     *
     * test si le point est dans le polygone
     *
     */
    private static boolean contains(ArrayList<Point> p, Point mp) {
        for (int i = 0; i < p.size(); i++) {
            if (equalPoint(p.get(i), mp)) {
                return true;
            }
        }
        //test si le point coupe une droite
        Point temp = p.get(0);
        for (Point pt : p) {
            if (intersectionPtLine(temp, pt, mp)) {
                return true;
            }
            temp = pt;
        }
        if (intersectionPtLine(temp, p.get(0), mp)) {
            return true;
        }
        return false;
    }

    /**
     * TestCollisionPolygons si le polygon source est inclu dans le polygon de
     * référence
     *
     * @param p_in : polygon source
     * @param p_out : polygon de référence
     * @return true si inclue false sinon
     *
     */
    public static boolean polygonInPolygon(ArrayList<Point> p_in, ArrayList<Point> p_out) {
        ArrayList<Point> filtrPolyIn = new ArrayList<>();
        ArrayList<Point> filtrPolyOut = new ArrayList<>();

        for (int i = 0; i < p_in.size(); i++) {
            Point p = new Point((int) Math.round(p_in.get(i).getX()), (int) Math.round(p_in.get(i).getY()));
            if (!contains(p_out, p)) {
                filtrPolyIn.add(p);
            } else {
                if (strict.isSelected()) {
                    return false;
                }
            }
        }

        for (Point p : filtrPolyIn) {
            if (!pointInsidePolygon(p_out, p)) {
                System.out.println("polygonInPolygon fail points internes:p=" + p);
                System.out.println("tableau=" + p_out);
                return false;
            }
        }

        for (int i = 0; i < p_out.size(); i++) {
            Point p = new Point((int) Math.round(p_out.get(i).getX()), (int) Math.round(p_out.get(i).getY()));
            if (!contains(p_in, p)) {
                filtrPolyOut.add(p);
            } else {
                if (strict.isSelected()) {
                    return false;
                }
            }
        }

        for (Point p : filtrPolyOut) {
            if (pointInsidePolygon(p_in, p)) {
                System.out.println("polygonInPolygon fail points externes:p=" + p);
                System.out.println("tableau=" + p_in);
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne l'égalité des points A et B
     *
     * @param pt_a : point A
     * @param pt_b : point B
     *
     */
    private static boolean equalPoint(Point pt_a, Point pt_b) {
        return (pt_a.getX() == pt_b.getX()) && (pt_a.getY() == pt_b.getY());
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
