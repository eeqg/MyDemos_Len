package com.example.mydemos_len.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import com.example.mydemos_len.utils.HanziToPinyin.Token;

/**
 * Created by Administrator on 2017/6/1 0001.
 */

public class HnaziToPinyinUtil {
	
	//<i> 全拼:
	public String getFullPinYin(String source) {
		if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) {
			//如果没有"Locale.CHINA"这项的值就不能转拼音
			return source;
		}
		ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(source);
		if (tokens == null || tokens.size() == 0) {
			return source;
		}
		StringBuffer result = new StringBuffer();
		for (Token token : tokens) {
			if (token.type == Token.PINYIN) {
				result.append(token.target);
			} else {
				result.append(token.source);
			}
		}
		return result.toString();
	}
	
	//<ii>简拼:
	public String getFirstPinYin(String source) {
		if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) {
			return source;
		}
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(source);
		if (tokens == null || tokens.size() == 0) {
			return source;
		}
		StringBuffer result = new StringBuffer();
		for (Token token : tokens) {
			if (token.type == Token.PINYIN) {
				result.append(token.target.charAt(0));
			} else {
				result.append("#");
			}
		}
		return result.toString();
	}
}
