package com.pay.pie.domain.pay.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pay.pie.domain.meet.entity.Meet;
import com.pay.pie.domain.pay.entity.Pay;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long>, PayRepositoryCustom {
	List<Pay> findByMeetOrderByCreatedAtDesc(Meet meet);

	Pay findFirstByMeetOrderByCreatedAtDesc(Meet meet);

	List<Pay> findAllByMeetOrderByCreatedAtDesc(Meet meet);

	//	Pay findFirstByMeetOrderByUpdatedAtDesc(Meet meet);

	@Query
		(
			"""
   				SELECT p
   				FROM Pay p
   				WHERE p.meet.id IN :meets AND p.createdAt = (
   					SELECT MAX(p2.createdAt)
   					FROM Pay p2
   					WHERE p2.meet.id = p.meet.id
   				)
			"""
		)
	List<Pay> findLatestPayInfo(@Param("meets") List<Long> meets);

}
