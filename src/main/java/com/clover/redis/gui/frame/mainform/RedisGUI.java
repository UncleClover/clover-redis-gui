package com.clover.redis.gui.frame.mainform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.clover.redis.gui.frame.ServerDialog;

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

		// 新增事件
		addMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverDialog.setModal(true);
				serverDialog.setVisible(true);

				// 获取redis值
				DefaultMutableTreeNode top = new DefaultMutableTreeNode("127.0.0.1:16379");
				top.add(new DefaultMutableTreeNode("12111"));
				JTree redisTree = new JTree(top);
				redisServerPannel.add(redisTree);
			}
		});
		JPanel colPanel = new JPanel();
		frame.getContentPane().add(colPanel, BorderLayout.CENTER);
		colPanel.setLayout(new BorderLayout(0, 0));

		final JScrollPane colScrollPanel = new JScrollPane();
		colPanel.add(colScrollPanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new RedisGUI().frame.setVisible(true);
	}
}