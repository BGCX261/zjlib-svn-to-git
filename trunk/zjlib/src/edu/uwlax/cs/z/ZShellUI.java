/* This file is part of ZJLib, a library written in java to support 
 the implementation of specifications written in Z-Notation in Java 5.
 
 Copyright (C) 2007  Moritz Eysholdt <Moritz@Eysholdt.de>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package edu.uwlax.cs.z;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ZShellUI {

	private Throwable lastException;

	private boolean running;

	private Object zClass;

	protected ArrayList<Object> createMethodParams(Method method,
			ArrayList<String> args) throws Exception {
		int i = 1;
		ArrayList<Object> param = new ArrayList<Object>();
		for (Class c : method.getParameterTypes()) {
			if (ZPrimitive.class.isAssignableFrom(c)) {
				param.add(ZPrimitive.getInstance(c, args.get(i)));
			} else
				try {
					param.add(c.getConstructor(String.class).newInstance(
							args.get(i)));
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(
							"Every class that is a parameter of the method "
									+ method.getName()
									+ " needs to provide constructor that accepts a single string "
									+ "as parameter or the class has to implement the "
									+ "interface ZNamable.");
				}
			i++;
		}
		return param;
	}

	protected boolean cmdExecMethod(ArrayList<String> args) {
		Method method = null;
		ArrayList<Method> methods = getMethods();
		if (args.get(0).startsWith("o")) {
			int index = Integer.parseInt(args.get(0).substring(1));
			if ((index < 0) && (index >= methods.size()))
				throw new RuntimeException("Invalid variable index: " + index);
			method = methods.get(index);
		} else {
			for (Method m : methods)
				if (m.getName().equals(args.get(0)))
					method = m;
			if (method == null)
				return false;
		}
		if ((args.size() - 1) != method.getParameterTypes().length)
			throw new RuntimeException("The operation \"" + method.getName()
					+ "\" needs exactly " + method.getParameterTypes().length
					+ " parameter(s).");

		try {
			ArrayList<Object> param = createMethodParams(method, args);
			Object r = method.invoke(zClass, param.toArray());
			if (method.getReturnType().equals(void.class))
				System.out.println("ok");
			else
				System.out.println("ok. result:\n" + r);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	protected void cmdShowField(ArrayList<String> args) {
		int index = Integer.parseInt(args.get(0).substring(1));
		ArrayList<Field> fields = getFields();
		if ((index < 0) && (index >= fields.size()))
			throw new RuntimeException("Invalid variable index: " + index);
		Field field = fields.get(index);

		System.out.println(field.getName() + ":");
		try {
			Object o = field.get(zClass);
			if (o == null)
				System.out.println("null");
			else
				System.out.println(o.toString());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private Class findZClass() {
		Class c = zClass.getClass();
		while (c != null && !c.getSimpleName().endsWith("Z"))
			c = c.getSuperclass();
		if (c == null)
			c = zClass.getClass();
		return c;
	}

	private ArrayList<Field> getFields() {
		ArrayList<Field> r = new ArrayList<Field>();
		for (Field f : findZClass().getDeclaredFields())
			if (!Modifier.isPrivate(f.getModifiers())
					&& !Modifier.isStatic(f.getModifiers()))
				r.add(f);
		return r;
	}

	private ArrayList<Method> getMethods() {
		ArrayList<Method> r = new ArrayList<Method>();
		for (Method m : findZClass().getDeclaredMethods())
			if (!m.getName().endsWith("Impl")
					&& !Modifier.isPrivate(m.getModifiers())
					&& !Modifier.isStatic(m.getModifiers()))
				r.add(m);
		return r;
	}

	public Object getZClass() {
		return zClass;
	}

	private void printCommands() {
		System.out.println("Commands for Class "
				+ zClass.getClass().getSimpleName() + ": ");

		int i = 0;
		System.out.println("  Viewable Variables:");
		for (Field f : getFields()) {
			System.out.println("    v" + (i++) + ": "
					+ f.getType().getSimpleName() + " " + f.getName());
		}

		i = 0;
		System.out.println("  Executable Operations:");
		for (Method m : getMethods()) {
			System.out.print("    o" + (i++) + ": " + m.getName());
			for (Class p : m.getParameterTypes()) {
				System.out.print(" <" + p.getSimpleName() + ">");
			}
			System.out.println();
		}
		System.out.println("  Other Commands:");
		System.out.println("    h: Print this help");
		System.out.println("    e: Print last exception");
		System.out.println("    q: Quit");
	}

	protected void processCommand(ArrayList<String> args) {
		// System.out.println("Cmd: " + args);
		if (args.size() < 1)
			return;
		if (args.get(0).startsWith("v"))
			cmdShowField(args);
		else if (args.get(0).equals("h"))
			printCommands();
		else if (args.get(0).equals("q"))
			running = false;
		else if (args.get(0).equals("e")) {
			System.out.print("Last Exception: ");
			if (lastException == null)
				System.out.println("None.");
			else
				lastException.printStackTrace(System.out);
			System.out.println();
		} else if (!cmdExecMethod(args))
			throw new RuntimeException("Unknown Command: \"" + args.get(0)
					+ "\". Press \"h<ENTER>\" for help.");

	}

	public void runShell() {
		printCommands();
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String s;
		try {
			running = true;
			while (running && ((s = r.readLine()) != null)) {
				ArrayList<String> args = new ArrayList<String>();
				boolean esc = false;
				for (String a : s.split("\"")) {
					if (esc)
						args.add(a);
					else
						for (String c : a.split(" "))
							if (c.trim().length() != 0)
								args.add(c.trim());
					esc = !esc;
				}
				try {
					processCommand(args);
				} catch (Exception e) {
					handleException(e);
				}
			}
		} catch (IOException e) {
			handleException(e);
		}
	}

	private void handleException(Throwable e) {
		lastException = e;
		while (e != null) {
			if ((e instanceof AssertionError))
				break;
			else
				e = e.getCause();
		}
		if (e == null)
			e = lastException;
		String msg = (e.getMessage() == null) ? "(no message)" : e.getMessage();
		System.out.println(e.getClass().getSimpleName() + ": " + msg);
		if (e instanceof AssertionError) {
			StackTraceElement s = e.getStackTrace()[0];
			System.out.println("\tat " + s.getClassName() + "."
					+ s.getMethodName() + "(" + s.getFileName() + ":"
					+ s.getLineNumber() + ")");
		}
	}

	public void setZClass(Object class1) {
		zClass = class1;
	}
}
