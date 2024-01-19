package sms.plateserv.InnoComm.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sms.plateserv.InnoComm.enums.SmsResponseStatus;

@Data
@Builder
public class SmsResponse {

	private String message;
	private SmsResponseStatus status;
}