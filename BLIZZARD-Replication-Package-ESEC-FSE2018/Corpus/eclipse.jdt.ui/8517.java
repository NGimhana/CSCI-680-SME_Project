package p;

import java.util.Vector;

class A {

    void k() {
        Vector<Vector<String>> v1 = new Vector<Vector<String>>();
        Vector<Vector<String>> v2 = new Vector<Vector<String>>();
        Vector<String> v3 = new Vector<String>();
        // String <= E[v3] --> String is
        v3.add(new String("fff"));
        // not parametric --> nothing to unify
        // v3 <= E[v2] --> 2. unify (E[v3], E[E[v2]])
        v2.add(v3);
        // E[v2] <= E[v1] --> 1. unify (E[E[v2]], E[E[v1]])
        v1.add(v2.get(0));
    }
}
