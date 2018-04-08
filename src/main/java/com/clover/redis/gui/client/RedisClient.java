package com.clover.redis.gui.client;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * redis服务
 * 
 * @author zhangdq
 * @Email qiang900714@126.com
 * @time 2018年4月4日 下午2:53:55
 */
public class RedisClient {
	private String host;
	private int port;
	private String password;

	private static RedisClient instance;

	public RedisClient() {
	}

	public RedisClient(String host, int port, String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}

	public static RedisClient getInstance(String host, int port, String password) {
		if (instance == null) {
			instance = new RedisClient(host, port, password);
		}
		return instance;
	}

	/**
	 * ping redis 服务
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月8日 下午1:17:38
	 * @return
	 */
	public String ping() {
		String ping = "";
		try {
			Jedis jedis = new Jedis(this.host, this.port);
			jedis.auth(this.password);
			ping = jedis.ping();
			jedis.close();
		} catch (Exception e) {
			ping = e.toString();
		}
		return ping;
	}

	/**
	 * 获取redis DB数量
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月8日 下午1:23:47
	 * @return
	 */
	public int getRedisDB() {
		List<String> dbList = new ArrayList<>();
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		dbList = jedis.configGet("databases");
		jedis.close();
		return Integer.parseInt(dbList.get(1));
	}
}
