package org.purl.wf4ever.astrotaverna.aladin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import cds.aladin.Aladin;

public class MyClassLocation {

	//private static WeakHashMap<Object, File> classLocationFiles = new WeakHashMap<Object, File>();
	
	private ArrayList classLocationFiles = new ArrayList<File>();
	private ArrayList classLocationDirs = new ArrayList<File>();
	private ArrayList classLocationURIs = new ArrayList<File>();
	

	/**
	 * Get the canonical directory of the class file or jar file that the given
	 * class was loaded from. This method can be used to calculate the root
	 * directory of an installation.
	 * 
	 * @see #getClassLocationFile(Class)
	 * 
	 * @param theClass
	 *            The class which location is to be found
	 * @return The canonical directory of the class or jar file that this class
	 *         file was loaded from
	 * @throws IOException
	 *             if the canonical directory or jar file cannot be found
	 */
	public File getClassLocationDir(Class theClass)
			throws IOException {

		File file = getClassLocationFile(theClass);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		classLocationDirs.add(file);
		
		return file;
	}

	/**
	 * Get the canonical directory or jar file that the given class was loaded
	 * from. Note that this file might be a jar, use
	 * {@link #getClassLocationDir(Class)} if you want the directory that
	 * contains the JAR.
	 * 
	 * @see #getClassLocationURI(Class)
	 * @return The canonical directory or jar file that this class file was
	 *         loaded from
	 * @throws IOException
	 *             if the canonical directory or jar file cannot be found, or
	 *             the class was not loaded from a file:/// URI.
	 */
	public File getClassLocationFile(Class theClass)
			throws IOException {
		

		URI fileURI = getClassLocationURI(theClass);
		// Now that we have a URL, make sure that it is a "file" URL
		// as we need to coerce the URL into a File object
		if (!fileURI.getScheme().equals("file")) {
			throw new IOException("Class " + theClass
					+ " was not loaded from a file, but from " + fileURI);
		}
		// Coerce the URL into a File and check that it exists. Note that
		// the JVM <code>File(String)</code> constructor automatically
		// flips all '/' characters to '\' on Windows and there are no
		// valid escape characters so we would not have to worry about
		// URL encoded slashes.
		File file = new File(fileURI);
		if (!file.exists() || !file.canRead())
			throw new IOException("File/directory " + file + " where "
					+ theClass + " was loaded from was not found");
		File loadingFile = file.getCanonicalFile();
		classLocationFiles.add(loadingFile);
		return loadingFile;
	}

	/**
	 * Get the URI from where the given class was loaded from. Note that this
	 * might be pointing to a JAR or a location on the network. If you want the
	 * location as a file or directory, use {@link #getClassLocationFile(Class)}
	 * or {@link #getClassLocationDir(Class)}.
	 * 
	 * @see #getClassLocationURI(Class)
	 * @return The canonical directory or jar file that this class file was
	 *         loaded from
	 * @throws IOException
	 *             if the canonical directory or jar file cannot be found, or
	 *             the class was not loaded from a file:/// URI.
	 */
	public URI getClassLocationURI(Class theClass) throws IOException {

		// Get a URL for where this class was loaded from
		String classResourceName = theClass.getName().replace('.', '/')
				+ ".class";
		URL resource = theClass.getResource("/" + classResourceName);
		if (resource == null)
			throw new IOException("Source of class " + theClass + " not found");
		String resourcePath = null;
		String embeddedClassName = null;
		String protocol = resource.getProtocol();
		boolean isJar = (protocol != null) && (protocol.equals("jar"));
		if (isJar) {
			// Note: DON'T decode as the part-URL is not double-encoded
			// and otherwise %20 -> " " -> new URI() would fail
			resourcePath = resource.getFile();
			embeddedClassName = "!/" + classResourceName;
		} else {
			resourcePath = resource.toExternalForm();
			embeddedClassName = classResourceName;
		}
		int sep = resourcePath.lastIndexOf(embeddedClassName);
		if (sep >= 0) {
			resourcePath = resourcePath.substring(0, sep);
		}

		URI sourceURI;
		try {
			sourceURI = new URI(resourcePath).normalize();
		} catch (URISyntaxException e) {
			throw new IOException("Invalid URI: " + resourcePath);
		}
		classLocationURIs.add(sourceURI);
		return sourceURI;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		MyClassLocation myLocation = new MyClassLocation();
		
		File file = myLocation.getClassLocationFile(Aladin.class);
		
		System.out.println("Result form my location: "+file);
		
		Class theClass = Aladin.class;
		String classResourceName = theClass.getName().replace('.', '/')	+ ".class";
		URL resource = theClass.getResource("/" + classResourceName);
		System.out.println("resource: "+ resource);
				
		URL codeSource = theClass.getProtectionDomain().getCodeSource().getLocation();
		
		System.out.println("code source: " + codeSource);
		
		//me devuelve el path a la clase que se est‡ ejecutando.
		System.out.println("Class loader 1: " + ClassLoader.getSystemClassLoader().getResource(".").getPath());
		System.out.println("Class loader 2: " + ClassLoader.getSystemClassLoader().getResource(classResourceName).getPath());
		resource = Aladin.class.getClassLoader().getResource("/"+classResourceName);
		System.out.println("Class loader 3: "+ resource);
		
		
	}

}
