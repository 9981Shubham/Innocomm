package sms.plateserv.InnoComm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SmsRequest {
    private String message;
    private String phoneNumber;
    private String userId;
	public SmsRequest(String message, String phoneNumber, String userId) {
		super();
		this.message = message;
		this.phoneNumber = phoneNumber;
		this.userId = userId;
	}
	public SmsRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SmsRequest [message=" + message + ", phoneNumber=" + phoneNumber + ", userId=" + userId + "]";
	}
    
    
}
