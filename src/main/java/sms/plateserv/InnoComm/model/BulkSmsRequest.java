package sms.plateserv.InnoComm.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BulkSmsRequest {
	private String message;
	private List<String> phoneNumber;
	private String userId;

	public BulkSmsRequest(String message, List<String> phoneNumber, String userId) {
		super();
		this.message = message;
		this.phoneNumber = phoneNumber;
		this.userId = userId;
	}

	public BulkSmsRequest() {
		super();
	}

	@Override
	public String toString() {
		return "BulkSmsRequest [message=" + message + ", userId=" + userId + "]";
	}
}
