package sms.plateserv.InnoComm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sms.plateserv.InnoComm.entitys.Role;
import sms.plateserv.InnoComm.entitys.User;
import sms.plateserv.InnoComm.entitys.UserWallet;
import sms.plateserv.InnoComm.enums.RoleName;
import sms.plateserv.InnoComm.model.BaseResponse;
import sms.plateserv.InnoComm.model.RegistrationRequest;
import sms.plateserv.InnoComm.model.SmsRequest;
import sms.plateserv.InnoComm.service.AuthService;
import sms.plateserv.InnoComm.service.SmsCommunicatorService;

//@DataJpaTest
@SpringBootTest
class InnoCommControllerTests {

//	@Mock
//	private SmsCommunicatorService smsCommunicatorService;

	@Autowired
	private SmsCommunicatorService smsCommunicatorService;
	// private SmsCommunicatorController smsCommunicatorController;
	@Autowired
	private AuthService authService;

	@Test
	void userRegistration() throws Exception {
		RegistrationRequest request=new RegistrationRequest();
		request.setEmail("jhkjkohnSmith.adt@gmail.com");
		request.setPassword("Shubham1lj");
		request.setUsername("Shubha@123");
		User user=authService.createUser(request);
		User userSample=new User();
		userSample.setActive(true);
		userSample.setEmail("jhkjkohnSmith.adt@gmail.com");
		userSample.setPassword("Shubha@123");
		Role newRole = new Role();
		newRole.setId(1l);
		newRole.setRole(RoleName.ROLE_ADMIN);
		Set<Role> role =new HashSet<Role>();
		role.add(newRole);
		userSample.setRoles(role);
		userSample.setUsername("Shubham1lj");
		UserWallet savedWallet = new UserWallet();
		savedWallet.setBalance(Double.valueOf(6));
		savedWallet.setId(1l);
		userSample.setUserWallet(savedWallet);
		assertEquals(userSample, user);
	}
	@Test
	void sendSms_ValidRequest_ReturnsOk() throws Exception {
		SmsRequest validRequest = new SmsRequest();

		// Set values for the fields using the setter methods
		validRequest.setMessage("callback functionality implementations.");
		validRequest.setPhoneNumber("+919981763130");
		validRequest.setUserId("1");

		BaseResponse baseResponse = smsCommunicatorService.sendSmsImpl(validRequest);
		System.out.println(baseResponse);

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		BaseResponse baseResponse = new BaseResponse();
//
//		// Set values for the fields using setter methods
//		baseResponse.setMessage("SENDED");
//		baseResponse.setStatus("SUCCESS");
//
//		// Create an array for the response and set values for its fields
//		SmsRequest[] responseArray = new SmsRequest[1];
//		SmsRequest smsRequest = new SmsRequest();
//		smsRequest.setMessage("callback functionality implementations.");
//		smsRequest.setPhoneNumber("+919981763130");
//		smsRequest.setUserId("1");
//		responseArray[0] = smsRequest;
//
//		baseResponse.setResponse(responseArray);
//
//		baseResponse.setStatusCode("200");
//		when(smsCommunicatorService.sendSmsImpl(validRequest)).thenReturn(baseResponse);
//		ResponseEntity<BaseResponse> responseEntity = smsCommunicatorController.sendSms(validRequest);

	}

//    @Test
//    void sendSms_EmptyRequest_ReturnsNotFound() throws Exception {
//        SmsRequest emptyRequest = new SmsRequest(/* provide empty request data */);
//
//        Mockito.when(smsCommunicatorService.sendSmsImpl(emptyRequest)).thenThrow(new BaseException(ErrorMessages.DATA_NOT_FOUND));
//
//        mockMvc.perform(post("/smsSender/sendSingleSMS")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(emptyRequest)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("FAILURE"));
//    }
//
//    @Test
//    void addBalance_ValidRequest_ReturnsOk() throws Exception {
//        Long userId = 1L;
//        Double amountToAdd = 50.0;
//
//        BaseResponse expectedResponse = new BaseResponse(/* provide expected response data */);
//
//        Mockito.when(smsCommunicatorService.addSmsBalanceForSpecifiedUser(userId, amountToAdd)).thenReturn(expectedResponse);
//
//        mockMvc.perform(post("/smsSender/addSmsBalance/{UserId}/{AmmountToAdd}", userId, amountToAdd))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void addBalance_InvalidUser_ReturnsFailure() throws Exception {
//        Long invalidUserId = -1L;
//        Double amountToAdd = 50.0;
//
//        Mockito.when(smsCommunicatorService.addSmsBalanceForSpecifiedUser(invalidUserId, amountToAdd))
//                .thenThrow(new BaseException(ErrorMessages.FAILURE));
//
//        mockMvc.perform(post("/smsSender/addSmsBalance/{UserId}/{AmmountToAdd}", invalidUserId, amountToAdd))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.status").value("FAILURE"));
//    }
//
//    @Test
//    void bulkSmsSend_ValidRequest_ReturnsOk() throws Exception {
//        BulkSmsRequest validBulkRequest = new BulkSmsRequest(/* provide valid bulk request data */);
//
//        BaseResponse expectedResponse = new BaseResponse(/* provide expected response data */);
//
//        Mockito.when(smsCommunicatorService.bulkSmsSend(validBulkRequest)).thenReturn(expectedResponse);
//
//        mockMvc.perform(post("/smsSender/bulkSmsSend")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(validBulkRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void bulkSmsSend_EmptyRequest_ReturnsNotFound() throws Exception {
//        BulkSmsRequest emptyBulkRequest = new BulkSmsRequest(/* provide empty bulk request data */);
//
//        Mockito.when(smsCommunicatorService.bulkSmsSend(emptyBulkRequest)).thenThrow(new BaseException(ErrorMessages.DATA_NOT_FOUND));
//
//        mockMvc.perform(post("/smsSender/bulkSmsSend")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(emptyBulkRequest)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("FAILURE"));
//    }
//
//    @Test
//    void consumeMessage_ValidRequest_ReturnsOk() throws Exception {
//        SmsRequest validRequest = new SmsRequest(/* provide valid request data */);
//
//        mockMvc.perform(post("/smsSender/consumeMessage")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(validRequest)))
//                .andExpect(status().isOk());
//
//        Mockito.verify(smsCommunicatorService, Mockito.times(1)).consumeSms(validRequest);
//    }
//
//    @Test
//    void consumeMessage_ExceptionThrown_ReturnsFailure() throws Exception {
//        SmsRequest validRequest = new SmsRequest(/* provide valid request data */);
//
//        Mockito.doThrow(new BaseException(ErrorMessages.SERVICE_NOT_AVAILABLE, "FAILURE", "Service not available"))
//                .when(smsCommunicatorService).consumeSms(validRequest);
//
//        mockMvc.perform(post("/smsSender/consumeMessage")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(validRequest)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.status").value("FAILURE"))
//                .andExpect(jsonPath("$.message").value("Service not available"));
//    }
//
//    @Test
//    void callBackSupport_ValidRequest_ReturnsOk() throws Exception {
//        String phoneNumber = "1234567890";
//        String status = "SUCCESS";
//
//        mockMvc.perform(post("/smsSender/callBackSupport")
//                .param("phoneNumber", phoneNumber)
//                .param("Status", status))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void callBackSupport_InvalidPhoneNumber_ReturnsNotFound() throws Exception {
//        String invalidPhoneNumber = "invalidNumber";
//        String status = "SUCCESS";
//
//        Mockito.doThrow(new BaseException(ErrorMessages.DATA_NOT_FOUND))
//                .when(smsCommunicatorService).callBackSupport(invalidPhoneNumber, status);
//
//        mockMvc.perform(post("/smsSender/callBackSupport")
//                .param("phoneNumber", invalidPhoneNumber)
//                .param("Status", status))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("FAILURE"));
//    }
//
//    @Test
//    void smsStatusTracker_ValidRequest_ReturnsOk() throws Exception {
//        int offset = 0;
//        int limit = 10;
//        String phoneNumber = "1234567890";
//        Date date = Date.valueOf("2024-01-19");
//
//        BaseResponse expectedResponse = new BaseResponse(/* provide expected response data */);
//
//        Mockito.when(smsCommunicatorService.smsStatusTracker(offset, limit, phoneNumber, date)).thenReturn(expectedResponse);
//
//        mockMvc.perform(get("/smsSender/smsStatusTracker")
//                .param("offset", String.valueOf(offset))
//                .param("limit", String.valueOf(limit))
//                .param("phoneNumber", phoneNumber)
//                .param("date", date.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void smsStatusTracker_InvalidDate_ReturnsBadRequest() throws Exception {
//        int offset = 0;
//        int limit = 10;
//        String phoneNumber = "1234567890";
//        String invalidDate = "invalid_date";
//
//        mockMvc.perform(get("/smsSender/smsStatusTracker")
//                .param("offset", String.valueOf(offset))
//                .param("limit", String.valueOf(limit))
//                .param("phoneNumber", phoneNumber)
//                .param("date", invalidDate))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value("FAILURE"))
//                .andExpect(jsonPath("$.message").value("Invalid date format"));
//    }

}
