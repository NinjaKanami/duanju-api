package com.sqx.modules.utils;

import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 邀请码生成解密工具类
 * @author fang
 * @date 2020/7/8
 */
@Component
public class InvitationCodeUtil {

    private static CommonInfoService commonInfoService;
    private static UserService userService;

    @Autowired
    public void setCommonRepository(CommonInfoService commonInfoService,UserService userService) {
        InvitationCodeUtil.commonInfoService = commonInfoService;
        InvitationCodeUtil.userService = userService;
    }


    /** 自定义进制（选择你想要的进制数，不能重复且最好不要0、1这些容易混淆的字符） */
    private static final char[] r=new char[]{ 'M', 'J', 'U', 'D', 'Z', 'X', '9', 'C', '7', 'P','E', '8', '6', 'B', 'G', 'H', 'S', '2', '5',  'F', 'R', '4','Q', 'W', 'K', '3', 'V', 'Y', 'T', 'N'};

    /** 定义一个字符用来补全邀请码长度（该字符前面是计算出来的邀请码，后面是用来补全用的） */
    private static final char b='A';

    /** 进制长度 */
    private static final int binLen=r.length;

    /** 邀请码长度 */
    private static final int s=6;


    /** 补位字符串 */
    private static final String e="KSLFXFR";

    /**
     * 根据ID生成六位随机码
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digit = ThreadLocalRandom.current().nextInt(0, 10);
            sb1.append(digit);
        }
        String randomNum = sb1.toString();
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        String str=new String(buf, charPos, (32 - charPos));
        // 不够长度的自动补全
        if(str.length() < s) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(e.subSequence(0, s-str.length()));
            str+=stringBuilder.toString();
        }
        String code=randomNum+str;
        UserEntity userEntity = userService.queryByInvitationCode(code);
        if(userEntity==null){
            return code;
        }else{
            return toSerialCode(id);
        }

    }

    /**
     * 根据随机码生成ID
     * @param code 随机码
     * @return ID
     */
    public static long codeToId(String code) {
        return userService.queryByInvitationCode(code).getUserId();
    }






}