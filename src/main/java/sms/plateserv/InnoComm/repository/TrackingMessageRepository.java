package sms.plateserv.InnoComm.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import sms.plateserv.InnoComm.entitys.TrackingMessage;

@Repository
public interface TrackingMessageRepository
		extends PagingAndSortingRepository<TrackingMessage, Long>, JpaSpecificationExecutor<TrackingMessage> {
	@Query(value = "select * from message_tracker where phone_number=?1 and date=?2", nativeQuery = true)
	Page<List<TrackingMessage>> findSmsStatusByDateAndPhoneBumber(String mobileNumber, Date date, Pageable pageable);
}