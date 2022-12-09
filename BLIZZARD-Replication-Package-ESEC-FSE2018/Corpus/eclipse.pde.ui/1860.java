/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package a.classes.methods;

/**
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class RemoveProtectedMethodNoInstantiate {

    protected int protectedMethod(String arg) {
        return -1;
    }
}
