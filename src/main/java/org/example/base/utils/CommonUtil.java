package org.example.base.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpServletRequest;
import org.example.base.constants.Constant;
import org.example.base.models.dto.ApiOutput;
import org.example.base.models.dto.Event;
import org.example.base.models.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:14 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private CommonUtil() {}

    /**Kiểm tra trong text có chứa kí tự đặc biệt không*/
    public static Boolean containSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**Lấy thời gian hiện tại theo pattern yyyy-MM-dd HH:mm:ss */
    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")).getTime());
    }
    /**Lấy timestamp lúc 00:00 của ngày hiện tại*/
    public static Long getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**Lấy thời gian hiện tại theo milisecond, tiến hành cộng thêm một khoảng thời gian theo giây, phút, giờ, ngày, tháng, năm. Nếu như muốn trừ thời gian thì để giá trị number là số âm. Kết quả trả lại là timestamp sau khi đã được cộng thời gian.*/
    public static Long additionToDate(Long timeStamp, String type, Integer number){
        Date date = new Date(timeStamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (type){
            case Constant.Time.SECOND:
                calendar.add(Calendar.SECOND, number);
                break;
            case Constant.Time.MINUTE:
                calendar.add(Calendar.MINUTE, number);
                break;
            case Constant.Time.HOUR:
                calendar.add(Calendar.HOUR, number);
                break;
            case Constant.Time.DATE:
                calendar.add(Calendar.DATE, number);
                break;
            case Constant.Time.MONTH:
                calendar.add(Calendar.MONTH, number);
                break;
            case Constant.Time.YEAR:
                calendar.add(Calendar.YEAR, number);
                break;
            default:
        }
        date = calendar.getTime();
        return date.getTime();
    }

    /**Đổi timestamp sang datetime theo pattern mặc định*/
    public static String convertTimestampToDate(Long timeStamp){
        if (timeStamp != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(timeStamp);
        }
        return null;
    }

    /**Đổi timestamp sang định dạng datetime theo pattern do người dùng qui định */
    public static String convertTimestampToDate(Long timeStamp, String pattern){
        if (timeStamp != null){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date date = new Date(timeStamp);
                return sdf.format(date);
            } catch (Exception e) {
                logger.error("Error in convertTimeStampToDate: {}", e.getMessage());
            }
        }
        return null;
    }

    /**Thêm một số ngày kể từ thời điểm date*/
    public static Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**Loại bỏ kí cuối cùng của dãy*/
    public static String removeLastCharacter(String str){
        if (str != null && str.length() > 0){
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**Tách tên file ra khỏi đường dẫn*/
    public static String getFileName(String path){
        if (path == null){
            return null;
        }
        int index = path.lastIndexOf("/");
        String fileName = path.substring(index + 1);
        return fileName;
    }

    /**Tạo QR code có nội dung text, kích thước cùng vị trí để lưu QR sau khi tạo*/
    public static void generateQRCodeImage(String text, int with, int height, String filePath, String fileName)
            throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, with, height);
        File tmp = new File(filePath, fileName + ".png");
        tmp.createNewFile();
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", tmp);
    }

    /**Lấy một số bất kì trong khoảng cho trước*/
    public static int getRandomNumberInRange(int min, int max) {
        if (min > max){
            logger.error("Error in getRandomNumberInRange: max must be greater than min");
            return -2147483648;
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**Kiểm tra validate cho email*/
    public static boolean emailValidator(String email) {
        Pattern p = Pattern.compile(Constant.Regex.EMAIL);
        if (email == null)
            return false;
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**Kiểm tra validate cho số điện thoại khu vực Việt Nam*/
    public static boolean phoneVNValidator(String phone) {
        Pattern p = Pattern.compile(Constant.Regex.VN_PHONE);
        if (phone == null)
            return false;
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**Kiểm tra validate cho mật khẩu
     * type true: mật khẩu attt
     * false: mật khẩu thường*/
    public static boolean passwordValidator(String password, boolean type) {
        Pattern p;
        if (password == null)
            return false;
        if(type){
            p = Pattern.compile(Constant.Regex.ATTT_PASSWORD);
        } else {
            p = Pattern.compile(Constant.Regex.PASSWORD);
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**Chuyển đổi kiểm tra event có bị lỗi hay không để phục vụ lấy dữ liệu*/
    public static ApiOutput packing(Event event){
        ApiOutput apiOutput = new ApiOutput();
        if (event.errorCode == null)
            event.errorCode = Constant.ResultStatus.SUCCESS;
        apiOutput.setStatus(event.errorCode);
        if (event.errorCode != Constant.ResultStatus.SUCCESS){
            ErrorInfo errorInfo = ObjectMapperUtil.objectMapper(event.payload, ErrorInfo.class);
            apiOutput.setMessage(errorInfo.getErrorKey());
        }
        return apiOutput;
    }

    /**Lấy dữ liệu từ header*/
    public static Map<String, String> getHeadersInfo(HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**Tạo một chuỗi mật khẩu ngẫu nhiên*/
    public static String generateRandomStringPassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialChars = "@$!%*?&";
        //Kết hợp tất cả kí tự lại
        String allChars = lowercase + uppercase + digits + specialChars;
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        //Đảm bảo mỗi loại có ít nhất 1 kí tự
        result.append(lowercase.charAt(random.nextInt(lowercase.length())))
                .append(uppercase.charAt(random.nextInt(uppercase.length())))
                .append(digits.charAt(random.nextInt(digits.length())))
                .append(specialChars.charAt(random.nextInt(specialChars.length())));
        //Tạo thêm các kí tự ngẫu nhiên cho đủ độ dài tối thiểu
        for (int i = 4; i < 8; i++) {
            result.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        //Trộn ngẫu nhiên
        char[] chars = result.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = random.nextInt(chars.length);
            char tmp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = tmp;
        }
        return new String(chars);
    }

    /**Đảm bảo mật khẩu thoả mãn theo biểu thức chính qui*/
    public static String randomPassword(){
        String randomString = generateRandomStringPassword();
        while (!passwordValidator(randomString, true)) {
            randomString = generateRandomStringPassword();
        }
        return randomString;
    }
}
