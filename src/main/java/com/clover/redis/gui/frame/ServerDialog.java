package com.clover.redis.gui.frame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.clover.api.tools.common.utils.StringUtil;
import com.clover.redis.gui.client.RedisClient;

/**
 * 增加redis server弹出框
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月4日 下午12:30:01
 */
public class ServerDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// redis属性
	private String serverName;
	private String serverHost;
	private String serverPort;
	private String serverPassword;

	private JTextField name;
	private JTextField host;
	private JTextField port;
	private JTextField password;
	private JButton confirm;
	private JButton cancel;

	public ServerDialog() {
	}

	public ServerDialog(JFrame jFrame) {
		super(jFrame);
		setLayout(new GridLayout(5, 2));
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - 500) / 2, (size.height - 200) / 2, 500, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// 服务名称
		add(new JLabel("name"));
		name = new JTextField(64);
		name.setText("127.0.0.1:16379");
		add(name);

		// host
		add(new JLabel("host"));
		host = new JTextField(32);
		host.setText("127.0.0.1");
		add(host);

		// port
		add(new JLabel("port"));
		port = new JTextField(12);
		port.setText("16379");
		add(port);

		// password
		add(new JLabel("password"));
		password = new JTextField(64);
		password.setText("zhangdq");
		add(password);

		// 按钮
		cancel = new JButton("取消");
		confirm = new JButton("确定");
		cancel.addActionListener(this);
		confirm.addActionListener(this);
		add(cancel);
		add(confirm);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String button = e.getActionCommand();
		if (StringUtil.isNotEmpty(button) && "确定".equals(button)) {
			this.serverName = name.getText();
			if (StringUtil.isEmpty(this.serverName)) {
				JOptionPane.showMessageDialog(this, "请输入服务名称", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}

			this.serverHost = host.getText();
			if (StringUtil.isEmpty(this.serverHost)) {
				JOptionPane.showMessageDialog(this, "请输入redis地址", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}

			this.serverPort = port.getText();
			if (StringUtil.isEmpty(this.serverPort)) {
				JOptionPane.showMessageDialog(this, "请输入redis端口", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}

			this.serverPassword = password.getText();
			if (StringUtil.isEmpty(this.serverPassword)) {
				JOptionPane.showMessageDialog(this, "请输入redis密码", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}
			setVisible(true);
			int port = Integer.parseInt(this.serverPort);
			RedisClient redis = new RedisClient(this.serverHost, port, this.serverPassword);
			String ping = redis.ping();
			if (StringUtil.isNotEmpty(ping) && "PONG".equals(ping)) {
				setVisible(false);
				return;
			}
			JOptionPane.showMessageDialog(this, ping, "提示", JOptionPane.WARNING_MESSAGE);
		} else {
			setVisible(false);
		}
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerHost() {
		return serverHost;
	}

	public String getServerPort() {
		return serverPort;
	}

	public String getServerPassword() {
		return serverPassword;
	}
}
