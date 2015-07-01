package com.clouway.hr.adapter.http;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ResponseMessageDto {
  private String message;

  public ResponseMessageDto(String message) {
    this.message = message;
  }

  public ResponseMessageDto() {
  }

  public String getMessage() {
    return message;
  }
}
