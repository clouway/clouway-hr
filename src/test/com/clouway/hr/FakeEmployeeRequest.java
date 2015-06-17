package com.clouway.hr;

import com.google.common.collect.Multimap;
import com.google.inject.TypeLiteral;
import com.google.sitebricks.headless.Request;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public class FakeEmployeeRequest implements Request {
  @Override
  public <E> RequestRead<E> read(Class<E> aClass) {
    return null;
  }

  @Override
  public <E> RequestRead<E> read(TypeLiteral<E> typeLiteral) {
    return null;
  }

  @Override
  public void readTo(OutputStream outputStream) throws IOException {

  }

  @Override
  public Multimap<String, String> headers() {
    return null;
  }

  @Override
  public Multimap<String, String> params() {
    return null;
  }

  @Override
  public Multimap<String, String> matrix() {
    return null;
  }

  @Override
  public String matrixParam(String s) {
    return null;
  }

  @Override
  public String param(String s) {
    return null;
  }

  @Override
  public String header(String s) {
    return null;
  }

  @Override
  public String uri() {
    return null;
  }

  @Override
  public String path() {
    return null;
  }

  @Override
  public String context() {
    return null;
  }

  @Override
  public String method() {
    return null;
  }
}
