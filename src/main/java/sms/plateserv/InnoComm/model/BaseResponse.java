package sms.plateserv.InnoComm.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseResponse {
	private String message;
	private String status;
	private Object response;
	private String statusCode;
}
