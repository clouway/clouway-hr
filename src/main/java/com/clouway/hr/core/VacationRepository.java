package com.clouway.hr.core;


import com.clouway.hr.adapter.http.VacationRequestDto;
import com.clouway.hr.adapter.http.VacationResponseDto;

import java.util.List;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface VacationRepository {
  void updateStatus(Long id, String status);

  void add(VacationRequestDto vacation);

  String getStatus(Long id);

  List<VacationResponseDto> get(String userId);

  List<VacationResponseDto> getStatus(String pending);

  List<VacationResponseDto> getHistory(User currentUser);
}
