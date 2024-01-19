package sms.plateserv.InnoComm.utils;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sms.plateserv.InnoComm.enums.ErrorMessages;
import sms.plateserv.InnoComm.model.BaseResponse;

public class Utils {

	static Logger log = LoggerFactory.getLogger(Utils.class);

	/**
	 * Utility method to check if a string is empty.
	 *
	 * @param str Input string which has to be checked.
	 * @return true, if checks if is empty
	 */
	public static boolean isEmpty(String str) {
		return str == null || (str.trim().length() == 0);
	}

	/**
	 * Utility method to check if a string is not empty.
	 *
	 * @param str Input string which has to be checked.
	 * @return true, if checks if is not empty
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * Checks if the given objArr is null or size 0
	 *
	 * @param objArr array of Object.
	 * @return true if the array is null or size 0.
	 */
	public static boolean isEmpty(Object[] objArr) {
		return (objArr == null) || (objArr.length < 1);
	}

	/**
	 * Checks if the given objArr is not null and size not 0
	 *
	 * @param objArr array of Object.
	 * @return true if the array is not null and size 0.
	 */
	public static boolean isNotEmpty(Object[] objArr) {
		return !isEmpty(objArr);
	}

	/**
	 * This method checks if the given list is null or is empty.
	 *
	 * @param listObj instance of java.util.List
	 * @return true if list is null or size == 0
	 */
	public static boolean isEmpty(List<?> listObj) {
		return listObj == null || listObj.isEmpty();
	}

	public static <T> boolean isNotEmpty(T t) {
		return !isEmpty(t);
	}

	public static <T> boolean isEmpty(T t) {
		return t == null;
	}

	public static BaseResponse SuccessResponse(Object response, String message) {
		BaseResponse baseResponse = new BaseResponse();
		ErrorMessages success = ErrorMessages.SUCCESS;
		baseResponse.setMessage(message);
		baseResponse.setResponse(response);
		baseResponse.setStatus(success.message());
		baseResponse.setStatusCode(success.code());
		return baseResponse;
	}

	public static BaseResponse ErrorResponse(Object response, String message) {
		BaseResponse baseResponse = new BaseResponse();
		ErrorMessages error = ErrorMessages.FAILURE;
		baseResponse.setMessage(message);
		baseResponse.setResponse(response);
		baseResponse.setStatus(error.message());
		baseResponse.setStatusCode(error.code());
		return baseResponse;
	}

	public static final int PHONE_NUMBER_LENGTH = 10;
	public static final String PHONE_NUMBER = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";

	/**
	 * If given one is valid format returns 10 digit number, if not returns null.
	 *
	 * @param communicationAddress
	 * @return
	 */
	public static Boolean getPhoneNumberValidation(String communicationAddress) {
		if (isEmpty(communicationAddress))
			return Boolean.FALSE;
		Pattern p = Pattern.compile(PHONE_NUMBER);
		Matcher m = p.matcher(communicationAddress);
		if (m.find()) {
			if (communicationAddress.length() == PHONE_NUMBER_LENGTH) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public static String generateRandomUuid() {
		return UUID.randomUUID().toString();
	}
}