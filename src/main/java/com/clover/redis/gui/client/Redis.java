package com.clover.redis.gui.client;

import redis.clients.jedis.Jedis;

/**
 * redis服务
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月4日 下午2:53:55
 */
public class Redis {
	private String host;
	private String port;
	private String password;

	public Redis() {
	}

	public Redis(String host, String port, String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}

	public String ping() {
		String ping = "";
		try {
			Jedis jedis = new Jedis("127.0.0.1", 16379);
			jedis.auth("zhangdq");
			ping = jedis.ping();
			jedis.close();
		} catch (Exception e) {
			ping = e.toString();
		}
		return ping;
	}
}
