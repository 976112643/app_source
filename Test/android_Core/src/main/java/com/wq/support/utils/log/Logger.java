package com.wq.support.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logger is a wrapper of {@link android.util.Log} But more pretty, simple and
 * powerful
 */
public final class Logger {
	public static final int DEBUG = 3;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;
	public static final int INFO = 4;
	public static final int VERBOSE = 2;
	public static final int WARN = 5;

	private static final String DEFAULT_TAG = "PRETTYLOGGER";

	private static Printer printer = new LoggerPrinter();

	// no instance
	private Logger() {}

	/**
	 * It is used to get the settings object in order to change settings
	 * 
	 * @return the settings object
	 */
	public static Settings init() {
		return init(DEFAULT_TAG);
	}

	/**
	 * It is used to change the tag
	 * 
	 * @param tag
	 *            is the given string which will be used in Logger as TAG
	 */
	public static Settings init(String tag) {
		printer = new LoggerPrinter();
		return printer.init(tag);
	}

	public static void resetSettings() {
		printer.resetSettings();
	}

	public static Printer T(String tag) {
		return printer.t(tag, printer.getSettings().getMethodCount());
	}
	public static Printer T(Object tag) {
		return printer.t(tag.getClass().getSimpleName(), printer.getSettings().getMethodCount());
	}
	public static Printer T(int methodCount) {
		return printer.t(null, methodCount);
	}

	public static Printer T(String tag, int methodCount) {
		return printer.t(tag, methodCount);
	}

	public static void log(int priority, String tag, String message,
			Throwable throwable) {
		printer.log(priority, tag, message, throwable);
	}

	public static void D(String message, Object... args) {
		printer.d(message, args);
	}

	public static void D(Object object) {
		printer.d(object);
	}

	public static void E(String message, Object... args) {
		printer.e(null, message, args);
	}

	public static void E(Throwable throwable) {
		printer.e(throwable, "");
	}

	public static void E(Throwable throwable, String message, Object... args) {
		printer.e(throwable, message, args);
	}

	public static void I(String message, Object... args) {
		printer.i(message, args);
	}

	public static void V(String message, Object... args) {
		printer.v(message, args);
	}

	public static void W(String message, Object... args) {
		printer.w(message, args);
	}

	public static void WTF(String message, Object... args) {
		printer.wtf(message, args);
	}

	/**
	 * Formats the json content and print it
	 * 
	 * @param json
	 *            the json content
	 */
	public static void JSON(String json) {
		printer.json(json);
	}

	public static String formatJson(String json) {
		if (Helper.isEmpty(json)) { return "Empty/Null json content"; }
		try {
			json = json.trim();
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				String message = jsonObject.toString(2);
				return message;
			}
			if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				String message = jsonArray.toString(2);
				return message;
			}
			return "Invalid Json";
		} catch (JSONException e) {
			return ("Invalid Json");
		}
	}

	/**
	 * Formats the json content and print it
	 * 
	 * @param xml
	 *            the xml content
	 */
	public static void XML(String xml) {
		printer.xml(xml);
	}

}
