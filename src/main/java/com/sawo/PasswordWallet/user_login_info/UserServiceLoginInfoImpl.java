package com.sawo.PasswordWallet.user_login_info;

import com.sawo.PasswordWallet.exception_handler.UnsuccessfulLoginThreshold;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceLoginInfoImpl implements UserLoginInfoService {

    private static final Integer ACCOUNT_LOCKOUT_TIME_IN_MIN = 2;

    private final UserLoginInfoRepository userLoginInfoRepository;

    public void searchForConsecutiveFailedLogins(String ipAddress, LocalDateTime loginTime) {

        List<UserLoginInfo> previousFailedLoginsWhichCount = this.userLoginInfoRepository.findAllByIpAddressAndCountDuringLoginAttemptTrue(ipAddress)
                .orElse(new ArrayList<>()).stream().sorted(Comparator.comparing(UserLoginInfo::getLoginTime)).collect(Collectors.toList());

        if (previousFailedLoginsWhichCount.size() == 3) {
            LocalDateTime lastFailedLoginTime = previousFailedLoginsWhichCount.get(previousFailedLoginsWhichCount.size() - 1).getLoginTime();
            LocalDateTime endOfAccountLockout = lastFailedLoginTime.plusMinutes(ACCOUNT_LOCKOUT_TIME_IN_MIN);

            long timeToUnblockingInMilis = Duration.between(endOfAccountLockout, loginTime).toMillis();

            if (LocalDateTime.now().isAfter(endOfAccountLockout)) {
                previousFailedLoginsWhichCount.forEach(userLoginInfo -> userLoginInfo.setCountDuringLoginAttempt(false));
                this.userLoginInfoRepository.saveAll(previousFailedLoginsWhichCount);
                return;
            } else {
                throw new UnsuccessfulLoginThreshold(
                        "Your account is blocked due to multiple previous unsuccessful login attempts. " +
                                "Try again in " + this.getTimeToUnblockingString(timeToUnblockingInMilis));
            }
        }
    }

    private String getTimeToUnblockingString(long timeToUnblockingInMilis) {
        String timeToUnblockingString = String.format("%d minutes %d seconds",
                TimeUnit.MILLISECONDS.toMinutes(timeToUnblockingInMilis),
                TimeUnit.MILLISECONDS.toSeconds(timeToUnblockingInMilis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeToUnblockingInMilis)));

        return timeToUnblockingString.replace("0 minutes", "").replace("-", "");
    }

    public void saveUserServiceLoginAttempt(String ipAddress, String masterLogin, LocalDateTime loginTime, boolean successful, boolean countDuringLoginAttempt) {

        if (successful) {
            Optional<List<UserLoginInfo>> previousFailedLoginsWhichCount = this.userLoginInfoRepository.findAllByIpAddressAndCountDuringLoginAttemptTrue(ipAddress);
            if (previousFailedLoginsWhichCount.isPresent()) {
                previousFailedLoginsWhichCount.get().forEach(userLoginInfo -> userLoginInfo.setCountDuringLoginAttempt(false));
                this.userLoginInfoRepository.saveAll(previousFailedLoginsWhichCount.get());
            }
        }

        this.userLoginInfoRepository.save(
                UserLoginInfo.builder().ipAddress(ipAddress).masterLogin(masterLogin).loginTime(loginTime)
                        .successful(successful).countDuringLoginAttempt(countDuringLoginAttempt).build()
        );
    }

    public UserLoginInfo findLastFailedOrSuccessfulUserLogin(String ipAddress, boolean successful) {
        return this.userLoginInfoRepository.findLastFailedOrSuccessfulUserLogin(ipAddress, successful).orElse(null);
    }

}
