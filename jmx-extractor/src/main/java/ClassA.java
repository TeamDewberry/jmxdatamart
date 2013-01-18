/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Capstone
 */
public class ClassA implements AnInterface{

    public int n1, n2;
    public String s;
    
    @Override
    public int getNum() {
        return n1;
    }

    @Override
    public String getStr() {
        return s;
    }
    
    @Override
    public String toString() {
        return s + n1 + n2;
    }
    
    public ClassA(int n1, int n2, String s) {
        this.n1 = n1;
        this.n2 = n2;
        this.s = s;
    }
    
}
