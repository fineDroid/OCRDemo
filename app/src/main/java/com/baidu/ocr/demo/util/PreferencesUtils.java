package com.baidu.ocr.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author by kaka
 *         created at 16/6/12 16:06
 *         Preferences工具类
 */
public class PreferencesUtils {

	private PreferencesUtils() {
		throw new AssertionError();
	}

	/**
	 * put string preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
	 * name that is not a string
	 * @see #getString(Context, String, String)
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, null);
	}

	/**
	 * get string preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a string
	 */
	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	/**
	 * put int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putInt(Context context, String key, int value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * get int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a int
	 * @see #getInt(Context, String, int)
	 */
	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}

	/**
	 * get int preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a int
	 */
	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * put long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putLong(Context context, String key, long value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getLong(Context, String, long)
	 */
	public static long getLong(Context context, String key) {
		return getLong(context, key, -1);
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * put float preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putFloat(Context context, String key, float value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * get float preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a float
	 * @see #getFloat(Context, String, float)
	 */
	public static float getFloat(Context context, String key) {
		return getFloat(context, key, -1);
	}

	/**
	 * get float preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a float
	 */
	public static float getFloat(Context context, String key, float defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * put boolean preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences, default is false
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
	 * name that is not a boolean
	 * @see #getBoolean(Context, String, boolean)
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	/**
	 * get boolean preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a boolean
	 */
	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	public static boolean remove(Context context, String key) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.edit().remove(key).commit();
	}

	/**
	 * 针对复杂类型存储<对象>
	 *
	 * @param key
	 * @param object
	 */
	public static void setObject(Context context, String key, Object object) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

		//创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//创建字节对象输出流
		ObjectOutputStream out = null;
		try {
			//然后通过将字对象进行64转码，写入key值为key的sp中
			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(key, objectVal);
			editor.commit();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		if (sp.contains(key)) {
			String objectVal = sp.getString(key, null);
			byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
			//一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bais);
				T t = (T) ois.readObject();
				return t;
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bais != null) {
						bais.close();
					}
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
