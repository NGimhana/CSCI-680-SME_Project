package p;

class A {

    Bar.B[] field = new Bar.B[] { new Bar.B() };

    void f() {
        int temp;
    }
}

class Bar {

    static class B {
    }
}
