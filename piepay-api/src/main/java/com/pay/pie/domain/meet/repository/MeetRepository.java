package com.pay.pie.domain.meet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pay.pie.domain.meet.entity.Meet;

import io.lettuce.core.dynamic.annotation.Param;

public interface MeetRepository extends JpaRepository<Meet, Long> {
	Optional<Meet> findByMeetInvitation(String meetInvitation);

	@Query(
		"""
			select mt, mm
			from MemberMeet mm
			join fetch mm.meet mt
			where mt.id = :meetId
						"""
	)
	Object[] getHighlight(@Param("meetId") Long meetId);
}
