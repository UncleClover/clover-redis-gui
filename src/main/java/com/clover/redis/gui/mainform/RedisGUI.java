package com.clover.redis.gui.mainform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/**
 * redis gui mainform
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月2日 下午8:24:04
 */
public class RedisGUI {
	private JFrame frame;
	private JTable table;
	private JTextField funcNoTxt;
	private JTextField interTxt;
	private JTextField fileUrlTxt;
	private JMenuBar menubar;
	private JMenu options;
	private JMenuItem open, add;

	public RedisGUI() {
		initGUI();
	}

	private void initGUI() {
		frame = new JFrame();

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
		colPanel.setLayout(new BorderLayout(0, 0));

		final JScrollPane colScrollPanel = new JScrollPane();
		colPanel.add(colScrollPanel, BorderLayout.CENTER);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBorder(new LineBorder(SystemColor.BLACK, 1, true));
		table.setForeground(Color.BLACK);
		table.setBackground(SystemColor.control);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.textInactiveText));
		colPanel.add(panel, BorderLayout.SOUTH);

		JLabel funcNoLabel = new JLabel("jlabel");
		funcNoTxt = new JTextField();
		funcNoTxt.setText("10002");
		funcNoTxt.setColumns(8);
		panel.add(funcNoLabel);
		panel.add(funcNoTxt);

		JLabel interLabel = new JLabel("�ӿڣ�");
		interTxt = new JTextField();
		interTxt.setText("InterService");
		interTxt.setColumns(8);
		panel.add(interLabel);
		panel.add(interTxt);

		// ���ɴ����Ŀ¼
		JLabel fileUrlLabel = new JLabel("����Ŀ¼��");
		fileUrlTxt = new JTextField();
		fileUrlTxt.setText("E:\\setting\\code\\");
		fileUrlTxt.setColumns(20);
		panel.add(fileUrlLabel);
		panel.add(fileUrlTxt);

		// ������ť
		JButton addBtn = new JButton("���ɲ������");
		JButton queryBtn = new JButton("���ɲ�ѯ����");
		JButton delBtn = new JButton("����ɾ������");
		JButton updateBtn = new JButton("���ɸ��´���");
		JButton checkAllBtn = new JButton("ȫѡ");
		JButton uncheckAllBtn = new JButton("��ѡ");

		// ��ӵ��������
		JPanel optPanel = new JPanel();
		optPanel.setBackground(new Color(224, 255, 255));
		optPanel.setBorder(new LineBorder(Color.GRAY));
		optPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 8));
		optPanel.add(addBtn);
		optPanel.add(queryBtn);
		optPanel.add(delBtn);
		optPanel.add(updateBtn);
		optPanel.add(checkAllBtn);
		optPanel.add(uncheckAllBtn);
		frame.getContentPane().add(optPanel, BorderLayout.SOUTH);

		// ���밴ťע���¼�
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getRowCount();
				if (row == 0) {
					return;
				}
				String funcNo = funcNoTxt.getText();
				if (funcNo == null || funcNo.equals("")) {
					JOptionPane.showMessageDialog(frame, "���ܺŲ���Ϊ�գ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String inter = interTxt.getText();
				if (inter == null || inter.equals("")) {
					JOptionPane.showMessageDialog(frame, "�ӿ������ֲ���Ϊ�գ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String fileUrl = fileUrlTxt.getText();
				if (fileUrl == null || fileUrl.equals("")) {
					JOptionPane.showMessageDialog(frame, "�ļ�Ŀ¼����Ϊ�գ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
					return;
				}
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				List<Map<String, Object>> tList = new ArrayList<Map<String, Object>>();
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("funcNo", funcNo);
				tempMap.put("inter", inter);
				tempMap.put("fileurl", fileUrl);
				tList.add(tempMap);
				for (int i = 0; i < row; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", model.getValueAt(i, 0));
					map.put("column", model.getValueAt(i, 1));
					tList.add(map);
				}
				// ���ɴ���
			}
		});

		// ȫѡע���¼�
		checkAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getRowCount();
				if (row == 0) {
					return;
				}
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				for (int i = 0; i < row; i++) {
					model.setValueAt(true, i, 0);
				}
			}
		});
		// ��ѡע���¼�
		uncheckAllBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getRowCount();
				if (row == 0) {
					return;
				}
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				for (int i = 0; i < row; i++) {
					Object nullable = model.getValueAt(i, 4);
					model.setValueAt(nullable.equals("N"), i, 0);
				}
			}
		});
	}

	public static void main(String[] args) {
		new RedisGUI().frame.setVisible(true);
	}
}
