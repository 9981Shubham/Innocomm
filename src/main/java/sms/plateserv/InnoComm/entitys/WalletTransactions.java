package sms.plateserv.InnoComm.entitys;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sms.plateserv.InnoComm.enums.TransactionStatus;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WalletTransactions")
@Entity(name = "WalletTransactions")
@Proxy(lazy = false)
public class WalletTransactions {

	@Id
	@Column(name = "WALLETTRANSACTION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;

	@Column(name = "DATE")
	private Date date;
	
	@Column(name= "USER_ID")
	private  Long user;
}