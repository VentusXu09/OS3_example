/* 
 * Created on 2004-6-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package stw.os.assignment.file;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * @author scorpio
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UserInterface extends JFrame {

	TableModel tablemodel;
	JTable table;
	JScrollPane tablepane;

	JTextArea editarea;
	JScrollPane editpane;

	JTextField path;

	Container contentPane;
	private JToolBar toolbar;
	private FileSystem filesystem;
	public UserInterface(FileSystem filesystem) {
		this.filesystem = filesystem;
		this.setTitle("FileManagement");
		this.setSize(800, 550);
		this.setResizable(false);

		contentPane = this.getContentPane();
		contentPane.setLayout(null);

		tablemodel = new FileInfoTableModel(filesystem, filesystem.getNow_dir_index());
		table = new JTable(tablemodel);
		tablepane = new JScrollPane(table);
		tablepane.setBounds(0, 70, 500, 480);

		ArrayList p = filesystem.getPath();
		String temppath = "";
		for (int i = 0; i < p.size(); ++i) {
			temppath = temppath + (String) p.get(i) + "\\";
		}
		path = new JTextField(temppath);
		path.setBounds(50, 50, 450, 20);
		path.setEditable(false);
		JTextField tag = new JTextField("Path:");
		tag.setEditable(false);
		tag.setBounds(0, 50, 50, 20);
		contentPane.add(tag);
		contentPane.add(path);

		toolbar = new JToolBar();
		toolbar.setLayout(null);
		toolbar.setBackground(Color.WHITE);
		toolbar.setBounds(0, 0, 500, 50);
		//		Icon newfileIcon;
		//		newfileIcon = new ImageIcon(getClass().getResource("newfile.jpg"));
		JButton button_newfile =
			new JButton(new ImageIcon(getClass().getResource("newfile.jpg")));
		button_newfile.setToolTipText("Create a new file");
		JButton button_newdir =
			new JButton(new ImageIcon(getClass().getResource("newdir.jpg")));
		button_newdir.setToolTipText("Create a new directory");
		JButton button_delfile =
			new JButton(new ImageIcon(getClass().getResource("delfile.jpg")));
		button_delfile.setToolTipText("Delete a file");
		JButton button_deldir =
			new JButton(new ImageIcon(getClass().getResource("deldir.jpg")));
		button_deldir.setToolTipText("Delete a directory");
		JButton button_intodir =
			new JButton(new ImageIcon(getClass().getResource("into.jpg")));
		button_intodir.setToolTipText("Into a directory");
		JButton button_updir =
			new JButton(new ImageIcon(getClass().getResource("up.jpg")));
		button_updir.setToolTipText("Upper directory");
		JButton button_format =
			new JButton(new ImageIcon(getClass().getResource("format.jpg")));
		button_format.setToolTipText("Format");
		JButton button_edit =
			new JButton(new ImageIcon(getClass().getResource("editfile.jpg")));
		button_edit.setToolTipText("Edit a file");
		JButton button_save =
			new JButton(new ImageIcon(getClass().getResource("savefile.jpg")));
		button_save.setToolTipText("Save editting file");
		JButton button_close =
			new JButton(new ImageIcon(getClass().getResource("closefile.jpg")));
		button_close.setToolTipText("Close editting file");
		//		button_newfile.setHorizontalTextPosition(SwingConstants.CENTER);
		//		button_newfile.setVerticalTextPosition(SwingConstants.BOTTOM);
		button_newfile.setBounds(0, 0, 50, 50);
		button_newfile.setName("newfile");
		button_newdir.setBounds(50, 0, 50, 50);
		button_newdir.setName("newdir");
		button_delfile.setBounds(100, 0, 50, 50);
		button_delfile.setName("delfile");
		button_deldir.setBounds(150, 0, 50, 50);
		button_deldir.setName("deldir");
		button_intodir.setBounds(200, 0, 50, 50);
		button_intodir.setName("intodir");
		button_updir.setBounds(250, 0, 50, 50);
		button_updir.setName("updir");
		button_format.setBounds(300, 0, 50, 50);
		button_format.setName("format");
		button_edit.setBounds(350, 0, 50, 50);
		button_edit.setName("edit");
		button_save.setBounds(400, 0, 50, 50);
		button_save.setName("save");
		button_close.setBounds(450, 0, 50, 50);
		button_close.setName("close");

		toolbar.add(button_newfile);
		toolbar.add(button_newdir);
		toolbar.add(button_delfile);
		toolbar.add(button_deldir);
		toolbar.add(button_intodir);
		toolbar.add(button_updir);
		toolbar.add(button_format);
		toolbar.add(button_edit);
		toolbar.add(button_save);
		toolbar.add(button_close);
		contentPane.add(toolbar);

		//		dirpanel.add(dirpane);
		contentPane.add(tablepane);

		Handler handler = new Handler(filesystem, this);

		button_newfile.addActionListener(handler);
		button_newdir.addActionListener(handler);
		button_delfile.addActionListener(handler);
		button_deldir.addActionListener(handler);
		button_intodir.addActionListener(handler);
		button_updir.addActionListener(handler);
		button_format.addActionListener(handler);
		button_edit.addActionListener(handler);
		button_save.addActionListener(handler);
		button_close.addActionListener(handler);
	}
}

class Handler implements ActionListener {
	private FileSystem filesystem;
	private UserInterface ui;
	private boolean editting;
	private String editting_file;
	public Handler(FileSystem filesystem, UserInterface ui) {
		this.filesystem = filesystem;
		this.ui = ui;
		editting = false;
	}
	public void actionPerformed(ActionEvent event) {
		String operation = ((JButton) event.getSource()).getName();
		if (operation.equals("newfile")) {
			if (!editting) {
				String fn =
					JOptionPane.showInputDialog("new file name", "newfile");
				if (fn != null) {
					filesystem.addNewFile(fn);
					ui.contentPane.remove(ui.tablepane);
					ui.tablemodel =
						new FileInfoTableModel(
							filesystem,
							filesystem.getNow_dir_index());
					ui.table = new JTable(ui.tablemodel);
					ui.tablepane = new JScrollPane(ui.table);
					ui.tablepane.setBounds(0, 70, 500, 480);
					ui.contentPane.add(ui.tablepane);
				}

			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("newdir")) {
			if (!editting) {
				String dn =
					JOptionPane.showInputDialog(
						"new directory name",
						"newdirectory");
				if (dn != null) {
					filesystem.addNewDir(dn);
					ui.contentPane.remove(ui.tablepane);
					ui.tablemodel =
						new FileInfoTableModel(
							filesystem,
							filesystem.getNow_dir_index());
					ui.table = new JTable(ui.tablemodel);
					ui.tablepane = new JScrollPane(ui.table);
					ui.tablepane.setBounds(0, 70, 500, 480);
					ui.contentPane.add(ui.tablepane);
				}

			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("delfile")) {
			if (!editting) {
				int rowcount = ui.table.getSelectedRowCount();
				if (rowcount > 0) {
					int row = ui.table.getSelectedRow();
					String type = (String) ui.tablemodel.getValueAt(row, 0);
					if (type.equals("File")) {
						String name = (String) ui.tablemodel.getValueAt(row, 1);
						filesystem.delFile(name);
						ui.contentPane.remove(ui.tablepane);
						ui.tablemodel =
							new FileInfoTableModel(
								filesystem,
								filesystem.getNow_dir_index());
						ui.table = new JTable(ui.tablemodel);
						ui.tablepane = new JScrollPane(ui.table);
						ui.tablepane.setBounds(0, 70, 500, 480);
						ui.contentPane.add(ui.tablepane);
					} else {
						JOptionPane.showMessageDialog(
							null,
							"Select a valid file first.");
					}
				} else {
					JOptionPane.showMessageDialog(
						null,
						"Select a valid file first.");
				}
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("deldir")) {
			if (!editting) {
				int rowcount = ui.table.getSelectedRowCount();
				if (rowcount > 0) {
					int row = ui.table.getSelectedRow();
					String type = (String) ui.tablemodel.getValueAt(row, 0);
					if (type.equals("Directory")) {
						String name = (String) ui.tablemodel.getValueAt(row, 1);
						filesystem.delDir(name);
						ui.contentPane.remove(ui.tablepane);
						ui.tablemodel =
							new FileInfoTableModel(
								filesystem,
								filesystem.getNow_dir_index());
						ui.table = new JTable(ui.tablemodel);
						ui.tablepane = new JScrollPane(ui.table);
						ui.tablepane.setBounds(0, 70, 500, 480);
						ui.contentPane.add(ui.tablepane);
					} else {
						JOptionPane.showMessageDialog(
							null,
							"Select a valid directory first.");
					}
				} else {
					JOptionPane.showMessageDialog(
						null,
						"Select a valid directory first.");
				}
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("intodir")) {
			if (!editting) {
				int rowcount = ui.table.getSelectedRowCount();
				if (rowcount > 0) {
					int row = ui.table.getSelectedRow();
					String type = (String) ui.tablemodel.getValueAt(row, 0);
					if (type.equals("Directory")) {
						String name = (String) ui.tablemodel.getValueAt(row, 1);
						filesystem.intoDir(name);
						filesystem.addPath(name);
						String temppath = ui.path.getText() + name + "\\";
						ui.path.setText(temppath);
						ui.contentPane.remove(ui.tablepane);
						ui.tablemodel =
							new FileInfoTableModel(
								filesystem,
								filesystem.getNow_dir_index());
						ui.table = new JTable(ui.tablemodel);
						ui.tablepane = new JScrollPane(ui.table);
						ui.tablepane.setBounds(0, 70, 500, 480);
						ui.contentPane.add(ui.tablepane);
					} else {
						JOptionPane.showMessageDialog(
							null,
							"Select a valid directory first.");
					}
				} else {
					JOptionPane.showMessageDialog(
						null,
						"Select a valid directory first.");
				}
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("updir")) {
			if (!editting) {
				filesystem.upDir();
				filesystem.delPath();
				ArrayList p = filesystem.getPath();
				String temppath = "";
				for (int i = 0; i < p.size(); ++i) {
					temppath = temppath + (String) p.get(i) + "\\";
				}
				ui.path.setText(temppath);
				ui.contentPane.remove(ui.tablepane);
				ui.tablemodel =
					new FileInfoTableModel(filesystem, filesystem.getNow_dir_index());
				ui.table = new JTable(ui.tablemodel);
				ui.tablepane = new JScrollPane(ui.table);
				ui.tablepane.setBounds(0, 70, 500, 480);
				ui.contentPane.add(ui.tablepane);
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("format")) {
			if (!editting) {
				filesystem.format();
				ui.path.setText("root\\");
				ui.contentPane.remove(ui.tablepane);
				ui.tablemodel =
					new FileInfoTableModel(filesystem, filesystem.getNow_dir_index());
				ui.table = new JTable(ui.tablemodel);
				ui.tablepane = new JScrollPane(ui.table);
				ui.tablepane.setBounds(0, 70, 500, 480);
				ui.contentPane.add(ui.tablepane);
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("edit")) {
			if (!editting) {
				int rowcount = ui.table.getSelectedRowCount();
				if (rowcount > 0) {
					int row = ui.table.getSelectedRow();
					String type = (String) ui.tablemodel.getValueAt(row, 0);
					if (type.equals("File")) {
						String name = (String) ui.tablemodel.getValueAt(row, 1);
						editting = true;
						editting_file = name;
						Block block = findblock("f" + name);
						ui.editarea = new JTextArea();
						ui.editarea.append(block.getData());
						ui.editarea.setBounds(0, 0, 300, 550);
						ui.editpane = new JScrollPane(ui.editarea);
						ui.editpane.setBounds(500, 0, 300, 550);
						ui.contentPane.add(ui.editpane);
						ui.contentPane.repaint();
					} else {
						JOptionPane.showMessageDialog(
							null,
							"Select a valid file first.");
					}
				} else {
					JOptionPane.showMessageDialog(
						null,
						"Select a valid file first.");
				}
			} else {
				JOptionPane.showMessageDialog(
					null,
					"The file \""
						+ this.editting_file
						+ "\" is being editted, you can save and close it first.");
			}
		} else if (operation.equals("save")) {
			String text = ui.editarea.getText();
			filesystem.saveFile(editting_file, text);
		} else if (operation.equals("close")) {
			this.editting = false;
			ui.contentPane.remove(ui.editpane);
			ui.contentPane.repaint();
		}
	}
	/**
	 * @return
	 */
	public boolean isEditting() {
		return editting;
	}

	/**
	 * @param b
	 */
	public void setEditting(boolean b) {
		editting = b;
	}

	public Block findblock(String name) {
		int i = 0;
		for (; i < filesystem.getFat().length; ++i) {
			if ((filesystem.getFat())[i] == 1) {
				if ((filesystem.getBlocks())[i].getName().equals(name)) {
					break;
				}
			}
		}
		return (filesystem.getBlocks())[i];
	}

}

class FileInfoTableModel extends AbstractTableModel {

	private FileSystem filesystem;
	private int current_dirindex;
	private Block block;

	public FileInfoTableModel(FileSystem filesystem, int current_dirindex) {
		this.filesystem = filesystem;
		this.current_dirindex = current_dirindex;

		block = (filesystem.getBlocks())[current_dirindex];
	}

	public int getRowCount() {
		return block.getDat().size();
	}
	public int getColumnCount() {
		return 5;
	}
	public Object getValueAt(int r, int c) {
		Set names = block.getDat().keySet();
		if (names.size() == 0) {
			if (c == 0) {
				return "empty";
			} else if (c == 1) {
				return null;
			} else if (c == 2) {
				return null;
			} else if (c == 3) {
				return null;
			} else if (c == 4) {
				return null;
			} else
				return null;
		} else {
			Iterator it = names.iterator();
			String[] anames = new String[names.size()];
			int d = 0;
			int f = names.size() - 1;
			while (it.hasNext()) {
				String s = (String) it.next();
				if (s.charAt(0) == 'f') {
					anames[f--] = s;
				} else {
					anames[d++] = s;
				}
			}

			Block tblock = findblock(anames[r]);
			if (c == 0) {
				if (tblock.getType() == '0') {
					return "Directory";
				} else
					return "File";
			} else if (c == 1) {
				return tblock.getName().substring(1);
			} else if (c == 2) {
				return (new Integer(tblock.getNow_size()).toString())+" Byte";
			} else if (c == 3) {
				return tblock.getCreation_date().toLocaleString();
			} else if (c == 4) {
				return tblock.getModification_date().toLocaleString();
			} else
				return null;
		}
	}
	public String getColumnName(int c) {
		if (c == 0) {
			return "Type";
		} else if (c == 1) {
			return "Name";
		} else if (c == 2) {
			return "Size";
		} else if (c == 3) {
			return "Creation Time";
		} else if (c == 4) {
			return "Latest Modification Time";
		} else
			return null;
	}

	public Block findblock(String name) {
		int i = 0;
		for (; i < filesystem.getFat().length; ++i) {
			if ((filesystem.getFat())[i] == 1) {
				if ((filesystem.getBlocks())[i].getName().equals(name)) {
					break;
				}
			}
		}
		return (filesystem.getBlocks())[i];
	}
}
