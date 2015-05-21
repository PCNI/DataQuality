package com.helicaltech.pcni.export;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used by the download report component
 *
 * @author Rajasekhar
 * @since 1.0
 */
public class ReportsUtility {
	private final static Logger logger = LoggerFactory.getLogger(ReportsUtility.class);

	/**
	 * Appends the system current time to the request parameter if it is not
	 * null. Else only time in milli seconds is returned
	 *
	 * @param requestParameter
	 *            The request parameter under concern
	 * @return string based on the request parameter
	 */
	public static String getReportName(String requestParameter) {
		if (requestParameter == null) {
			logger.debug("reportName is not provided. Using epoch time for reportName");
			requestParameter = "_" + System.currentTimeMillis();
		} else {
			logger.debug("reportName is provided. Using epoch time along with the reportName");
			requestParameter = requestParameter + "_" + System.currentTimeMillis();
		}
		return requestParameter;
	}

	/**
	 * Returns the url decoded string. A string is url encoded if it consists of
	 * % symbol.
	 *
	 * @param htmlString
	 *            The source of report html
	 * @param encoding
	 *            Usually utf-8
	 * @return The url decoded string
	 */
	public String decodeURLEncoding(String htmlString, String encoding) {
		Pattern pattern = Pattern.compile("[%]");
		Matcher matcher = pattern.matcher(htmlString);
		logger.info("htmlString containsPercentSymbol(is URL Encoded) = " + matcher.find());
		if (matcher.find()) {
			try {
				htmlString = java.net.URLDecoder.decode(htmlString, encoding);
			} catch (UnsupportedEncodingException ex) {
				logger.error("Character encoding is not supported " + encoding);
				ex.printStackTrace();
			}
			logger.debug("After URL decoding htmlString = " + htmlString);
		}
		return htmlString;
	}

	/**
	 * Returns the base64 decoded string
	 *
	 * @param htmlString
	 *            The source of report html
	 * @param encoding
	 *            Usually utf-8
	 * @return The base64 decoded string
	 */
	public String decodeBase64Encoding(String htmlString, String encoding) {
		boolean isBase64;
		try {
			byte[] data = htmlString.getBytes(encoding);
			isBase64 = Base64.isBase64(data);

			logger.info("htmlString is isBase64 encoded= " + isBase64);

			if (isBase64) {
				htmlString = new String(Base64.decodeBase64(data), encoding);
				logger.debug("After base64 decoding htmlString = " + htmlString);
			}

		} catch (UnsupportedEncodingException ex) {
			logger.debug("htmlString passed is not properly encoded with " + encoding);
			ex.printStackTrace();
		}
		return htmlString;
	}
}
