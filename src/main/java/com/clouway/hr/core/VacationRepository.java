package com.clouway.hr.core;


import com.clouway.hr.adapter.http.VacationRequestDto;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface VacationRepository {
  void updateStatus(Long id, String status);

  void add(VacationRequestDto vacation);

  String getStatus(Long id);
}
