package sms.plateserv.InnoComm.entitys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import lombok.Builder;
import lombok.Data;
@Data
@Table(name = "UserWallet")
@Entity(name = "UserWallet")
@Proxy(lazy = false)
public class UserWallet {

	@Id
	@Column(name = "Wallet_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "BALANCE")
	private Double balance;
}