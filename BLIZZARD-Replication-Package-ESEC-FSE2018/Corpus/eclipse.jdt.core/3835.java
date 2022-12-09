/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.env;

public interface IGenericField {

    /**
 * Answer an int whose bits are set according the access constants
 * defined by the VM spec.
 */
    // We have added AccDeprecated & AccSynthetic.
    int getModifiers();

    /**
 * Answer the name of the field.
 */
    char[] getName();
}
