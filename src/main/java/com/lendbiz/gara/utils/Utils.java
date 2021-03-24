package com.lendbiz.gara.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.tomcat.util.codec.binary.Base64;

import com.lendbiz.gara.model.exception.BusinessException;

public class Utils {
	public static final String SUB_ID = "lendbiz_";

	public static String generateId(int numberOfCharactor) {
		String alpha = "abcdefghijklmnopqrstuvwxyz0123456789"; // a-z

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numberOfCharactor; i++) {
			int number = randomNumber(0, alpha.length() - 1);
			char ch = alpha.charAt(number);
			sb.append(ch);
		}
		return SUB_ID + sb.toString();
	}

	public static int randomNumber(int min, int max) {
		Random generator = new Random();
		return generator.nextInt((max - min) + 1) + min;
	}

	public static boolean isCompareDate(Date refIdDate, String issueDate) {
		try {
			Date issue = new SimpleDateFormat("dd/MM/yyyy").parse(issueDate);
			Date refDate = new SimpleDateFormat("yyyy-MM-dd").parse(refIdDate.toString());

			if (refDate.compareTo(issue) == 0) {
				return true;
			}

			return false;
		} catch (ParseException e) {
			throw new BusinessException("07", "Can not compare date with date of identity");
		}
	}

	public static String decodeCif(String cif) {
		byte[] valueDecoded = Base64.decodeBase64(cif);
		return new String(valueDecoded);
	}

	public static String encodeCif(String cif) {
		byte[] valueEncoded = Base64.encodeBase64(cif.getBytes());
		return new String(valueEncoded);
	}

	/**
	 * @param inputStringDate
	 * @return
	 */
	public static String formatDateString(String inputStringDate) {
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

		String reformattedStr = "";

		try {

			reformattedStr = myFormat.format(fromUser.parse(inputStringDate));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reformattedStr;
	}

}
