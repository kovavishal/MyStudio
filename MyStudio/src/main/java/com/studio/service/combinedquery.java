package com.studio.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.studio.domain.ReportDetail;

public interface combinedquery extends JpaRepository {
 //@Query(name = "query1", nativeQuery = true)
	  List<ReportDetail> orderDetails();

}
