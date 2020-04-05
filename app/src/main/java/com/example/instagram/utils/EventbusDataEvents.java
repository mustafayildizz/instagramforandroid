package com.example.instagram.utils;

public class EventbusDataEvents {

   public static class SendCredentials {
        String number;
        String email;
        String verificationID;
        String code;
        boolean isEmail;

       public SendCredentials(String number, String email, String verificationID, String code, boolean isEmail) {
           this.number = number;
           this.email = email;
           this.verificationID = verificationID;
           this.code = code;
           this.isEmail = isEmail;
       }


       public boolean isEmail() {
           return isEmail;
       }

       public void setEmail(boolean email) {
           isEmail = email;
       }

       public String getEmail() {
           return email;
       }

       public void setEmail(String email) {
           this.email = email;
       }

       public String getVerificationID() {
           return verificationID;
       }

       public void setVerificationID(String verificationID) {
           this.verificationID = verificationID;
       }

       public String getCode() {
           return code;
       }

       public void setCode(String code) {
           this.code = code;
       }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }



}
