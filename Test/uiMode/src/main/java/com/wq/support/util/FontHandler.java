package com.wq.support.util;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.wq.base.BaseApplication;

public class FontHandler implements TagHandler {

	private int startIndex = 0;
	private int stopIndex = 0;

	@Override
	public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
		processAttributes(xmlReader);

		if (tag.equalsIgnoreCase("ddbfont")) {
			if (opening) {
				startFont(tag, output, xmlReader);
			} else {
				endFont(tag, output, xmlReader);
			}
		}

	}

	public void startFont(String tag, Editable output, XMLReader xmlReader) {
		startIndex = output.length();
	}

	public void endFont(String tag, Editable output, XMLReader xmlReader) {
		stopIndex = output.length();

		String color = attributes.get("color");
		String size = attributes.get("size");
		size = size.split("px")[0];
		if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
			output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			output.setSpan(new AbsoluteSizeSpan(UiUtils.dp2px(BaseApplication.getInstance(), Integer.parseInt(size))), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			output.setSpan(new ForegroundColorSpan(0xff2b2b2b), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}

	final HashMap<String, String> attributes = new HashMap<String, String>();

	private void processAttributes(final XMLReader xmlReader) {
		try {
			Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
			elementField.setAccessible(true);
			Object element = elementField.get(xmlReader);
			Field attsField = element.getClass().getDeclaredField("theAtts");
			attsField.setAccessible(true);
			Object atts = attsField.get(element);
			Field dataField = atts.getClass().getDeclaredField("data");
			dataField.setAccessible(true);
			String[] data = (String[]) dataField.get(atts);
			Field lengthField = atts.getClass().getDeclaredField("length");
			lengthField.setAccessible(true);
			int len = (Integer) lengthField.get(atts);

			/**
			 * MSH: Look for supported attributes and add to hash map. This is
			 * as tight as things can get :) The data index is "just" where the
			 * keys and values are stored.
			 */
			for (int i = 0; i < len; i++)
				attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
		} catch (Exception e) {}
	}

}