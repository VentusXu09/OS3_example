/*
 * Created on 2004-6-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package stw.os.assignment.file;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author scorpio
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Test {

	public static void main(String[] args) {
		FileSystem filesystem = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("filesystem.dat"));
			filesystem = (FileSystem) in.readObject();
			in.close();

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			filesystem = new FileSystem();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			UserInterface frame = new UserInterface(filesystem);
			
			frame.addWindowListener(new WindowHandler(filesystem));
			frame.show();
		}

	}
}

class WindowHandler extends WindowAdapter {
	private FileSystem filesystem;

	public WindowHandler(FileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public void windowClosing(WindowEvent event) {
		ObjectOutputStream out = null;
		try {
			out =
				new ObjectOutputStream(new FileOutputStream("filesystem.dat"));
			out.writeObject(filesystem);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			System.exit(0);
		}
	}
}
