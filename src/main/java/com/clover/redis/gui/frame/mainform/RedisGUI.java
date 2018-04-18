package com.clover.redis.gui.frame.mainform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.clover.api.tools.common.utils.StringUtil;
import com.clover.redis.gui.client.RedisClient;
import com.clover.redis.gui.frame.ServerDialog;
import com.clover.redis.gui.model.Keys;
import com.clover.redis.gui.model.Vals;

/**
 * redis gui mainform
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月2日 下午8:24:04
 */
public class RedisGUI {
	private int keysRowsNum = -1;
	private int valsRowsNum = -1;
	private String oldValue = "";
	private String newValue = "";
	private JFrame frame;
	private JMenuBar menubar;
	private JMenu options;
	private JMenuItem open, add;
	private JButton addBtn = new JButton("新增");
	private JButton del = new JButton("删除");
	private JButton confirm = new JButton("确定");
	private JButton cancel = new JButton("取消");
	private JButton refresh = new JButton("刷新");
	private JButton addHead = new JButton("添加头部");
	private JButton addTail = new JButton("添加尾部");
	private JButton addAssignPoint = new JButton("添加指定位置");
	private JButton delHead = new JButton("删除头部");
	private JButton delTail = new JButton("删除尾部");
	private JButton delAssignPoint = new JButton("删除指定位置");
	
	private ServerDialog serverDialog;

	public RedisGUI() {
		initGUI();
	}

	private void initGUI() {
		frame = new JFrame();
		serverDialog = new ServerDialog(frame);

		// frame基本界面设置
		frame.setTitle("Redis GUI");
		frame.setResizable(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(900, 600));
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((size.width - 900) / 2, (size.height - 600) / 2);

		// 设置菜单
		add = new JMenuItem("add");
		open = new JMenuItem("open");
		options = new JMenu("options");
		options.add(add);
		options.add(open);
		menubar = new JMenuBar();
		menubar.add(options);
		frame.setJMenuBar(menubar);

		// 左侧redis server面板
		JPanel redisServerPannel = new JPanel();
		redisServerPannel.setBorder(new LineBorder(Color.WHITE, 5));
		redisServerPannel.setBackground(Color.WHITE);

		// JPanel增加鼠标右键菜单
		JPopupMenu popMenu = new JPopupMenu();
		JMenuItem addMenuItem = new JMenuItem("add");
		popMenu.add(addMenuItem);
		popMenu.add(new JMenuItem("del"));
		redisServerPannel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popMenu.show(redisServerPannel, e.getX(), e.getY());
				}
			}
		});
		redisServerPannel.add(popMenu);
		redisServerPannel.setPreferredSize(new Dimension(200, size.height));
		frame.getContentPane().add(redisServerPannel, BorderLayout.WEST);

		JPanel colPanel = new JPanel();
		frame.getContentPane().add(colPanel, BorderLayout.CENTER);
		colPanel.setLayout(new GridLayout(2, 1));

		// redis keys
		final JScrollPane keys = new JScrollPane();
		colPanel.add(keys);

		DefaultTableModel tableModel = new DefaultTableModel();
		JTable keysTable = new JTable(tableModel) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		keysTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		keysTable.setBorder(new LineBorder(SystemColor.BLACK, 1, true));
		keysTable.setForeground(Color.BLACK);
		keysTable.setBackground(SystemColor.control);
		keysTable.setCellSelectionEnabled(false);
		keysTable.setColumnSelectionAllowed(false);
		keysTable.setEnabled(false);
		
		// 显示db值的面板分成左右两部分：左值右操作
		JPanel valpanel = new JPanel();
		valpanel.setLayout(new BorderLayout(0, 0));
		colPanel.add(valpanel);
		
		// 操作
		JPanel optPanel = new JPanel();
		optPanel.setBackground(new Color(224, 255, 255));
		optPanel.setBorder(new LineBorder(Color.GRAY));
		optPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		addBtn.setVisible(false);
		optPanel.add(addBtn);
		
		del.setVisible(false);
		optPanel.add(del);
		
		confirm.setVisible(false);
		optPanel.add(confirm);
		
		cancel.setVisible(false);
		optPanel.add(cancel);
		
		addHead.setVisible(false);
		optPanel.add(addHead);
		
		addTail.setVisible(false);
		optPanel.add(addTail);
		
		addAssignPoint.setVisible(false);
		optPanel.add(addAssignPoint);
		
		delHead.setVisible(false);
		optPanel.add(delHead);
		
		delTail.setVisible(false);
		optPanel.add(delTail);
		
		delAssignPoint.setVisible(false);
		optPanel.add(delAssignPoint);
		
		optPanel.add(refresh);
		optPanel.setVisible(false);
		valpanel.add(optPanel, BorderLayout.SOUTH);
		
		// redis values
		final JScrollPane vals = new JScrollPane();
		valpanel.add(vals, BorderLayout.CENTER);
		
		// redis values table
		JTable valsTable = new JTable(tableModel) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				if(column == 0) {
					return false;
				}
				return true;
			}
		};
		
		valsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		valsTable.setBorder(new LineBorder(SystemColor.BLACK, 1, true));
		valsTable.setForeground(Color.BLACK);
		valsTable.setBackground(SystemColor.control);
		valsTable.setCellSelectionEnabled(false);
		valsTable.setColumnSelectionAllowed(false);

		// 新增事件
		addMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverDialog.setModal(true);
				serverDialog.setVisible(true);

				// 获取redis服务配置
				DefaultMutableTreeNode top = new DefaultMutableTreeNode(serverDialog.getServerName());
				JTree redisTree = new JTree(top);

				// 获取DB
				int port = Integer.parseInt(serverDialog.getServerPort());
				RedisClient redis = RedisClient.getInstance(serverDialog.getServerHost(), port, serverDialog.getServerPassword());
				int dbNums = redis.getRedisDB();
				DefaultTreeModel model = (DefaultTreeModel) redisTree.getModel();

				for (int i = 0; i < dbNums; i++) {
					DefaultMutableTreeNode redisDbItem = new DefaultMutableTreeNode("db" + i);
					model.insertNodeInto(redisDbItem, top, i);
				}

				// 添加到面板
				redisServerPannel.add(redisTree);
				redisTree.updateUI();
				redisTree.expandRow(0);

				redisTree.addTreeSelectionListener(new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) redisTree.getLastSelectedPathComponent();
						if(!node.isLeaf() || node.toString().equals(serverDialog.getServerName())) {
							return;
						}
						String selectedDB = node.toString();
						List<Keys> keyList = redis.getKeys(Integer.parseInt(selectedDB.substring(2)));
						Object[][] cols = new Object[keyList.size()][3];
						for (int i = 0; i < keyList.size(); i++) {
							cols[i][0] = keyList.get(i).getKey();
							cols[i][1] = keyList.get(i).getType();
							cols[i][2] = keyList.get(i).getSize();
						}
						keysTable.setModel(new DefaultTableModel(cols, new String[] { "KEY", "TYPE", "SIZE" }) {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;
							@SuppressWarnings("rawtypes")
							Class[] columnTypes = new Class[] { Object.class, Object.class, Object.class, Object.class, Object.class };

							@SuppressWarnings({ "unchecked", "rawtypes" })
							public Class getColumnClass(int columnIndex) {
								return columnTypes[columnIndex];
							}
						});
						
						keysTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
							private static final long serialVersionUID = 1L;

							@Override
							public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
								if (row == RedisGUI.this.keysRowsNum) {
									setBackground(new Color(240, 255, 240));
								} else {
									setBackground(null);
								}
								
								return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
							}
						});
						
						keys.remove(keysTable);
						keys.setViewportView(keysTable);
						
						keysTable.addMouseMotionListener(new MouseMotionListener() {
							@Override
							public void mouseMoved(MouseEvent e) {
								RedisGUI.this.keysRowsNum = keysTable.rowAtPoint(e.getPoint());
								keysTable.updateUI();
							}

							@Override
							public void mouseDragged(MouseEvent paramMouseEvent) {
							}
						});
						
						keysTable.addMouseListener(new MouseListener() {
							@Override
							public void mouseReleased(MouseEvent paramMouseEvent) {
							}

							@Override
							public void mousePressed(MouseEvent paramMouseEvent) {
							}

							@Override
							public void mouseExited(MouseEvent paramMouseEvent) {
								RedisGUI.this.keysRowsNum = -1;
								keysTable.updateUI();
							}

							@Override
							public void mouseEntered(MouseEvent paramMouseEvent) {
							}

							@Override
							public void mouseClicked(MouseEvent e) {
								// 双击
								if (e.getClickCount() == 2) {
									optPanel.setVisible(true);
									Keys queryVals = new Keys();
									String type = keysTable.getValueAt(keysTable.rowAtPoint(new Point(e.getX(), e.getY())), 1).toString();
									queryVals.setKey(keysTable.getValueAt(keysTable.rowAtPoint(new Point(e.getX(), e.getY())), 0).toString());
									queryVals.setType(type);
									queryVals.setSize(keysTable.getValueAt(keysTable.rowAtPoint(new Point(e.getX(), e.getY())), 2).toString());
									List<Vals> valsList = redis.queryVals(queryVals, Integer.parseInt(selectedDB.substring(2)));
									Object[][] valCols = new Object[valsList.size()][2];
									for (int i = 0; i < valsList.size(); i++) {
										valCols[i][0] = valsList.get(i).getColumn();
										valCols[i][1] = valsList.get(i).getValue();
									}
									
									valsTable.setModel(new DefaultTableModel(valCols, new String[] { "COLUMN", "VALUE" }) {
										/**
										 * 
										 */
										private static final long serialVersionUID = 1L;
										@SuppressWarnings("rawtypes")
										Class[] columnTypes = new Class[] { Object.class, Object.class };

										@SuppressWarnings({ "unchecked", "rawtypes" })
										public Class getColumnClass(int columnIndex) {
											return columnTypes[columnIndex];
										}
									});
									
									valsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
										private static final long serialVersionUID = 1L;

										@Override
										public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
											if (row == RedisGUI.this.valsRowsNum) {
												setBackground(new Color(240, 255, 240));
											} else {
												setBackground(null);
											}
											
											return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
										}
									});
									
									
									// 鼠标事件
									valsTable.addMouseListener(new MouseListener() {
										
										@Override
										public void mouseReleased(MouseEvent e) {}
										
										@Override
										public void mousePressed(MouseEvent e) {}
										
										@Override
										public void mouseExited(MouseEvent e) {}
										
										@Override
										public void mouseEntered(MouseEvent e) {}
										
										@Override
										public void mouseClicked(MouseEvent e) {
											RedisGUI.this.oldValue = valsTable.getValueAt(valsTable.getSelectedRow(), valsTable.getSelectedColumn()).toString();
										}
									});
									
									// 数值改变事件，只有失去焦点才会触发
									valsTable.getModel().addTableModelListener(new TableModelListener() {
										
										@Override
										public void tableChanged(TableModelEvent e) {
											RedisGUI.this.newValue = valsTable.getValueAt(e.getLastRow(), e.getColumn()).toString();
										}
									});
									
									setButtonEnableByType(type);
									vals.remove(valsTable);
									vals.setViewportView(valsTable);
								}
							}
						});
					}
				});
			}
		});
	}

	/**
	 * 根据redis数据库类型设置按钮是否可用
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月17日 下午2:28:35
	 * @param type
	 */
	private void setButtonEnableByType(String type) {
		if (StringUtil.isEmpty(type)) {
			return;
		}

		switch (type) {
		case "string":
			addHead.setVisible(false);
			addTail.setVisible(false);
			addAssignPoint.setVisible(false);
			delHead.setVisible(false);
			delTail.setVisible(false);
			delAssignPoint.setVisible(false);
			addBtn.setVisible(false);
			del.setVisible(false);
			confirm.setVisible(true);
			cancel.setVisible(true);
			refresh.setVisible(false);
			break;
			
		case "list":
			addBtn.setVisible(false);
			del.setVisible(false);
			confirm.setVisible(true);
			cancel.setVisible(true);
			refresh.setVisible(false);
			addHead.setVisible(true);
			addTail.setVisible(true);
			addAssignPoint.setVisible(true);
			delHead.setVisible(true);
			delTail.setVisible(true);
			delAssignPoint.setVisible(true);
			break;
			
		case "set":
			addHead.setVisible(false);
			addTail.setVisible(false);
			addAssignPoint.setVisible(false);
			delHead.setVisible(false);
			delTail.setVisible(false);
			delAssignPoint.setVisible(false);
			addBtn.setVisible(true);
			del.setVisible(true);
			confirm.setVisible(false);
			cancel.setVisible(false);
			refresh.setVisible(true);
			break;
			
		case "hash":
			addHead.setVisible(false);
			addTail.setVisible(false);
			addAssignPoint.setVisible(false);
			delHead.setVisible(false);
			delTail.setVisible(false);
			delAssignPoint.setVisible(false);
			addBtn.setVisible(true);
			del.setVisible(true);
			confirm.setVisible(true);
			cancel.setVisible(true);
			refresh.setVisible(true);
			break;
			
		default:
			break;
		}
	}
	
	public static void main(String[] args) {
		new RedisGUI().frame.setVisible(true);
	}
}
