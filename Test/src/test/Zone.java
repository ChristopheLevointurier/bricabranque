/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author CLV
 */
public class Zone extends JPanel {

    private TestCollisionPolygons pere;

    public Zone(TestCollisionPolygons aThis) {
        pere = aThis;
        setPreferredSize(new Dimension(350, 300));
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.blue);
        g.setColor(Color.white);
        g.fillRect(0, 0, 500, 400);
        g.setColor(Color.BLACK);
        if (pere.outPolygon.isEmpty()) {
            return;
        }
        Point temp = pere.outPolygon.get(0);
        for (int i = 1; i < pere.outPolygon.size(); i++) {
            g.drawLine((int) temp.getX(), (int) temp.getY(), (int) pere.outPolygon.get(i).getX(), (int) pere.outPolygon.get(i).getY());
            temp = pere.outPolygon.get(i);
        }
        g.drawLine((int) temp.getX(), (int) temp.getY(), (int) pere.outPolygon.get(0).getX(), (int) pere.outPolygon.get(0).getY());


        g.setColor(Color.BLUE);
        if (pere.inPolygon.isEmpty()) {
            return;
        }
        temp = pere.inPolygon.get(0);
        for (int i = 1; i < pere.inPolygon.size(); i++) {
            g.drawLine((int) temp.getX(), (int) temp.getY(), (int) pere.inPolygon.get(i).getX(), (int) pere.inPolygon.get(i).getY());
            temp = pere.inPolygon.get(i);
        }
        g.drawLine((int) temp.getX(), (int) temp.getY(), (int) pere.inPolygon.get(0).getX(), (int) pere.inPolygon.get(0).getY());



        pere.testPolygon();
    }
}
