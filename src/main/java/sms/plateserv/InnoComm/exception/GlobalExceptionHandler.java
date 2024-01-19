package sms.plateserv.InnoComm.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sms.plateserv.InnoComm.enums.ErrorMessages;
import sms.plateserv.InnoComm.model.BaseResponse;
import sms.plateserv.InnoComm.utils.Utils;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = { BaseException.class })
	public ResponseEntity<BaseResponse> defaultBaseExceptionHandler(BaseException exception) {

		LOG.info(" defaultLICErrorHandler : ");
		LOG.error(exception.getMessage(), exception);
		ErrorMessages errorMessages = exception.errorMessages;

		String message = errorMessages.message();
		Object[] customMessage = exception.customMessage;
		if (Utils.isNotEmpty(customMessage)) {
			message = String.format(message, customMessage);
		}
		BaseResponse response = new BaseResponse();
		String status = exception.status;
		if (Utils.isEmpty(status)) {
			status = ErrorMessages.FAILURE.message();
		}
		response.setMessage(message);
		response.setStatus(status);
		response.setStatusCode(errorMessages.code());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(value = { UserLoginException.class })
	public ResponseEntity<BaseResponse> defaultUserLoginExceptionHandler(UserLoginException exception) {

		LOG.info(" defaultLICErrorHandler : ");
		LOG.error(exception.getMessage(), exception);
		String message = exception.errorMessages;
		Object[] customMessage = List.of(exception.customMessage).toArray();
		if (Utils.isNotEmpty(customMessage)) {
			message = String.format(message, customMessage);
		}
		BaseResponse response = new BaseResponse();
		String status = null;
		if (Utils.isEmpty(status)) {
			status = ErrorMessages.FAILURE.message();
		}
		response.setStatus(status);
		response.setStatusCode(ErrorMessages.FAILURE.code());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(value = { InvalidTokenRequestException.class })
	public ResponseEntity<BaseResponse> defaultInvalidTokenRequestExceptionHandler(
			InvalidTokenRequestException exception) {

		LOG.info(" defaultLICErrorHandler : ");
		LOG.error(exception.getMessage(), exception);
		String message = exception.getMessage();
		Object[] customMessage = List.of(exception.getMessage()).toArray();
		if (Utils.isNotEmpty(customMessage)) {
			message = String.format(message, customMessage);
		}
		BaseResponse response = new BaseResponse();
		String status = null;
		if (Utils.isEmpty(status)) {
			status = ErrorMessages.FAILURE.message();
		}
		response.setMessage(message);
		response.setStatus(status);
		response.setStatusCode(ErrorMessages.FAILURE.code());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(value = { TokenRefreshException.class })
	public ResponseEntity<BaseResponse> defaultLICErrorHandler(TokenRefreshException exception) {

		LOG.info(" defaultLICErrorHandler : ");
		LOG.error(exception.getMessage(), exception);

		String message = exception.getMessage();
		Object[] customMessage = List.of(exception.getMessage()).toArray();
		if (Utils.isNotEmpty(customMessage)) {
			message = String.format(message, customMessage);
		}
		BaseResponse response = new BaseResponse();
		String status = null;
		if (Utils.isEmpty(status)) {
			status = ErrorMessages.FAILURE.message();
		}
		response.setMessage(message);
		response.setStatus(status);
		response.setStatusCode(ErrorMessages.FAILURE.code());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}