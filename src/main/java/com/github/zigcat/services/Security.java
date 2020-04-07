package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.UserController;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class Security {
    private static Logger l = LoggerFactory.getLogger(Security.class);
    public static String serverErrorMessage = "Stopped by internal server error(500)";
    public static String badRequestMessage = "Stopped by client's bad request(400)";
    public static String unauthorizedMessage = "Stopped by unauthorized or forbidden request(403)";

    public static User authorize(String login, String password){
        try {
            l.info("!!!\tchecking access");
            List<User> userList = UserController.userDao.queryForAll();
            for(User u: userList){
                if(u.getEmail().equals(login) && BCrypt.checkpw(password, u.getPassword())){
                    l.info(u.toString()+" got access");
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NotFoundException(Security.unauthorizedMessage);
    }

    public static boolean isValidEmail(String email){
        email = email.trim();
        EmailValidator eval = EmailValidator.getInstance();
        if(eval.isValid(email)){
            return true;
        }
        return false;
    }

    public static boolean isValidDate(String date){
        int spaceCounter = 0, numCounter = 0;
        if(date.equals("0000")){
            return false;
        } else {
            for(int i=0;i<date.length();i++){
                if(date.charAt(i) == ' '){
                    spaceCounter ++;
                } else if(date.charAt(i) >= '0' && date.charAt(i) <= '9'){
                    numCounter++;
                }
            }
            return numCounter == 8 && spaceCounter == 2;
        }
    }
}
