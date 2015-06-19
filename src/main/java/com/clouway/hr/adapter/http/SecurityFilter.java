package com.clouway.hr.adapter.http;

import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Created on 15-5-5.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

@Singleton
public class SecurityFilter implements Filter {


  public void init(FilterConfig config) throws ServletException {
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
    System.out.println("Second filter");

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;

    final Principal userPrincipal = request.getUserPrincipal();

    if (userPrincipal == null) {
      response.sendError(response.SC_UNAUTHORIZED, "you are not authorized");
      return;
    }

    chain.doFilter(req, resp);
  }

  public void destroy() {
  }
}
