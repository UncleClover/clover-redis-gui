package com.clover.redis.gui.frame.mainform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.clover.redis.gui.client.RedisClient;
import com.clover.redis.gui.frame.ServerDialog;
import com.clover.redis.gui.model.Keys;

/**
 * redis gui mainform
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月2日 下午8:24:04
 */
public class RedisGUI {
	private JFrame frame;
	private JMenuBar menubar;
	private JMenu options;
	private JMenuItem open, add;

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
			}// 表格不允许被编辑
		};
		keysTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		keysTable.setBorder(new LineBorder(SystemColor.BLACK, 1, true));
		keysTable.setForeground(Color.BLACK);
		keysTable.setBackground(SystemColor.control);
		keysTable.setCellSelectionEnabled(false);
		keysTable.setColumnSelectionAllowed(false);

		final JScrollPane vals = new JScrollPane();
		colPanel.add(vals);

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
						List<Keys> keyList = redis.getKeys();
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
						keys.setViewportView(keysTable);
					}
				});
			}
		});
	}

	public static void main(String[] args) {
		new RedisGUI().frame.setVisible(true);
	}
}
