package com.clouway.hr.core.vacationstate;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class VacationStatus {
  private State state;
  public Long id;

  public void setState(State state) {
    this.state = state;
  }

  public State getState() {
    return state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    VacationStatus that = (VacationStatus) o;

    if (state != null ? !state.equals(that.state) : that.state != null) return false;
    return !(id != null ? !id.equals(that.id) : that.id != null);

  }

  @Override
  public int hashCode() {
    int result = state != null ? state.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
