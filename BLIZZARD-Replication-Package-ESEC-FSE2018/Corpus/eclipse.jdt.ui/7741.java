package pkg1;

public class A implements PullUpToInterfaceBug.Foo {

    public void b() {
    }

    /* (non-Javadoc)
	 * @see p.PullUpToInterfaceBug.Foo#baz4()
	 */
    @Override
    public void baz4() {
    // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see p.PullUpToInterfaceBug.Foo#baz3()
	 */
    @Override
    public void baz3() {
    // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see p.PullUpToInterfaceBug.Foo#baz2()
	 */
    @Override
    public void baz2() {
    // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see p.PullUpToInterfaceBug.Foo#baz1()
	 */
    @Override
    public void baz1() {
    // TODO Auto-generated method stub
    }
}

class PullUpToInterfaceBug {

    interface Foo {

        void baz4();

        void baz3();

        void baz2();

        void baz1();
    }

    static class B implements Foo {

        public void baz1() {
        }

        public void baz2() {
        }

        public void baz3() {
        }

        public void baz4() {
        }
    }
}
