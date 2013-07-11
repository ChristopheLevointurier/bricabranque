/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author CLV
 */
public class UtilsPolygon {
    
    
    public static boolean pointInsidePolygon(ArrayList<Point> p, Point mp) {
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
        //droite pentues non prises en compte
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
    public static boolean polygonInPolygon(ArrayList<Point> p_in, ArrayList<Point> p_out, boolean strict) {
        ArrayList<Point> filtrPolyIn = new ArrayList<>();
        ArrayList<Point> filtrPolyOut = new ArrayList<>();

        for (int i = 0; i < p_in.size(); i++) {
            Point p = new Point((int) Math.round(p_in.get(i).getX()), (int) Math.round(p_in.get(i).getY()));
            if (!contains(p_out, p)) {
                filtrPolyIn.add(p);
            } else {
                if (strict) {
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
                if (strict) {
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


    
}
