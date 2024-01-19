package sms.plateserv.InnoComm.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import sms.plateserv.InnoComm.entitys.TrackingMessage;
import sms.plateserv.InnoComm.entitys.User;
import sms.plateserv.InnoComm.entitys.UserWallet;
import sms.plateserv.InnoComm.entitys.WalletTransactions;
import sms.plateserv.InnoComm.enums.ErrorMessages;
import sms.plateserv.InnoComm.enums.TransactionStatus;
import sms.plateserv.InnoComm.exception.BaseException;
import sms.plateserv.InnoComm.model.BaseResponse;
import sms.plateserv.InnoComm.model.BulkSmsRequest;
import sms.plateserv.InnoComm.model.ErrorCodes;
import sms.plateserv.InnoComm.model.SmsRequest;
import sms.plateserv.InnoComm.repository.TrackingMessageRepository;
import sms.plateserv.InnoComm.repository.UserRepository;
import sms.plateserv.InnoComm.repository.UserWalletRepository;
import sms.plateserv.InnoComm.repository.WalletTransactionRepository;
import sms.plateserv.InnoComm.utils.Utils;

//@Transactional
@Service
public class SmsCommunicatorService {

	private static final Logger LOGGER = LogManager.getLogger(SmsCommunicatorService.class);

	@Value("${sms.provider.service}")
	private Boolean provideSms;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TrackingMessageRepository trackingMessageRepository;

	@Autowired
	WalletTransactionRepository walletTransactionRepository;

	@Autowired
	private KafkaTemplate<String, SmsRequest> kafkaTemplate;

	private static final String TOPIC = "Kafka_Example";

	public static final String PHONE_NUMBER_PATTERN = "^\\+91[6789]\\d{9}$";

	@Autowired
	UserWalletRepository userWalletRepository;

	public BaseResponse sendSmsImpl(SmsRequest request) {
		if (!Utils.isEmpty(request)) {
			LOGGER.debug("Message sending initiated");
			BaseResponse response = null;
			List<SmsRequest> smsRequestList = IntStream
					.range(0, (int) Math.ceil((double) request.getMessage().length() / 160))
					.mapToObj(i -> SmsRequest.builder()
							.message(request.getMessage().substring(i * 160,
									Math.min((i + 1) * 160, request.getMessage().length())))
							.phoneNumber(request.getPhoneNumber()).userId(request.getUserId()).build())
					.collect(Collectors.toList());
			smsRequestList.forEach(sr -> kafkaTemplate.send(TOPIC, sr));
			response = Utils.SuccessResponse(smsRequestList, "SENDED");
			return response;
		} else {
			throw new BaseException(ErrorMessages.SOURCE_INPUT_REQUIRE, ErrorMessages.FAILURE.name(),
					"input Fields %s  is required ");
		}
	}

	public BaseResponse addSmsBalanceForSpecifiedUser(Long userId, Double ammountToAdd) {
		BaseResponse response = null;
		Optional<User> findById = userRepository.findById(userId);
		if (!findById.isEmpty() && ammountToAdd < Double.valueOf(100000)) {
			LOGGER.debug("The userID is:- "+userId+" and the amount to add is:- "+ammountToAdd);
			UserWallet userWallet = findById.get().getUserWallet();
			userWallet.setBalance(userWallet.getBalance() + ammountToAdd);
			userWalletRepository.save(userWallet);
			WalletTransactions walletTransactions = new WalletTransactions().builder().amount(ammountToAdd)
					.status(TransactionStatus.CREDIT).date(new Date(System.currentTimeMillis())).user(userId).build();
			walletTransactionRepository.save(walletTransactions);
			response = Utils.SuccessResponse("AMOUNT +" + ammountToAdd + " ADDED TO " + userId,
					"AMOUNT ADDED SUCCESSFULLY");
		} else {
			response = Utils.ErrorResponse("USER NOT FOUND", "AMOUNT NOT ADDED");
			throw new BaseException(ErrorMessages.SOURCE_INPUT_REQUIRE, response.toString(),
					"MOBILE NUMBER NOT PRESENT");
		}
		return response;
	}

	public void consumeSms(SmsRequest request) {
		// * Check phone number *
		String phoneNumber = request.getPhoneNumber();
		LOGGER.debug("The phone number provided is:- "+phoneNumber);
		Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
		Matcher matcher = pattern.matcher(phoneNumber);
		TrackingMessage trackingMessage = null;

		// * Get the message size *
		String message = request.getMessage();

		// * If the phone number is valid, then do the other operations *
		if (matcher.matches()) {

			// * Check the phone number, whether the character size is exceeding 160
			// characters *
			if (message.length() > 160) {
				LOGGER.debug("Message length exceeded 160 characters");
				trackingMessage = TrackingMessage.builder().phoneNumber(phoneNumber)
						.message("Message size is more than 160 characters").statusCode(ErrorCodes.CODE_1705)
						.reason(ErrorCodes.map.get(ErrorCodes.CODE_1705)).dateTime(new Date(System.currentTimeMillis()))
						.build();
				return;
			} else {
				// * If the message size is <= 160 characters then adjust the balance *
				Long userId = Long.parseLong(request.getUserId());
				Optional<User> opt = userRepository.findById(userId);
				if (opt.isPresent()) {
					User user = opt.get();
					// * START:- Synchronized block *
					synchronized (this) {
						UserWallet userWallet = user.getUserWallet();
						Double balance = userWallet.getBalance();

						// * If the balance amount is greater than 0, then adjust the balance amount,
						// and update it into database *
						// * Also update the same into the Tracking table *
						if (balance > 0) {
							LOGGER.debug("The balance of the user with ID:- "+userId+" is greater than 0");
							WalletTransactions walletTransactions = new WalletTransactions().builder()
									.amount(Double.valueOf(1)).status(TransactionStatus.DEBIT)
									.date(new Date(System.currentTimeMillis())).user(userId).build();
							walletTransactionRepository.save(walletTransactions);
							LOGGER.debug("User wallet is saved successfully");
							balance = balance - 1;
							userWallet.setBalance(balance);
							user.setUserWallet(userWallet);
							userRepository.save(user);
							LOGGER.debug("User is also saved and the balance is also deducted after the message is sent");
							// * Update this to tracking repository that message has been sent successfully
							// *
							trackingMessage = TrackingMessage.builder().phoneNumber(phoneNumber).message(message)
									.statusCode("200").reason("Message sent successfully")
									.dateTime(new Date(System.currentTimeMillis())).build();
						}
						// * If the balance amount is not sufficient then update as insufficient credit
						// balance *
						else {
							LOGGER.debug("The message cannot be sent as there is insufficient balance");
							trackingMessage = TrackingMessage.builder().phoneNumber(phoneNumber).message(message)
									.statusCode(ErrorCodes.CODE_1025).reason(ErrorCodes.map.get(ErrorCodes.CODE_1025))
									.dateTime(new Date(System.currentTimeMillis())).build();
						}
					}
					// * END:- Synchronized block *
				}
				// * If the sender is not present in database *
				else {
					LOGGER.debug("The message cannot be sent as the sender is not found");
					trackingMessage = TrackingMessage.builder().phoneNumber(phoneNumber).message(message)
							.statusCode("400").reason("User not found").dateTime(new Date(System.currentTimeMillis()))
							.build();
				}
			}
		} else {
			// * If the phone number is not valid, then update this into the Tracking
			// database *
			LOGGER.debug("The message cannot be sent as the sender provided invalid phone number");
			trackingMessage = TrackingMessage.builder().phoneNumber(phoneNumber).message(message)
					.statusCode(ErrorCodes.CODE_1704)
					.reason(ErrorCodes.map.get(ErrorCodes.CODE_1704).replace("type", "phone number"))
					.dateTime(new Date(System.currentTimeMillis())).build();
		}
		callBackSupport(request.getPhoneNumber(), trackingMessage.getReason());
		trackingMessageRepository.save(trackingMessage);
	}

	public BaseResponse bulkSmsSend(BulkSmsRequest bulkRequest) {
		BaseResponse response = null;
		if (bulkRequest.getPhoneNumber().size() >0) {
			bulkRequest.getPhoneNumber().stream().forEach(
					phone -> sendSmsImpl(new SmsRequest(bulkRequest.getMessage(), phone, bulkRequest.getUserId())));
			response = Utils.SuccessResponse("Message Send To :" + bulkRequest.getPhoneNumber(), "SMS SENT");
			LOGGER.debug("Message is successfully sent in bulk");
		} else {
			LOGGER.debug("Message is not sent as mobile number is not present");
			response = Utils.ErrorResponse("MOBILE NUMBER NOT PRESENT", "Failed to send SMS");
			throw new BaseException(ErrorMessages.SOURCE_INPUT_REQUIRE, response.toString(),
					"MOBILE NUMBER NOT PRESENT");
		}
		return response;
	}

	public void callBackSupport(String phoneNumber, String status) {
		LOGGER.debug("CALLBACK :Message is successfully sent");
		LOGGER.info("CALLBACK :Message is successfully sent");
		System.out.println("SMS CALLBACK SUPPORT :" + "MESSAGE SENT ON PHONENUMBER : " + phoneNumber
				+ " HAVING STATUS :" + status);
	}

	public BaseResponse smsStatusTracker(int offset, int limit, String phoneNumber, Date date) {
		LOGGER.debug("Inside SMS Status Tracking");
		BaseResponse response = null;
		if (phoneNumber != "" && phoneNumber != null) {
			Pageable pageable = PageRequest.of(offset, limit);
			phoneNumber = phoneNumber.replace(" ", "+");
			Page<List<TrackingMessage>> dmsTasksPage = trackingMessageRepository
					.findSmsStatusByDateAndPhoneBumber(phoneNumber, date, pageable);
			response = Utils.SuccessResponse(dmsTasksPage, "STATUS FOUND");
			return response;
		} else {
			response = Utils.ErrorResponse("MOBILE NUMBER NOT PRESENT", "NOT FOUND");
			throw new BaseException(ErrorMessages.SOURCE_INPUT_REQUIRE, response.toString(),
					"MOBILE NUMBER NOT PRESENT");
		}
	}
}
