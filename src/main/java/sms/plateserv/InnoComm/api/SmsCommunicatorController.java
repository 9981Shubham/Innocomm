package sms.plateserv.InnoComm.api;

import java.sql.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sms.plateserv.InnoComm.enums.ErrorMessages;
import sms.plateserv.InnoComm.exception.BaseException;
import sms.plateserv.InnoComm.model.BaseResponse;
import sms.plateserv.InnoComm.model.BulkSmsRequest;
import sms.plateserv.InnoComm.model.SmsRequest;
import sms.plateserv.InnoComm.service.SmsCommunicatorService;
import sms.plateserv.InnoComm.utils.Utils;

//(name = "Sms Service Management", description = "Service for SMS communication")
@RestController
@CrossOrigin
@RequestMapping("/smsSender")
public class SmsCommunicatorController {

	private static final Logger logger = LogManager.getLogger(SmsCommunicatorController.class);

	@Autowired
	SmsCommunicatorService smsCommunicatorService;

	@PostMapping("/sendSingleSMS")
	public ResponseEntity<BaseResponse> sendSms(@RequestBody SmsRequest request) {
		logger.info("Sending single SMS");
		try {
			BaseResponse baseResponse = smsCommunicatorService.sendSmsImpl(request);
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while Sending message {}", e);
			throw new BaseException(ErrorMessages.DATA_NOT_FOUND);
		}
	}

	@PostMapping("/addSmsBalance/{UserId}/{AmmountToAdd}")
	public ResponseEntity<BaseResponse> addBalance(@PathVariable("UserId") Long UserId,
			@PathVariable("AmmountToAdd") Double AmmountToAdd) {
		logger.debug("Receiving messages from Kafka topic Kafka_Example in the consumer");
		try {
			BaseResponse baseResponse = smsCommunicatorService.addSmsBalanceForSpecifiedUser(UserId, AmmountToAdd);
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			throw new BaseException(ErrorMessages.FAILURE);
		}
	}

	@PostMapping("/bulkSmsSend")
	public ResponseEntity<BaseResponse> sendBulkSms(@RequestBody BulkSmsRequest bulkRequest) {

		logger.info("Sending bulk SMS");
		try {
			BaseResponse baseResponse = smsCommunicatorService.bulkSmsSend(bulkRequest);
			return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while Sending message  {}", e);
			throw new BaseException(ErrorMessages.DATA_NOT_FOUND);
		}
	}

	@KafkaListener(topics = "Kafka_Example", groupId = "group_json", containerFactory = "userKafkaListenerFactory")
	public void consumeMessage(SmsRequest user) {
		logger.debug("Receiving messages from Kafka topic Kafka_Example in the consumer");
		try {
			smsCommunicatorService.consumeSms(user);
		} catch (Exception e) {
			throw new BaseException(ErrorMessages.SERVICE_NOT_AVAILABLE, ErrorMessages.FAILURE.name(), e.getMessage());
		}
	}

	@PostMapping("/callBackSupport")
	public void callBackSupport(@PathVariable("phoneNumber") String phoneNumber,
			@PathVariable("Status") String Status) {
		logger.info("CallBack Request by user");
		try {
			smsCommunicatorService.callBackSupport(phoneNumber, Status);
		} catch (Exception e) {
			logger.error("Error while getting callback status  {}", e);
			throw new BaseException(ErrorMessages.DATA_NOT_FOUND);
		}
	}

	@GetMapping("/smsStatusTracker")
	public ResponseEntity<BaseResponse> smsStatusTracker(@RequestParam("offset") int offset,
			@RequestParam("limit") int limit, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("date") Date date) {
		logger.info("Getting status of SMS");
		try {
			BaseResponse tm = smsCommunicatorService.smsStatusTracker(offset, limit, phoneNumber, date);
			return new ResponseEntity<BaseResponse>(tm, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while getting status  {}", e);
			throw new BaseException(ErrorMessages.DATA_NOT_FOUND);
		}
	}

}
