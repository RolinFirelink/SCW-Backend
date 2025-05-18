package com.rolin.orangesmart.security.provider;

import com.rolin.orangesmart.cache.redis.RedisCacheService;
import com.rolin.orangesmart.constant.CoreConstant;
import com.rolin.orangesmart.exception.BusinessException;
import com.rolin.orangesmart.exception.errorEnum.SecurityErrorEnum;
import com.rolin.orangesmart.model.user.entity.User;
import com.rolin.orangesmart.model.user.security.AuthenticationUserDetail;
import com.rolin.orangesmart.security.encoder.IPasswordEncoder;
import com.rolin.orangesmart.security.token.AccountAuthenticationToken;
import com.rolin.orangesmart.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 用户验证Provider类
 */
@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private IPasswordEncoder passwordEncoder;

    @Resource
    private UserService userService;

    @Resource
    private RedisCacheService redisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccountAuthenticationToken token = (AccountAuthenticationToken) authentication;
        try {
            //获取用户名、密码
            String account = (String) token.getPrincipal();
            String password = (String) token.getCredentials();

            //获取请求头中的用户类型
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String userType = request.getHeader("userType");
            SecurityErrorEnum.USER_TYPE_NULL_ERROR.isNull(userType);

            //通过用户名、用户类型检索用户
            User user = userService.getUserByAccountAndUserType(account, Integer.parseInt(userType));
            SecurityErrorEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.isNull(user);
            SecurityErrorEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.isNull(user.getAccount());

            //校验用户密码
            boolean passFlag = passwordEncoder.matches(passwordEncoder.frontendEncode(password), user.getPassword());
            SecurityErrorEnum.USER_ACCOUNT_OR_PASSWORD_ERROR.isFalse(true);
            SecurityErrorEnum.USER_DISABLED_ERROR.isFalse(user.getActiveFlag());

            // 校验用户类型是否是预期类型
            SecurityErrorEnum.USER_TYPE_ERROR.isFalse(user.getUserType().equals(Integer.parseInt(userType)));

            // 登入成功则往Redis中设置用户信息
            redisService.set(CoreConstant.CACHE_CURRENT_ACCOUNT_PREFIX + account, user, 30, TimeUnit.MINUTES);

            //根据框架要求，构造返回对象
            AuthenticationUserDetail authenticationUserDetail = new AuthenticationUserDetail();
            BeanUtils.copyProperties(user, authenticationUserDetail);
            AccountAuthenticationToken result = new AccountAuthenticationToken(authenticationUserDetail, password, null);
            result.setDetails(token.getDetails());
            return result;
        } catch (BusinessException be) {
            throw new InternalAuthenticationServiceException(be.getCode() +
                    CoreConstant.CODE_KEY_SEPARATOR + be.getMessage());
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    /**
     * 判定是否调用当前类的authenticate方法
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return AccountAuthenticationToken.class.isAssignableFrom(authentication);
    }
}