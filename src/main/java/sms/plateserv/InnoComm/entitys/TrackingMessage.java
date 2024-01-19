package sms.plateserv.InnoComm.entitys;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_tracker")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trackingID;
	private String phoneNumber;
	private String message;
	private String statusCode;
	private String reason;
	@Column(name = "DATE")
	private Date dateTime;

}