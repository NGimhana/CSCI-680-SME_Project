/*******************************************************************************
 * Copyright (c) 2013, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.eclipse.jdt.core.tests.compiler.parser.CompletionParserTest18;
import org.eclipse.jdt.core.tests.compiler.parser.SelectionParserTest18;
import org.eclipse.jdt.core.tests.model.CompletionTests18;
import org.eclipse.jdt.core.tests.model.JavaElement8Tests;
import org.eclipse.jdt.core.tests.model.JavaSearchBugs8Tests;
import org.eclipse.jdt.core.tests.model.ResolveTests18;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RunOnlyAssistModelTests18 extends TestCase {

    public  RunOnlyAssistModelTests18(String name) {
        super(name);
    }

    public static Class[] getAllTestClasses() {
        return new Class[] { ResolveTests18.class, CompletionParserTest18.class, CompletionTests18.class, SelectionParserTest18.class, JavaSearchBugs8Tests.class, JavaElement8Tests.class };
    }

    public static Test suite() {
        TestSuite ts = new TestSuite(RunOnlyAssistModelTests18.class.getName());
        Class[] testClasses = getAllTestClasses();
        addTestsToSuite(ts, testClasses);
        return ts;
    }

    public static void addTestsToSuite(TestSuite suite, Class[] testClasses) {
        for (int i = 0; i < testClasses.length; i++) {
            Class testClass = testClasses[i];
            // call the suite() method and add the resulting suite to the suite
            try {
                Method suiteMethod = //$NON-NLS-1$
                testClass.getDeclaredMethod(//$NON-NLS-1$
                "suite", //$NON-NLS-1$
                new Class[0]);
                Test test = (Test) suiteMethod.invoke(null, new Object[0]);
                suite.addTest(test);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
