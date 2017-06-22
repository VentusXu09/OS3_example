package stw.os.assignment.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * Created on 2004-4-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

//模拟100个block的情况

public class FileSystem implements Serializable {
	private Map dat; //根分配表
	private Map now_dat; //当前目录分配表
	private byte[] fat; //磁盘分配表 0表示空闲，1表示占用
	private Block[] blocks;
	private String now_dir;
	private int now_dir_index;
	private int up_dir_index;
	private ArrayList path;

	public FileSystem() {
		blocks = new Block[100];
		dat = new HashMap();

		fat = new byte[100];
		for (int i = 0; i < 100; ++i) {
			fat[i] = 0;
		}
		path = new ArrayList();
		path.add("root");
		now_dir = "root";
		now_dir_index = 0;
		up_dir_index = -1;
		blocks[0] = new Block("root", '0');
		blocks[0].setCreation_date(new Date());
		blocks[0].setModification_date(new Date());
		blocks[0].setDir_index(-1);
		fat[0] = 1;
		dat = blocks[0].getDat();
		now_dat = dat;
	}

	public void format() {
		for (int i = 0; i < 100; ++i) {
			fat[i] = 0;
		}
		path = new ArrayList();
		path.add("root");
		now_dir = "root";
		now_dir_index = 0;
		up_dir_index = -1;
		blocks[0] = new Block("root", '0');
		blocks[0].setCreation_date(new Date());
		blocks[0].setModification_date(new Date());
		blocks[0].setDir_index(-1);
		fat[0] = 1;
		dat = blocks[0].getDat();
		now_dat = dat;
	}

	public boolean addNewDir(String name) {
		int i = 0;
		for (; i < 100; ++i) {
			if (fat[i] == 0) {
				break;
			}
		}
		if (i == 100) {
			return false;
		}
		blocks[i] = new Block("d" + name, '0');
		blocks[i].setCreation_date(new Date());
		blocks[i].setModification_date(new Date());
		blocks[i].setDir_index(this.now_dir_index);
		fat[i] = 1;
		now_dat.put("d" + name, new Integer(i));
		return true;
	}

	public boolean addNewFile(String name) {
		int i = 0;
		for (; i < 100; ++i) {
			if (fat[i] == 0) {
				break;
			}
		}
		if (i == 100) {
			return false;
		}
		blocks[i] = new Block("f" + name, '1');
		blocks[i].setCreation_date(new Date());
		blocks[i].setModification_date(new Date());
		blocks[i].setDir_index(this.now_dir_index);
		blocks[i].setData("new");
		fat[i] = 1;
		now_dat.put("f" + name, new Integer(i));
		return true;
	}

	public void delFile(String name) {
		int index = ((Integer) now_dat.get("f" + name)).intValue();
		now_dat.remove("f" + name);
		fat[index] = 0;
		/*		if (index == -1) {
					now_dat.remove("f" + name);
				} else {
					while (blocks[index].getNext_node() != -1) {
						fat[index] = 0;
						index = blocks[index].getNext_node();
					}
				}*/
	}

	public void delDir(int index) {
		Block block = blocks[index];
		Set set = block.getDat().keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
//			System.out.println(it.next());
//			int i = ((Integer) it.next()).intValue();
			int i=((Integer) block.getDat().get((String)it.next())).intValue();
			if (blocks[i].getType() == '0') {
				delDir(i);
			} else {
				fat[i] = 0;
				//				delFile
			}
		}
	}

	public void delDir(String name) {
		int index = ((Integer) now_dat.get("d" + name)).intValue();
		delDir(index);
		now_dat.remove("d" + name);
		fat[index] = 0;
	}

	public void intoDir(String name) {
		int index = ((Integer) now_dat.get("d" + name)).intValue();
		this.up_dir_index = this.now_dir_index;
		this.now_dir_index = index;
		Block block = blocks[index];
		this.now_dat = block.getDat();
		this.now_dir = "d" + name;
	}

	public void upDir() {
		if (this.up_dir_index != -1) {
			Block block = blocks[up_dir_index];
			this.now_dir_index = up_dir_index;
			this.up_dir_index = block.getDir_index();
			this.now_dat = block.getDat();
			this.now_dir = block.getName();
		}
	}

	public boolean saveFile(String filename, String text) {
		int index = ((Integer) now_dat.get("f" + filename)).intValue();
		Block block = blocks[index];
		int length = text.length();
		block.setNow_size(length);
		int numofblock = length / Block.SIZE;
		if (length % Block.SIZE != 0) {
			++numofblock;
		}
		if (numofblock > 1) {
			block.setData(text.substring(0, Block.SIZE));
			for (int i = 0; i < numofblock; ++i) {
				int j = 0;
				for (; j < 100; ++j) {
					if (fat[j] == 0) {
						break;
					}
				}
				if (j == 100) {
					return false;
				}
				block.setNext_node(j);
				block = blocks[j];
				blocks[j] = new Block(filename + "node", '1');
				blocks[j].setCreation_date(new Date());
				blocks[j].setModification_date(new Date());
				blocks[j].setDir_index(this.now_dir_index);
				if (i != numofblock - 1) {
					blocks[j].setData(
						text.substring(
							Block.SIZE * (i + 1),
							Block.SIZE * (i + 2)));
				} else if (i == numofblock - 1) {
					blocks[j].setData(text.substring(Block.SIZE * (i + 1)));
					block.setNext_node(-1);
				}

				fat[j] = 1;
			}

		} else if (numofblock == 1) {
			block.setData(text);
		}

		return true;
	}

	/**
	 * @return
	 */
	public Block[] getBlocks() {
		return blocks;
	}

	/**
	 * @return
	 */
	public byte[] getFat() {
		return fat;
	}

	/**
	 * @param blocks
	 */
	public void setBlocks(Block[] blocks) {
		this.blocks = blocks;
	}

	/**
	 * @param bs
	 */
	public void setFat(byte[] bs) {
		fat = bs;
	}

	/**
	 * @return
	 */
	public String getNow_dir() {
		return now_dir;
	}

	/**
	 * @param string
	 */
	public void setNow_dir(String string) {
		now_dir = string;
	}

	public void savesystem() {

	}

	public void loadsystem() {

	}

	/**
	 * @return
	 */
	public ArrayList getPath() {
		return path;
	}

	/**
	 * @param list
	 */
	public void addPath(String pname) {
		path.add(pname);
	}
	public void delPath() {
		int n = path.size();
		if (n > 1) {
			path.remove(n - 1);
		}
	}

	/**
	 * @return
	 */
	public int getNow_dir_index() {
		return now_dir_index;
	}

	/**
	 * @param i
	 */
	public void setNow_dir_index(int i) {
		now_dir_index = i;
	}

}

class Block implements Serializable {
	private int dir_index; //所在目录的索引
	private char type; //0 表示目录；1 表示文件
	private int previous_node; //指向上个块的指针
	private int next_node; //指向下个块的指针
	public final static int SIZE = 512; //块的大小
	private int now_size;
	private Map dat; //目录分配表
	private String name;
	private String data;
	private Date creation_date;
	private Date modification_date;

	public Block() {
		type = '0';
		previous_node = 0;
		next_node = 0;
		now_size = 0;
		dat = new HashMap();
	}

	public Block(String name, char type) {
		this.type = type;
		this.name = name;
		previous_node = 0;
		next_node = 0;
		now_size = 0;
		dat = new HashMap();
	}

	public Map getDat() {
		return dat;
	}

	public int getNext_node() {
		return next_node;
	}

	public int getNow_size() {
		return now_size;
	}

	public int getPrevious_node() {
		return previous_node;
	}

	public char getType() {
		return type;
	}

	public void setDat(Map map) {
		dat = map;
	}

	public void setNext_node(int i) {
		next_node = i;
	}

	public void setNow_size(int i) {
		now_size = i;
	}

	public void setPrevious_node(int i) {
		previous_node = i;
	}

	public void setType(char c) {
		type = c;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @return
	 */
	public Date getCreation_date() {
		return creation_date;
	}

	/**
	 * @return
	 */
	public Date getModification_date() {
		return modification_date;
	}

	/**
	 * @param date
	 */
	public void setCreation_date(Date date) {
		creation_date = date;
	}

	/**
	 * @param date
	 */
	public void setModification_date(Date date) {
		modification_date = date;
	}

	/**
	 * @return
	 */
	public int getDir_index() {
		return dir_index;
	}

	/**
	 * @param i
	 */
	public void setDir_index(int i) {
		dir_index = i;
	}

	/**
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param string
	 */
	public void setData(String string) {
		data = string;
	}

}
