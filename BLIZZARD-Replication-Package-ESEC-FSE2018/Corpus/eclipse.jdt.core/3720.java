/*******************************************************************************
 * Copyright (c) 2005, 2008 BEA Systems, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    sbandow@bea.com - initial API and implementation
 *    
 *******************************************************************************/
package org.eclipse.jdt.apt.tests.annotations.filegen;

import java.io.IOException;
import java.io.PrintWriter;
import org.eclipse.jdt.apt.tests.annotations.BaseProcessor;
import org.eclipse.jdt.apt.tests.annotations.ProcessorTestStatus;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;

public class FileGenLocationAnnotationProcessor extends BaseProcessor {

    public  FileGenLocationAnnotationProcessor(AnnotationProcessorEnvironment env) {
        super(env);
    }

    public void process() {
        ProcessorTestStatus.setProcessorRan();
        try {
            Filer f = _env.getFiler();
            //$NON-NLS-1$
            PrintWriter pwa = f.createSourceFile("test.A");
            pwa.print(CODE_GEN_IN_PKG);
            pwa.close();
            //$NON-NLS-1$
            PrintWriter pwb = f.createSourceFile("B");
            pwb.print(CODE_GEN_AT_PROJ_ROOT);
            pwb.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected String CODE_GEN_IN_PKG = "package test;" + "\n" + "public class A" + "\n" + "{" + "\n" + "}";

    protected String CODE_GEN_AT_PROJ_ROOT = "public class B" + "\n" + "{" + "\n" + "    test.A a;" + "\n" + "}";
}
