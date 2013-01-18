/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Capstone
 */
public class ClassB implements AnInterface{

    public int n;
    public String s1, s2;
    
    @Override
    public int getNum() {
        return n;
    }

    @Override
    public String getStr() {
        return s1;
    }
    
    @Override
    public String toString() {
        return s1 + s2 + n;
    }

    public ClassB(int n, String s1, String s2) {
        this.n = n;
        this.s1 = s1;
        this.s2 = s2;
    }
    
}
