package com.voip.calc.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Core domain object representing all input data required for rate calculation.
 * This is an immutable value object — once constructed its state cannot change.
 */
public final class CallContext {
    private final PhoneNumber callerNumber;
    private final PhoneNumber calleeNumber;
    private final CustomerType customerType;
    private final LocalDateTime callStartTime;

    public CallContext(PhoneNumber callerNumber, PhoneNumber calleeNumber,
                       CustomerType customerType, LocalDateTime callStartTime) {
        this.callerNumber = Objects.requireNonNull(callerNumber, "callerNumber must not be null");
        this.calleeNumber = Objects.requireNonNull(calleeNumber, "calleeNumber must not be null");
        this.customerType = Objects.requireNonNull(customerType, "customerType must not be null");
        this.callStartTime = Objects.requireNonNull(callStartTime, "callStartTime must not be null");
    }

    public static CallContext of(String callerNumber, String calleeNumber,
                                  String customerType, LocalDateTime callStartTime) {
        return new CallContext(
            PhoneNumber.of(callerNumber),
            PhoneNumber.of(calleeNumber),
            CustomerType.fromString(customerType),
            callStartTime
        );
    }

    public PhoneNumber getCallerNumber() {
        return callerNumber;
    }

    public PhoneNumber getCalleeNumber() {
        return calleeNumber;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public LocalDateTime getCallStartTime() {
        return callStartTime;
    }

    public CountryCode getDestinationCountry() {
        return calleeNumber.getCountryCode();
    }

    public RateZone getRateZone() {
        return RateZoneResolver.resolve(callStartTime.toLocalTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallContext that = (CallContext) o;
        return callerNumber.equals(that.callerNumber)
            && calleeNumber.equals(that.calleeNumber)
            && customerType == that.customerType
            && callStartTime.equals(that.callStartTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callerNumber, calleeNumber, customerType, callStartTime);
    }

    @Override
    public String toString() {
        return "CallContext{caller=" + callerNumber + ", callee=" + calleeNumber
            + ", customerType=" + customerType + ", callStartTime=" + callStartTime + "}";
    }
}
