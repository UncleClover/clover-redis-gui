package com.clover.redis.gui.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.clover.redis.gui.model.Keys;
import com.clover.redis.gui.model.Vals;

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

	/**
	 * 获取指定数据库key信息
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月8日 下午4:31:59
	 * @return
	 */
	public List<Keys> getKeys(int selectedDB) {
		List<Keys> keyList = new ArrayList<>();
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		Set<String> keys = jedis.keys("*");
		for (String key : keys) {
			Keys keyItem = new Keys();
			keyItem.setKey(key);
			keyItem.setSize("1");
			String type = jedis.type(key);
			keyItem.setType(type);
			if (type.equals("hash")) {
				keyItem.setSize(String.valueOf(jedis.hlen(key)));
			}
			if (type.equals("set")) {
				keyItem.setSize(String.valueOf(jedis.scard(key)));
			}
			if (type.equals("list")) {
				keyItem.setSize(String.valueOf(jedis.llen(key)));
			}
			keyList.add(keyItem);
		}
		jedis.close();
		return keyList;
	}

	public List<Vals> queryVals(Keys keys, int selectedDB) {
		List<Vals> vals = new ArrayList<>();
		String type = keys.getType();
		if (type.equals("string")) {
			Vals val = new Vals();
			val.setColumn(keys.getKey());
			val.setValue(get(keys.getKey(), selectedDB));
			vals.add(val);
		} else if (type.equals("list")) {
			List<String> lvalList = lrange(keys.getKey(), Long.valueOf(keys.getSize()), selectedDB);
			for (int i = 0; i < lvalList.size(); i++) {
				Vals val = new Vals();
				val.setColumn(String.valueOf(i + 1));
				val.setValue(lvalList.get(i));
				vals.add(val);
			}
		} else if (type.equals("set")) {
			Set<String> svalList = smembers(keys.getKey(), selectedDB);
			int i = 1;
			for (String v : svalList) {
				Vals val = new Vals();
				val.setColumn(String.valueOf(i++));
				val.setValue(v);
				vals.add(val);
			}
		} else if (type.equals("hash")) {
			Map<String, String> map = hgetAll(keys.getKey(), selectedDB);
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Vals val = new Vals();
				Entry<String, String> entry = it.next();
				val.setColumn(entry.getKey());
				val.setValue(entry.getValue());
				vals.add(val);
			}
		}
		return vals;
	}

	/**
	 * 
	 * 获取string类型数据
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月13日 下午4:55:51
	 * @param key
	 * @param selectedDB
	 * @return
	 */
	public String get(String key, int selectedDB) {
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		String val = jedis.get(key);
		jedis.close();
		return val;
	}

	/**
	 * 获取hash类型数据
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月13日 下午4:58:08
	 * @param key
	 * @param selectedDB
	 */
	public Map<String, String> hgetAll(String key, int selectedDB) {
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		Map<String, String> val = jedis.hgetAll(key);
		jedis.close();
		return val;
	}

	/**
	 * 查询当前key值下list类型所有数据
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月13日 下午5:07:17
	 * @param key
	 * @param length
	 * @param selectedDB
	 * @return
	 */
	public List<String> lrangeAll(String key, int selectedDB) {
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		List<String> val = lrange(key, jedis.hlen(key), selectedDB);
		jedis.close();
		return val;
	}

	/**
	 * 查询list类型数据
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月13日 下午5:02:35
	 * @param key
	 * @param length
	 * @param selectedDB
	 * @return
	 */
	public List<String> lrange(String key, Long length, int selectedDB) {
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		List<String> val = jedis.lrange(key, 0, length);
		jedis.close();
		return val;
	}

	/**
	 * 查询set类型数据
	 * 
	 * @author zhangdq
	 * @Email qiang900714@126.com
	 * @time 2018年4月13日 下午5:06:53
	 * @param key
	 * @param selectedDB
	 * @return
	 */
	public Set<String> smembers(String key, int selectedDB) {
		Jedis jedis = new Jedis(this.host, this.port);
		jedis.auth(this.password);
		jedis.select(selectedDB);
		Set<String> val = jedis.smembers(key);
		jedis.close();
		return val;
	}
}
