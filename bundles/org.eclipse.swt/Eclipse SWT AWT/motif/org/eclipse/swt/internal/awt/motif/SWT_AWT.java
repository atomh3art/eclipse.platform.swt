/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.awt.motif;

 
import java.lang.reflect.Constructor;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

/* AWT Imports */
import java.awt.EventQueue;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ComponentEvent;

public class SWT_AWT {

public static Panel new_Panel (final Composite parent) {
	int handle = parent.embeddedHandle;
	/*
	 * Some JREs have implemented the embedded frame constructor to take an integer
	 * and other JREs take a long.  To handle this binary incompatability, use
	 * reflection to create the embedded frame.
	 */
	Class clazz = null;
	try {
		clazz = Class.forName("sun.awt.X11.XEmbeddedFrame");
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);		
	}
	Constructor constructor = null;
	try {
		constructor = clazz.getConstructor (new Class [] {int.class});
	} catch (Throwable e1) {
		try {
			constructor = clazz.getConstructor (new Class [] {long.class});
		} catch (Throwable e2) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e2);
		}
	}
	Object value = null;
	try {
		value = constructor.newInstance (new Object [] {new Integer (handle)});
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	final Frame frame = (Frame) value;	
	Panel panel = new Panel ();
	frame.add (panel);
	parent.getShell ().addListener (SWT.Move, new Listener () {
		public void handleEvent (Event e) {
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispatchEvent (new ComponentEvent (frame, ComponentEvent.COMPONENT_MOVED));
				}
			});
		}
	});
	parent.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			parent.setVisible(false);
			frame.dispose ();
		}
	});
	return panel;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}
}
