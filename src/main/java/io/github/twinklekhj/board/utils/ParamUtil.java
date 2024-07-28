package io.github.twinklekhj.board.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamUtil {
	public static String parseString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String parseString(Object obj, String value) {
		return obj == null ? value : obj.toString();
	}

	public static int parseInt(Object obj) {
		if (obj == null)
			return -1;
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return -1;
		}
	}
	public static int parseInt(Object obj, int value) {
		if (obj == null)
			return value;
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return value;
		}
	}
	
	public static Map<String, Object> parseInt(Map<String, Object> paramMap, String key) {
		if (paramMap.get(key) != null) {
			paramMap.put(key, Integer.parseInt(paramMap.get(key).toString()));
		}
		return paramMap;
	}

	public static Long parseLong(Object obj) {
		if (obj == null)
			return -1L;
		try {
			return Long.parseLong(obj.toString());
		} catch (Exception e) {
			return -1L;
		}
	}

	/**
	 * [ParamUtil] paramMap의 여러 키 값들을 int로 변환
	 * 
	 * @param paramMap
	 * @param keys
	 * @return
	 */
	public static Map<String, Object> parseInt(Map<String, Object> paramMap, String[] keys) {
		for (String key : keys) {
			String value = ParamUtil.parseString(paramMap.get(key));
			try {
				paramMap.put(key, Integer.parseInt(value));
			} catch (NumberFormatException e) {
			}
		}

		return paramMap;
	}

	/**
	 * [ParamUtil] String을 쪼개 Array로 반환
	 * 
	 * @param obj
	 * @return
	 */
	public static String[] parseStringToArr(Object obj) {
		return parseStringToArr(obj, ",");
	}

	public static String[] parseStringToArr(Object obj, String regex) {
		String[] arr = {};
		if (obj == null)
			return arr;

		String str = obj.toString().replace(" ", "").replace("\"", "");
		if (str.isEmpty())
			return arr;

        return str.split(regex);
	}

	/**
	 * [ParamUtil] paramMap의 필수 파라미터 체크
	 * 
	 * @param paramMap
	 * @param keys
	 * @return
	 */
	public static boolean checkRequired(Map<String, Object> paramMap, String[] keys) {
		if (keys == null) {
			return true;
		} else if (keys.length == 0 && paramMap.isEmpty()) {
			return true;
		}

		for (String key : keys) {
			if (parseString(paramMap.get(key)).equals(""))
				return false;
		}

		return true;
	}


	/**
	 * [ParamUtil] String 데이터를 Timestamp 형식으로 변환
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, Object> strToTimestamp(Map<String, Object> paramMap, String key) throws ParseException {
		if (paramMap.get(key) != null) {
			// date parsing
			StringBuffer date = new StringBuffer(paramMap.get(key).toString());
			date.insert(12, ':');
			date.insert(10, ':');
			date.insert(8, ' ');
			date.insert(6, '-');
			date.insert(4, '-');

			paramMap.put(key, date.toString());
		}

		return paramMap;
	}

	/**
	 * [파일 업로드] Request 객체의 Param들과 File을 받아서 Byte로 변환한 뒤 Map 형태로 변환
	 * 
	 * @param request
	 * @param uploadFile
	 * @param fileParamName
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> convertRequestToMapWithFile(MultipartHttpServletRequest request,
			MultipartFile uploadFile, String fileParamName) throws IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			paramMap.put(key, request.getParameter(key));
		}
		paramMap.put(fileParamName, uploadFile.getBytes());
		return paramMap;
	}

	public static boolean formatDateByType(Map<String, Object> paramMap, String type) {
		if ("day".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM-DD");
			paramMap.put("SeriesFormat", "YYYY-MM");
			paramMap.put("Series", "1 day");
			paramMap.put("endDate", Utils.getNextDate(paramMap.get("startDate").toString(), "Month"));
		} else if ("month".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM");
			paramMap.put("SeriesFormat", "YYYY");
			paramMap.put("Series", "1 month");
			paramMap.put("endDate", Utils.getNextDate(paramMap.get("startDate").toString(), "Year"));
		} else if ("all".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM-DD");
			paramMap.put("SeriesFormat", "YYYY-MM-DD");
			paramMap.put("Series", "1 day");
			paramMap.put("endDate", Utils.getNextDate(paramMap.get("endDate").toString(), "Day"));
		} else
			return false;

		return true;
	}

	/**
	 * [ParamUtil] data의 Empty String 값들을 Null로 변경
	 * 
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> emptyToNull(Map<String, Object> paramMap) {
		for (String strKey : paramMap.keySet()) {
			if (paramMap.get(strKey).toString().isEmpty()) {
				paramMap.put(strKey, null);
			}
		}

		return paramMap;
	}
}
